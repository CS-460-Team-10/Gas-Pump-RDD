
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Flowmeter {
    private double gallonsPumped;
    private double pricePerGallon;
    private boolean pumping;
    static PrintWriter out;
    static BufferedReader bf;
    static Socket socket;
    ioPort api;

    public Flowmeter(double pricePerGallon, int connector) throws IOException {
        this.pricePerGallon = pricePerGallon;
        this.gallonsPumped = 0.0;
        this.pumping = false;

        api = ioPort.ChooseDevice(connector);
        api.defineAsServer(connector);
        System.out.println("Flowmeter listening on connector " + connector);

    }
    // Starts pumping fuel
    public void startPumping() {
        pumping = true;
        api.send("Pumping started. Price per gallon: $" + pricePerGallon);
    }
    // stops pumping fuel
    public void stopPumping() {
        pumping = false;
        api.send("Pump stopped. Total gallons: " + gallonsPumped
        + ", Cost: $" + getTotalCost());
    }




    /**
     * This code simulates fuel flow increasing gallons pumped.
     * in a real station.
     * @param gallons the amount of gallons being pumped.
     */
    public void flow(double gallons) {
        if(pumping) {
            gallonsPumped += gallons;
            api.send("Flow update:" + gallonsPumped + " gallons pumped.");
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
        return api.get();
    }



public static void main(String[] args) throws IOException, InterruptedException{
        Flowmeter fm = new Flowmeter(0, 2);
        while (true) {
            String msg = fm.checkMessage();
            if (msg != null) {
                fm.api.send("FROM FLOWMETER, CALUCLATION -> SERVER -> DONE!");
            }
        }
    }
}

