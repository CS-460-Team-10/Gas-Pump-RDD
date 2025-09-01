import java.io.IOException;
import java.net.UnknownHostException;

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
