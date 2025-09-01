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
    ioPortAPI api;

    public MainControlUnit(ioPortAPI api) throws IOException, InterruptedException{
        this.api = api;
        portConnect(333);
        portConnect(555);
        MCUsend("AUTH req for Device 1");
        MCUsend("Ping Device 2");

        System.out.println(MCUget());

    }
    public void MCUsend(String msg){
        api.send(msg);

    }
    public void portConnect(int Connector) throws UnknownHostException, IOException{
        api.ioport(Connector);
    }
    public String MCUget () throws IOException, InterruptedException{
        while(api.get() == null){
            Thread.sleep(50);
        }
        return api.get();
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        ioPortAPI API = new ioPortAPI();
        new MainControlUnit(API);
    }
}
