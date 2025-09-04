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
     * Hose: 1
     * Flow Meter: 2
     * Card Reader: 3
     * Screen: 4
     * 
     * 
     * (*NOTE: Connections also need to begin in that sequence too.*)
     */

    public hub() throws IOException, InterruptedException{

        // Connect to each device
        for(int i = 1; i <= numOfDevices; i++){
            ConnectToDevice(i);
        }

        String thing = "t01/s1B/f1/c4/\"Welcome!\":t23/s3R/f1/c4/\"Use the card reader to begin your transaction.\":t45/s1B/f1/c4/\"*\""; // Welcome screen
        int recipient = 4;

        while (true) {
            send(thing, recipient);
            System.out.println("Hub Sending: \n" + thing + "\n\n");
            System.out.println("Hub Recieving: \n" + get());
            Thread.sleep(500);
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
