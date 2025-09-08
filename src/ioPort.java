import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

abstract class ioPort{
    int connector;
    Socket socket;
    ServerSocket srSocket;
    PrintWriter Out;
    BufferedReader deviceResponse;
    String LastMessage;
    HashMap<Integer, Integer> DevicePorts = new HashMap<>();


    public ioPort(){
        DevicePorts.put(1,  333);
        DevicePorts.put(2,  555);
        DevicePorts.put(3,  888);
        DevicePorts.put(4, 999);

    }
    public abstract void ioport(int Connector) throws UnknownHostException, IOException;


    public void send(String Message){
        if (Out != null){
            Out.println(Message);
        }
    }
    public String get() {
        try {
            if (deviceResponse != null && deviceResponse.ready()) {
                LastMessage = "Port " + connector + ": " + deviceResponse.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        String tmp = LastMessage;
        LastMessage = null;
        return tmp;
    }

    public String read() {

    try {
            if (deviceResponse != null && deviceResponse.ready()) {
                LastMessage = "Port " + connector + ": " + deviceResponse.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LastMessage;
    }

    public static ioPort ChooseDevice(int Connector){
        if (Connector == 1){
            return new CommunicatorPort();        
        }
        if (Connector == 2){
            return new ControlPort();
        }
        if (Connector == 3){
            return new StatusPort();
        }
        if (Connector == 4){
            return new MonitorPort();
        }
        return null;
    }

}
class CommunicatorPort extends ioPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

}

class StatusPort extends ioPort {
    ServerSocket srSocket;
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        srSocket = new ServerSocket(DevicePorts.get(Connector));
        socket = srSocket.accept();
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

}

class ControlPort extends ioPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}


class MonitorPort extends ioPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}
