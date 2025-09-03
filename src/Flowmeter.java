package src;

import java.io.IOException;

public class Flowmeter {
    private ioPortAPI ioPortAPI;
    private double gallonsPumped;
    private double pricePerGallon;
    private boolean pumping;

    public Flowmeter(ioPortAPI ioPort, double pricePerGallon) {
        this.ioPortAPI = ioPort;
        this.pricePerGallon = pricePerGallon;
        this.gallonsPumped = 0.0;
        this.pumping = false;
    }
    // Starts pumping fuel
    public void startPumping() {
        pumping = true;
        ioPortAPI.send("Pumping started. Price per gallon: $" + pricePerGallon);
    }
    // stops pumping fuel
    public void stopPumping() {
        pumping = false;
        ioPortAPI.send("Pump stopped. Total gallons: " + gallonsPumped
        + ", Cost: $" + getTotalCost());
    }

    /**
     * This code simulates fuel flow increasing gallons pumped.
     * in a real station.
     * @param gallons the amount of gallons being pumping.
     */
    public void flow(double gallons) {
        if(pumping) {
            gallonsPumped += gallons;
            ioPortAPI.send("Flow update:" + gallonsPumped + " gallons pumped.");
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
        return ioPortAPI.get();
    }
}
