import java.io.IOException;
import java.net.UnknownHostException;

/* 
 * There are no comments.
 * I dont fully understand what this MCU class is supposed to be. What is the point of get and send here?
 * MainControlUnit could be named something like "hub" and can deal with all connections here.
 *      It might make sense to make it not take anything and simply be an object containing all connections.
 * Port connection integer values should be obtained from a map or table of some kind.
 * 
 * Otherwise goodjob.
 * -Alex
*/

public class MainControlUnit {
    ioPort api;

    public MainControlUnit() throws IOException, InterruptedException{
        // ConnectToDevice(2);
        // MCUsend("Card 4232");
        // ConnectToDevice(2);
        // MCUsend("SENDING TO FLOW METER.");
        ConnectToDevice(4);
        while (true) {
            System.out.println(MCUget());
        }
    }
    public void MCUsend(String msg){
        api.send(msg);

    }
    public void ConnectToDevice(int Connector) throws UnknownHostException, IOException{
        api = ioPort.ChooseDevice(Connector);
        api.defineAsClient(Connector);
    }
    public String MCUget () throws IOException, InterruptedException{
        while(true){
            String msg = api.get();
            if (msg != null){
                return msg;
            }
            Thread.sleep(50);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        new MainControlUnit();
    }
}
