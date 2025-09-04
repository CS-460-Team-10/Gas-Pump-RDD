import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

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

        send("MCU Connected.");
        
        while (true) {
            System.out.println(get());
        }
    }

    // Send data to all devices
    public void send(String msg){
        for (ioPort api : devices.values()) {
            api.send(msg);
        }
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
        System.out.println("Connector " + Connector + " ready.");
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        new hub();
    }
}
