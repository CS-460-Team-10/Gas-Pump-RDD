import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

// Central location for all connections
public class hub {
    // Device IDs/mapping
    private static final int SCREEN = 1;
    private static final int FLOW_METER = 2;
    private static final int CARD_READER = 3; // - Unused variable
    private static final int HOSE = 4;
    private static final int BANK = 5;
    private static final int GAS_STATION = 6;
    private final Map<Integer, ioPort> devices = new HashMap<>();
    private final int deviceCount = 6;


    // UI display markup codes
    private static final String UI_WELCOME =
        "t01/s1B/f1/c5/\"Welcome!\":t23/s3R/f1/c5/\"Use the card reader to begin your transaction.\":t45/s1B/f1/c5/\"*\"";

    private static final String UI_SELECT_FUEL =
        "t01/s2R/f1/c5/\"Payment approved. Select fuel type:\":t2/s3I/f1/c5/\"Unleaded\":"
      + "t3/s3R/f1/c5/\"Confirm\":t4/s3I/f1/c5/\"Premium\":t6/s3I/f1/c5/\"Premium Plus\":t8/s3I/f1/c5/\"Gasoline\"";

    private static final String UI_ATTACH_HOSE =
        "t23/s2R/f1/c5/\"Attach the hose to your vehicle's \\n       gas tank to begin fueling.\":t45/s1B/f1/c5/\"*\"";

    private static final String UI_FUELING =
        "t23/s2R/f1/c5/\"Fueling in progress...\":t45/s1B/f1/c5/\"*\"";

    private static final String UI_FINAL =
        "t01/s2B/f1/c5/\"Transaction complete.\":t45/s3I/f1/c5/\"Thank you!\":t67/s1B/f1/c5/\"*\"";

    // Flow meter commands
    private static final String METER_ON = "FM1";
    private static final String METER_OFF = "FM0";

    // Hub client
    public hub() throws IOException, InterruptedException{

        // Connect to each device
        for(int i = 1; i <= deviceCount; i++){
            ConnectToDevice(i);
        }

        String in;
        boolean midDisco;

        // Cycle through screens on 5 second timer
        while (true) {
            // *Welcome Screen*
            send(UI_WELCOME, SCREEN);
            System.out.println("Hub Sending: \n" + UI_WELCOME + "\n\n");
            in = get();
            while(in.charAt(5)!='3'){
                in = get();
                System.out.println("Hub Recieving: \n" + in);
            }

            // *Select Fuel Screen*
            send(UI_SELECT_FUEL, SCREEN);
            System.out.println("Hub Sending: \n" + UI_SELECT_FUEL + "\n\n");
            midDisco = false;
            in = get();
            while(in.charAt(5)!='1' && (!in.contains("bps2/3") || !in.contains("bps4/3") || !in.contains("bps6/3") || !in.contains("bps8/3"))){ // Ensure button sequencing
                in = get();
                System.out.println("Hub Recieving: \n" + in);
            }

            // *Attach Hose Screen*
            send(UI_ATTACH_HOSE, SCREEN);
            System.out.println("Hub Sending: \n" + UI_ATTACH_HOSE + "\n\n");
            in = get();
            while(in.charAt(5)!='4'){
                in = get();
                System.out.println("Hub Recieving: \n" + in);
            }
            send(METER_ON, FLOW_METER);

            // *Fueling Screen*
            send(UI_FUELING, SCREEN);
            System.out.println("Hub Sending: \n" + UI_FUELING + "\n\n");
            in = get();
            while(!in.equals("Port 4: Tank Full")){

                if(in.equals("Port 4: Hose detached")) { 
                    midDisco = true; break;  // Mid fuel disconnect stop - initiate emergency stop
                }

                if(in.contains("Gal Pumped:")) { 
                    send(in, HOSE); 
                }

                in = get();
                System.out.println("Hub Recieving: \n" + in);
            }
            // Clean fuel stop
            if(!midDisco){
                Thread.sleep(3000); // Wait so user can see fuel purchase amount
                send(METER_OFF, FLOW_METER);
            } 
            // Mid fuel flow valve disconnect - emergency stop initiated
            else {
                send(METER_OFF, FLOW_METER);
                Thread.sleep(3000);
            }

            // *Final Screen*
            send(UI_FINAL, SCREEN);
            System.out.println("Hub Sending: \n" + UI_FINAL + "\n\n");
            Thread.sleep(3000);
        }
    }

    // Send data to a recipient
    public void send(String msg, int recipient){
        devices.get(recipient).send(msg);
    }

    // Get data from all devices
    public String get() throws IOException, InterruptedException{
        while(true){
            for (ioPort api : devices.values()) {
                String msg = api.get();
                if (msg != null){
                    return msg;
                }
            }
            Thread.sleep(50);
        }
    }
    
    // Start a connection to a specified device
    public void ConnectToDevice(int connector) throws UnknownHostException, IOException {
        int type; // deviceType for ChooseDevice (client role)
        switch (connector) {
            case FLOW_METER:
                type = 2; // ControlPort (client)
                break;
            case HOSE:
                type = 4; // MonitorPort (client)
                break;
            default: // Screen (1), CardReader (3), or anything else
                type = 1; // CommunicatorPort (client)
        }

        ioPort api = ioPort.ChooseDevice(type); // hub is client
        api.ioport(connector);                   // connector selects which device/port 
        devices.put(connector, api);
        System.out.println("Connector " + connector + " initialized.");
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        new hub();
    }
}
