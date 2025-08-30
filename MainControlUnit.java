
public class MainControlUnit {
    ioPortAPI api;


    public MainControlUnit(ioPortAPI api){
        this.api = api;
        portConnect(333);
        MCUsend("TEST send");
    }
    public void MCUsend(String msg){
        api.send(msg);

    }
    public void portConnect(int Connector){
        api.ioport(Connector);
    }

    public static void main(String[] args){
        ioPortAPI API = new ioPortAPI();
        MainControlUnit mcu = new MainControlUnit(API);

    }
}
