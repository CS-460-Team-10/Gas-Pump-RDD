
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.util.Duration;

import javafx.scene.control.Label;
import helpers.imageLoader;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Flowmeter {
    private final double FLOW_RATE = 0.44; // Gal/s
    private double gallonsPumped;
    private double pricePerGallon;
    private boolean pumping;
    private String fuelType;
    static PrintWriter out;
    static BufferedReader bf;
    ioPort api;

    public Flowmeter(double pricePerGallon, int deviceType, int connector, String fuelType) throws IOException {
        this.pricePerGallon = pricePerGallon;
        this.gallonsPumped = 0.0;
        this.pumping = false;
        this.fuelType = fuelType;

        api = ioPort.ChooseDevice(deviceType);
        api.ioport(connector);
    }

    // Starts pumping fuel
    public void startPumping() {
        pumping = true;
        // api.send(String.format("Pumping started. Price per gallon: $%.2f", pricePerGallon));
        api.send("pump-on");
        api.send(String.format("gas-to-pump:%s", fuelType));
    }
    // Stops pumping fuel
    public void stopPumping() {
        pumping = false;
        // api.send(String.format("Pump stopped. Total gallons: %.2f" + ", Cost: $%.2f", gallonsPumped, getTotalCost()));
        api.send("pump-off");
        api.send(String.format("transaction: gallons = %.2f cost = %.2f",
                gallonsPumped, getGallonsPumped()));
    }
    // reset volume counter
    public void reset() {
        gallonsPumped = 0.0;
        api.send("reset");
    }

    /**
     * This code simulates fuel flow increasing gallons pumped.
     * in a real station.
     * @param gallons the amount of gallons being pumped.
     */
    public void flow(double gallons) {
        if(pumping) {
            this.gallonsPumped += gallons;
            api.send(String.format("volume: %.2f " + "gallons pumped.", gallonsPumped));
        }
    }
    // gets total gallons being pumped
    public double getGallonsPumped() {
        return this.gallonsPumped;
    }
    // get total cost
    public double getTotalCost() {
        return this.gallonsPumped * pricePerGallon;
    }
    // check message from the ioPort (e.g. control signals).
    public String checkMessage() throws IOException {
        return api.get();
    }

    /**
     * Inner class for the GUI representation of the hose.
     */
    public static class FlowmeterGraphics extends Application {
        private Flowmeter meter;

        @Override
        public void start(Stage primaryStage) {
            imageLoader img = new imageLoader();
            img.loadImages();

            // Show idle meter off image
            StackPane root;
            ImageView meterView = new ImageView(img.imageList.get(3));
            meterView.setPreserveRatio(true);
            meterView.setFitWidth(300);
            meterView.setSmooth(true);
            meterView.setPickOnBounds(true);

            // Fuel flow label
            Label fuelCostLabel = new Label();
            fuelCostLabel.setStyle(
                "-fx-background-color: rgba(0,0,0,0.6);" +
                "-fx-text-fill: green;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;"
            );
            

            root = new StackPane(meterView, fuelCostLabel);
            StackPane.setAlignment(fuelCostLabel, Pos.CENTER);
            fuelCostLabel.setTranslateY(-48);

            Scene scene = new Scene(root, 300, 200);
            primaryStage.setTitle("Flowmeter");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Process connections
            new Thread(() -> {
                try {
                    meter = new Flowmeter(2.49, 3, 2, "Regular");
                    boolean isOn = false;

                    while (true) {
                        String msg = meter.checkMessage();

                        if (msg != null && !msg.isEmpty()) {

                            // Turn on/off meter
                            if (msg.contains("FM1") && !isOn) {
                                isOn = true;
                                System.out.println("Meter turning ON");
                                meter.startPumping();
                                Platform.runLater(() -> {
                                    meterView.setImage(img.imageList.get(4));
                                    fuelCostLabel.setText("00.00-Gal");
                                });
                            } else if (msg.contains("FM0") && isOn) {
                                isOn = false;
                                System.out.println("Meter turning OFF");
                                meter.stopPumping();
                                meter.gallonsPumped = 0.0; // reset between sessions

                                Platform.runLater(() -> {
                                    meterView.setImage(img.imageList.get(3));
                                    PauseTransition p = new PauseTransition(Duration.millis(5000));
                                    p.setOnFinished(ev -> fuelCostLabel.setText(""));
                                    p.play();
                                });
                            }
                        }

                        // Simulate flow and update label if ON
                        if (isOn) {
                            meter.flow(meter.FLOW_RATE);
                            double g = meter.getGallonsPumped();
                            String gal = String.format("%.2f-Gal", g);
                            Platform.runLater(() -> fuelCostLabel.setText(gal));
                            meter.api.send("Gal Pumped: " + String.format("%.2f", g));
                            Thread.sleep(1000); // Pause per second to flow x-gal/sec
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Flowmeter-Conn").start();
        }
    }

    /**
     * Main method to launch JavaFX app.
     */
    public static void main(String[] args) throws InterruptedException, Exception {
        Application.launch(Flowmeter.FlowmeterGraphics.class, args);
    }
}

