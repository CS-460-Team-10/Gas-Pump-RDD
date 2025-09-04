<<<<<<< HEAD

import java.io.BufferedReader;
=======
>>>>>>> 2b77d42b72aef5aae6ccbcd68b2f520bcc385b4a
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Flowmeter {
<<<<<<< HEAD
=======
    private ioPort ioPortAPI;
>>>>>>> 2b77d42b72aef5aae6ccbcd68b2f520bcc385b4a
    private double gallonsPumped;
    private double pricePerGallon;
    private boolean pumping;
    static PrintWriter out;
    static BufferedReader bf;
    static Socket socket;

<<<<<<< HEAD
    public Flowmeter(double pricePerGallon) {
=======
    public Flowmeter(ioPort ioPort, double pricePerGallon) {
        this.ioPortAPI = ioPort;
>>>>>>> 2b77d42b72aef5aae6ccbcd68b2f520bcc385b4a
        this.pricePerGallon = pricePerGallon;
        this.gallonsPumped = 0.0;
        this.pumping = false;
    }
    // Starts pumping fuel
    public void startPumping() {
        pumping = true;
        send("Pumping started. Price per gallon: $" + pricePerGallon);
    }
    // stops pumping fuel
    public void stopPumping() {
        pumping = false;
        send("Pump stopped. Total gallons: " + gallonsPumped
        + ", Cost: $" + getTotalCost());
    }

    public void send(String message){
        out.println(message);
    }



    /**
     * This code simulates fuel flow increasing gallons pumped.
     * in a real station.
     * @param gallons the amount of gallons being pumped.
     */
    public void flow(double gallons) {
        if(pumping) {
            gallonsPumped += gallons;
            send("Flow update:" + gallonsPumped + " gallons pumped.");
        }
    }
    // gets total gallons being pumped
    public double getGallonsPumped() {
        return gallonsPumped;
    }
    // get total cost
    public double getTotalCost() {
        return gallonsPumped * pricePerGallon;
    }
    // check message from the ioPort (e.g. control signals).
    public String checkMessage() throws IOException {
        return bf.readLine();
    }



public static void main(String[] args) throws IOException, InterruptedException{
        int Port = 555;
        System.out.println("Listening");
        ServerSocket serverSocket = new ServerSocket(Port);

        while(true){
        Socket srSocket = serverSocket.accept();

        bf = new BufferedReader(new InputStreamReader(srSocket.getInputStream()));
        out = new PrintWriter(srSocket.getOutputStream(), true);

        String msg;
        while((msg = bf.readLine()) != null){
                System.out.println(msg);
                out.println("This is the FLowmeter, Volume caluclation -> Done -> Bye!");

        }
        bf.close();
        out.close();
        srSocket.close();

    }
} 
}

