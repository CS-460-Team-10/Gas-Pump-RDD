import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Queue;


/* 
 * There are no comments.
 * Get method should return the lastMesage, but also completely remove it from the que so it no longer exists.
 * Read should just return the lastMessage.
 * ioPort may need to be a subclass within the ioPortAPI, containing the send, get, and read functions.
 * 
 * Otherwise goodjob.
 * -Alex
*/

abstract class IoPort{
    int connector;
    Socket socket;
    PrintWriter Out;
    BufferedReader deviceResponse;
    String LastMessage;
    HashMap<Integer, Integer> DevicePorts = new HashMap<>();


    public IoPort(){
        DevicePorts.put(1,  333);
        DevicePorts.put(2,  555);
        DevicePorts.put(3,  888);


    }
    public abstract void ioport(int Connector) throws UnknownHostException, IOException;



    public void poll() {
        try {
            if (deviceResponse != null && deviceResponse.ready()) {
                LastMessage = "Port " + connector + ": " + deviceResponse.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void send(String Message){
        if (Out != null){
            Out.println(Message);
        }
    }
    public String get() {
        String tmp = LastMessage;
        LastMessage = null;
        return tmp;
    }

    public String read() {
        return LastMessage;
    }

    public static IoPort ChooseDevice(int Connector){
        if (Connector == 1){
            return new StatusPort();
        }
        if (Connector == 2){
            return new CommunicatorPort();
        }
        if (Connector == 3){
            return new ActuatorPort();
        }
        return null;
    }

}
class CommunicatorPort extends IoPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public String read() {
        throw new UnsupportedOperationException("Communicator cannot read");
    }
}



class StatusPort extends IoPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void send(String message) {
        throw new UnsupportedOperationException("Status cannot send");
    }

    @Override
    public String get() {
        throw new UnsupportedOperationException("Status cannot get");
    }
}

class ActuatorPort extends IoPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public String get() {
        throw new UnsupportedOperationException("Actuator cannot get");
    }

    @Override
    public String read() {
        throw new UnsupportedOperationException("Actuator cannot read");
    }
}