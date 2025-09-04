import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;



/* 
 * Now test with a device Ricky made instead.
 * 
 * -Alex
*/

public class TestServer {

    public static void main(String[] args) throws IOException, InterruptedException{
        int Port = 333;
        System.out.println("Listening");
        ServerSocket serverSocket = new ServerSocket(Port);
        Socket srSocket = serverSocket.accept();

        BufferedReader bf = new BufferedReader(new InputStreamReader(srSocket.getInputStream()));
        PrintWriter out = new PrintWriter(srSocket.getOutputStream(), true);


        String msg;
        while((msg = bf.readLine()) != null){
        System.out.println("Recieved from MCU: " +  msg);
        out.println("Authorized 2 from Port: " + Port);
        }

    }
}
