import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

// Central location for all connections
public class hub {
    Map<Integer, ioPort> devices = new HashMap<>();
    int numOfDevices = 4;

    /*
     * Connection Map:
     * ---------------
     * Screen: 1
     * Flow Meter: 2
     * Card Reader: 3
     * Hose: 4
     * 
     * (*NOTE: Connections also need to begin in that sequence too.*)
     */

    public hub() throws IOException, InterruptedException{

        // Connect to each device
        for(int i = 1; i <= numOfDevices; i++){
            ConnectToDevice(i);
        }

        String displayScreen = "";
        String in;
        int recipient;
        String meter; // FM0 = Off; FM1 = On;

        // Cycle through screens on 5 second timer
        while (true) {
            // Welcome Screen
            displayScreen = "t01/s1B/f1/c5/\"Welcome!\":t23/s3R/f1/c5/\"Use the card reader to begin your transaction.\":t45/s1B/f1/c5/\"*\"";
            recipient = 1;
            send(displayScreen, recipient);
            System.out.println("Hub Sending: \n" + displayScreen + "\n\n");
            in = get();
            while(in.charAt(5)!='3'){
                in = get();
                System.out.println("Hub Recieving: \n" + in);
            }

            // Select Fuel Screen
            displayScreen = "t01/s2R/f1/c5/\"Payment approved. Select fuel type:\":t2/s3I/f1/c5/\"Unleaded\":" +
                    "t3/s3R/f1/c5/\"Confirm\":t4/s3I/f1/c5/\"Premium\":t6/s3I/f1/c5/\"Premium Plus\":t8/s3I/f1/c5/\"Gasoline\"";
            recipient = 1;
            send(displayScreen, recipient);
            System.out.println("Hub Sending: \n" + displayScreen + "\n\n");
            in = get();
            while(in.charAt(5)!='1'){
                in = get();
                System.out.println("Hub Recieving: \n" + in);
            }

            // Attach Hose Screen
            displayScreen = "t23/s2R/f1/c5/\"Attach the hose to your vehicle's \\n       gas tank to begin fueling.\":t45/s1B/f1/c5/\"*\"";
            recipient = 1;
            send(displayScreen, recipient);
            System.out.println("Hub Sending: \n" + displayScreen + "\n\n");
            in = get();
            while(in.charAt(5)!='4'){
                in = get();
                System.out.println("Hub Recieving: \n" + in);
            }
            recipient = 2;
            meter = "FM1"; // Flow meter on
            send(meter, recipient);

            // Fueling Screen
            displayScreen = "t23/s2R/f1/c5/\"Fueling in progress...\":t45/s1B/f1/c5/\"*\"";
            recipient = 1;
            send(displayScreen, recipient);
            System.out.println("Hub Sending: \n" + displayScreen + "\n\n");
            in = get();
            while(!in.equals("Port 4: Tank Full")){
                if(in.equals("Port 4: Hose detached")) { break; }
                in = get();
                System.out.println("Hub Recieving: \n" + in);
            }
            recipient = 2;
            meter = "FM0"; // Flow meter off
            Thread.sleep(3000);
            send(meter, recipient);

            // Final Screen
            displayScreen = "t01/s2B/f1/c5/\"Transaction complete.\":t45/s3I/f1/c5/\"Thank you!\":t67/s1B/f1/c5/\"*\"";
            recipient = 1;
            send(displayScreen, recipient);
            System.out.println("Hub Sending: \n" + displayScreen + "\n\n");
            Thread.sleep(3000);
        }
    }

    // Send data to all devices
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
    
    // Start a connection to a device
    public void ConnectToDevice(int Connector) throws UnknownHostException, IOException{
        ioPort api = ioPort.ChooseDevice(4); // 4 is StatusPort Type for servers
        api.ioport(Connector);
        devices.put(Connector, api);
        System.out.println("Connector " + Connector + " initialized.");
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        new hub();
        
    }
}
