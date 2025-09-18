import java.io.IOException;
import helpers.imageLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Hose {
    private boolean connected;
    private final ioPort api;
    private boolean tankFull;
    private double fuelLevel;
    private double maxFuel;

    /**
     * Constructor to initialize the hose device.
     * @param deviceType Type of the device to choose
     * @param connector Connector/port number to connect the device
     * @throws IOException if the device initialization fails
     */
    public Hose(int deviceType, int connector) throws IOException {
        this.connected = false;
        this.tankFull = false;
        this.maxFuel = 12.0;
        this.fuelLevel = Math.random()*6.0;
        System.out.println("Max Fuel Capacity: " + this.maxFuel);
        System.out.println("Current Fuel Level: " + this.fuelLevel);
        this.api = ioPort.ChooseDevice(deviceType);
        api.ioport(connector);
        System.out.println("Hose is up: " + connector);

    }

    /**
     * Updates the hose sensor status and sends messages to the API.
     * @param sensorConnected boolean from the sensor that indicates if the hose is connected.
     * @param sensorTankFull boolean from the sensor that indicates if the tank is full
     */
    public void updateSenor(boolean sensorConnected, boolean sensorTankFull) {

        // checks if the hose is connected
        if (sensorConnected && !connected) {
            connected = true;
            System.out.println("Hose Connected");
            api.send("connected:true");
        } else if (!sensorConnected && connected) {
            connected = false;
            System.out.println("Hose Disconnected");
            api.send("connected:false");
        }

        // checks the status of the tank
        if (sensorTankFull && !tankFull) {
            tankFull = true;
            System.out.println("Tank Full");
            api.send("tank-full:true");
        } else if (!sensorTankFull && tankFull) {
            tankFull = false;
            api.send("tank-full:false");
        }
    }

    /**
     * Inner class for the GUI representation of the hose.
     */
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

            // Set up GUI layout
            StackPane root = new StackPane(hoseView);
            Scene scene = new Scene(root, 300, 200);

            primaryStage.setTitle("Hose");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Main method to launch JavaFX app.
     */
    public static void main(String[] args) throws InterruptedException, Exception {
        Application.launch(Hose.HoseGraphics.class, args);
    }
}
