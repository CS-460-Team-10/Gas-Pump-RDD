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
     * Hose: 4
     * Flow Meter: 2
     * Card Reader: 1
     * Screen: 4
     * 
     * 
     * (*NOTE: Connections also need to begin in that sequence too.*)
     */

    public hub() throws IOException, InterruptedException{

        // Connect to each device
        // for(int i = 1; i <= numOfDevices; i++){
        //     ConnectToDevice(i);
        // }

        ConnectToDevice(1); //Credit Card
        ConnectToDevice(2); // Folwmeter
        ConnectToDevice(4);// Hose

        while (true) {
            // Talk to Card Reader
            send("Please swipe card.", 1);
            System.out.println("Hub -> CardReader");
            System.out.println("CardReader -> Hub: " + get(1));

            // Talk to Flow Meter
            send("Start Flowing!", 2);
            System.out.println("Hub -> FlowMeter");
            System.out.println("FlowMeter -> Hub: " + get(2));

            //  Talk to Hose
            send("Check Hose Status.", 4);
            System.out.println("Hub -> Hose");
            System.out.println("Hose -> Hub: " + get(4));
            Thread.sleep(2000); 
        }
    }

    // Send data to all devices
    public void send(String msg, int recipient){
        devices.get(recipient).send(msg);
    }

    // Get data from all devices
    public String get(int recipient) throws IOException, InterruptedException{
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
        ioPort api = new CommunicatorPort();
        api.ioport(Connector);
        devices.put(Connector, api);
        System.out.println("Connector " + Connector + " initialized.");
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        new hub();
        
    }
}
