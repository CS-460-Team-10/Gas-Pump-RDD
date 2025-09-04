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
    ioPort api;

    public CreditCardReader(int connector) throws Exception {
        api = ioPort.ChooseDevice(connector);  
        api.defineAsServer(connector);         
        while (true) {
            String msg = api.get();
            if (msg != null) {
                boolean authorized = readCard(msg);
                if (authorized) {
                    api.send(msg + " Authorized.");
                } else {
                    api.send(msg + " Unauthorized.");
                }
            }
        }
    }

    private static boolean readCard(String cardNumber) {
        return cardNumber != null && !cardNumber.isEmpty();
    }

    public static void main(String[] args) throws Exception {
        new CreditCardReader(1);
    }
}
