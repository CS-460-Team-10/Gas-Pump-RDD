import java.io.IOException;

import helpers.imageLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Hose {
    private boolean attached;
    private final ioPort api;
    private boolean tankFull;
    private double fuelLevel;
    private double maxFuel;

    /**
     * Hose has a boolean just stating that It's attached or not
     * @param connector is just the port #
     * @throws IOException 
     */
    public Hose(int devieType, int connector) throws IOException {
        this.attached = false;
        this.tankFull = false;
        this.maxFuel = 12.0;
        this.fuelLevel = Math.random()*6.0;
        System.out.println("Max Fuel Capacity: " + this.maxFuel);
        System.out.println("Current Fuel Level: " + this.fuelLevel);
        this.api = ioPort.ChooseDevice(devieType);
        api.ioport(connector);
        System.out.println("Hose is up: " + connector);

    }

    /**
     * updateSenor just send a message to the API whether the sensor
     * is attached or not to the gas tank.
     * @param sensorAttached is boolean sent by the sensor telling the
     *                       status of the hose.
     */
    public void updateSenor(boolean sensorAttached, boolean sensorTankFull) {
        // checks if the hose is attached
        if (sensorAttached && !attached) {
            attached = true;
            System.out.println("Hose attached");
            api.send("Hose attached");
        } else if (!sensorAttached && attached) {
            attached = false;
            System.out.println("Hose detached");
            api.send("Hose detached");
        }
        // checks if the tank is full
        if (sensorTankFull && !tankFull) {
            tankFull = true;
            System.out.println("Tank Full");
            api.send("Tank Full");
        } else if (!sensorTankFull && tankFull) {
            tankFull = false;
            api.send("Tank is not full");
        }
    }

    public boolean isAttached() {
        return attached;
    }

    public boolean isTankFull() {
        return tankFull;
    }

    public static class HoseGraphics extends Application {
        private Hose hose;

        @Override
        public void start(Stage primaryStage) {

            imageLoader img = new imageLoader();
            img.loadImages();

            // Show idle reader image
            ImageView hoseView = new ImageView(img.imageList.get(5));
            hoseView.setPreserveRatio(true);
            hoseView.setFitWidth(300);
            hoseView.setSmooth(true);
            hoseView.setPickOnBounds(true);

            new Thread(() -> {
                try {
                    hose = new Hose(3, 4);

                    while (true) {
                        String msg = hose.api.get();

                        if (msg != null && !msg.isEmpty()) {
                            if(msg.contains("Gal Pumped:")){
                                String[] tokens = msg.split(":");
                                if (tokens.length > 1) {
                                    try {
                                        double fuelBought = Double.parseDouble(tokens[3].trim());
                                        hose.fuelLevel += fuelBought;
                                        System.out.println("Fuel Level updated: " + String.format("%.2f", hose.fuelLevel) + " gallons");
                                    } catch (NumberFormatException e) {
                                        System.err.println("Error parsing fuel amount from message: " + msg);
                                    }
                                }
                            }
                        }

                        if(hose.fuelLevel >= hose.maxFuel){ // Tank full
                            hose.updateSenor(true, true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Hose-Conn").start();

            final boolean[] toggled = {false};
            hoseView.setOnMouseClicked(e -> {
                toggled[0] = !toggled[0];
                if (toggled[0]) {
                    hoseView.setImage(img.imageList.get(6));
                    hose.updateSenor(true, hose.tankFull);
                } else {
                    hoseView.setImage(img.imageList.get(5));
                    hose.updateSenor(false, hose.tankFull);
                }
            });

            StackPane root = new StackPane(hoseView);

            Scene scene = new Scene(root, 300, 200);
            primaryStage.setTitle("Hose");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public static void main(String[] args) throws InterruptedException, Exception {
        Application.launch(Hose.HoseGraphics.class, args);
    }
}
