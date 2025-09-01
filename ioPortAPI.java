import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;



public class ioPortAPI {
    Socket socket;
    PrintWriter Out;
    BufferedReader deviceResponse;
    String LastMessage;
    
    public void ioport(int Connector) throws UnknownHostException, IOException{
            socket = new Socket("localhost",Connector);
            Out = new PrintWriter(socket.getOutputStream(), true);
            deviceResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try{
                    String response;
                    while((response = deviceResponse.readLine()) != null) {
                        LastMessage =  "Port " + Connector + ": " + response;
                    }
                }
                catch(IOException exception){
                    exception.printStackTrace();
                }
            }).start();

    }
    public void send(String Message){
        if (Out != null){
            Out.println(Message);
        }
    }
    public String get() throws IOException{
        return LastMessage;

    }
    public void read(){

    }
}
