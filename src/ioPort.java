import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

// Base Abstract class for all Port types
// Hub Uses Client Ports, Devices Use The Server Port
abstract class ioPort{
    int connector;
    Socket socket;
    ServerSocket srSocket;
    PrintWriter Out;
    BufferedReader deviceResponse;
    String LastMessage;
    HashMap<Integer, Integer> DevicePorts = new HashMap<>();


    public ioPort(){
        // Device to Port Mapping, Used for socket connection and distinction
        DevicePorts.put(1,  333); // Screen
        DevicePorts.put(2,  555); // Flowmeter 
        DevicePorts.put(3,  888); // CreditCardReader
        DevicePorts.put(4, 999); // Hose
        DevicePorts.put(5, 777); // Bank 
        DevicePorts.put(6, 666); // gas station

    }
    // used for Opening the connetion for this port, either client or Server, on the given connector
    public abstract void ioport(int Connector) throws UnknownHostException, IOException;

    // send a single line message
    public void send(String Message){
        if (Out != null){
            Out.println(Message);
        }
    }
    // Non-blocking poll which returns the next line if available, otherwise null
    // Acts like a one-slot queue. Used by the hub to receive device messages    
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
    // Quick read without clearing the saved line (returns the last seen line)
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
    // Choose a port specialization by role, not by connector id, so 
    // 1 = Communicator (client)
    // 2 = Control (client)
    // 3 = Status (server)      
    //4 = Monitor (client)
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

// Communicator port Used for general communciation between hub and the servers
class CommunicatorPort extends ioPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

}
//Status Port Used for receiving status updates from devices to hub, Our server-to-Client Communciator
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
// ControlPort, mainly used for communciation with flowmeter 
class ControlPort extends ioPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}

// MonitorPort, Mainly used for communication with the GUI
class MonitorPort extends ioPort {
    @Override
    public void ioport(int Connector) throws UnknownHostException, IOException {
        connector = Connector;
        socket = new Socket("localhost", DevicePorts.get(Connector));
        Out = new PrintWriter(socket.getOutputStream(), true);
        deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}

