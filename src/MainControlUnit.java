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
    IoPort api;

    public MainControlUnit() throws IOException, InterruptedException{
        ConnectToDevice(1);
        MCUsend("AUTH req for Device 1");

        System.out.println(MCUget());

    }
    public void MCUsend(String msg){
        api.send(msg);

    }
    public void ConnectToDevice(int Connector) throws UnknownHostException, IOException{
        api = IoPort.ChooseDevice(Connector);
        api.ioport(Connector);
    }
    public String MCUget () throws IOException, InterruptedException{
        while(true){
            api.poll();
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
