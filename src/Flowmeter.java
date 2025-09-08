
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.scene.control.Label;
import helpers.imageLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
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
            this.gallonsPumped += gallons;
            api.send("Flow update:" + gallonsPumped + " gallons pumped.");
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

    public static class FlowmeterGraphics extends Application {

        @Override
        public void start(Stage primaryStage) {
            imageLoader img = new imageLoader();
            img.loadImages();

            // Show idle reader image
            StackPane root;
            ImageView meterView = new ImageView(img.imageList.get(3));
            meterView.setPreserveRatio(true);
            meterView.setFitWidth(300);
            meterView.setSmooth(true);
            meterView.setPickOnBounds(true);

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

            new Thread(() -> {
                try {
                    Flowmeter meter = new Flowmeter(2.49, 2, 2);
                    boolean isOn = false;

                    while (true) {
                        String msg = meter.checkMessage();

                        if (msg != null && !msg.isEmpty()) {

                            if (msg.contains("FM1") && !isOn) {
                                isOn = true;
                                System.out.println("Meter is turning ON.");
                                Platform.runLater(() -> {
                                    meterView.setImage(img.imageList.get(4));
                                    fuelCostLabel.setText("00.00-Gal");
                                });
                            } else if (msg.contains("FM0") && isOn) {
                                isOn = false;
                                System.out.println("Meter is turning OFF.");
                                meter.stopPumping();
                                Platform.runLater(() -> {
                                    meterView.setImage(img.imageList.get(3));
                                    fuelCostLabel.setText("");
                                });
                            }
                        }

                        // If ON, simulate flow and update label
                        if (isOn) {
                            meter.startPumping();
                            meter.flow(0.16);
                            String gal = String.format("%.2f-Gal", meter.getGallonsPumped());
                            Platform.runLater(() -> fuelCostLabel.setText(gal));
                        }

                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Flowmeter-Conn").start();
        }
    }

    public static void main(String[] args) throws InterruptedException, Exception {
        Application.launch(Flowmeter.FlowmeterGraphics.class, args);
    }
}

