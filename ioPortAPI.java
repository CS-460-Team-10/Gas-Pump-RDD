import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ioPortAPI {
    Socket socket;
    PrintWriter Out;


    public void ioport(int Connector){
    try{
        socket = new Socket("localhost",Connector);
        Out = new PrintWriter(socket.getOutputStream(), true);
    }
    catch(IOException exception){
        exception.printStackTrace();
    }


    }
    public void send(String Message){
        Out.println(Message);
    }
    public void get(){

    }
    public void read(){

    }
}
