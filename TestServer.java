import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class TestServer {
    public static void main(String[] args) throws IOException{
        System.out.println("Listening");
        ServerSocket serverSocket = new ServerSocket(333);
        Socket srSocket = serverSocket.accept();

        BufferedReader bf = new BufferedReader(new InputStreamReader(srSocket.getInputStream()));
        System.out.println("Recieved from MCU: " + bf.readLine());
        
    
    }
}
