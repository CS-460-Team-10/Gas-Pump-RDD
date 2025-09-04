import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/* 
 * Looks pretty good. The unknownhost error might an issue with ioPort. Hopefully will fix it by fixing ioPort.
 * 
 * -Alex
*/

public class CreditCardReader {
    public static Boolean readCard(String cardNumber) {
        if (cardNumber != null && !cardNumber.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }




 public static void main(String[] args) throws IOException, InterruptedException{
        int Port = 333;
        System.out.println("Listening");
        ServerSocket serverSocket = new ServerSocket(Port);
        Socket srSocket = serverSocket.accept();
        while(true){

        
        BufferedReader bf = new BufferedReader(new InputStreamReader(srSocket.getInputStream()));
        PrintWriter out = new PrintWriter(srSocket.getOutputStream(), true);

        String Card;
        while((Card = bf.readLine()) != null){
            boolean Authorized = readCard(Card);
            if (Authorized == true){
                out.println(Card  + " Authorized.");

            } else{
                out.println(Card  + " Unauthorized.");
            }
        }

    }
    }
}