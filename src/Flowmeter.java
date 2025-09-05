
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import helpers.imageLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Flowmeter {
    private double gallonsPumped;
    private double pricePerGallon;
    private boolean pumping;
    static PrintWriter out;
    static BufferedReader bf;
    ioPort api;

    public Flowmeter(double pricePerGallon, int deviceType, int connector) throws IOException {
        this.pricePerGallon = pricePerGallon;
        this.gallonsPumped = 0.0;
        this.pumping = false;

        api = ioPort.ChooseDevice(deviceType);
        api.ioport(connector);
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

    public static class FlowmeterGraphics extends Application {
        private Flowmeter meter;

        @Override
        public void start(Stage primaryStage) {
            new Thread(() -> {
                try {
                    Flowmeter fm = new Flowmeter(2.49, 2, 2);
                    this.meter = fm;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Flowmeter-Conn").start();

            imageLoader img = new imageLoader();
            img.loadImages();

            // Show idle reader image
            StackPane root;
            ImageView meterView = new ImageView(img.imageList.get(0));
            meterView.setPreserveRatio(true);
            meterView.setFitWidth(300);
            meterView.setSmooth(true);
            meterView.setPickOnBounds(true);
            meterView.setOnMouseClicked(e -> {
                this.meter.api.send("Flow meter says hello.");
            });

            root = new StackPane(meterView);

            Scene scene = new Scene(root, 300, 200);
            primaryStage.setTitle("Flowmeter");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public static void main(String[] args) throws InterruptedException, Exception {
        Application.launch(Flowmeter.FlowmeterGraphics.class, args);
    }
}

