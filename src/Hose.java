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

    /**
     * Constructor to initialize the hose device.
     * @param deviceType Type of the device to choose
     * @param connector Connector/port number to connect the device
     * @throws IOException if the device initialization fails
     */
    public Hose(int deviceType, int connector) throws IOException {
        this.attached = false;
        this.api = ioPort.ChooseDevice(deviceType);
        api.ioport(connector);
        System.out.println("Hose is up: " + connector);

    }

    /**
     * Updates the hose sensor status and sends messages to the API.
     * @param sensorAttached boolean from the sensor that indicates if the hose is attached.
     * @param sensorTankFull boolean from the sensor that indicates if the tank is full
     */
    public void updateSenor(boolean sensorAttached, boolean sensorTankFull) {

        // checks if the hose is attached
        if (sensorAttached && !attached) {
            attached = true;
            api.send("Hose attached");
        } else if (!sensorAttached && attached) {
            attached = false;
            api.send("Hose detached");
        }

        // checks the status of the tank
        if (sensorTankFull && !tankFull) {
            tankFull = true;
            api.send("Tank Full");
        } else if (!sensorTankFull && tankFull) {
            tankFull = false;
            api.send("Tank is not full");
        }
    }

    /**
     * Inner class for the GUI representation of the hose.
     */
    public static class HoseGraphics extends Application {
        private Hose hose;

        @Override
        public void start(Stage primaryStage) {

            // Initialize the hose hardware on a background thread
            new Thread(() -> {
                try {
                    Hose h = new Hose(1, 4);
                    this.hose = h;
                    System.out.println("Hose connected (client on 4)");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Hose-Conn").start();

            // Load images for the GUI representation
            imageLoader img = new imageLoader();
            img.loadImages();

            // Show idle reader image
            ImageView hoseView = new ImageView(img.imageList.get(1));
            hoseView.setPreserveRatio(true);
            hoseView.setFitWidth(300);
            hoseView.setSmooth(true);
            hoseView.setPickOnBounds(true);

            // Simple toggle to simulate attaching/detaching the hose
            final boolean[] toggled = {false}; // Clean this later XXXXXXXXXXXXXX - works for now
            hoseView.setOnMouseClicked(e -> {

                // Switch image based on toggle
                if (toggled[0]) {
                    hoseView.setImage(img.imageList.get(1));
                } else {
                    hoseView.setImage(img.imageList.get(4));
                }
                toggled[0] = !toggled[0];

                // Update hose sensor status
                if (this.hose != null) {
                    this.hose.updateSenor(toggled[0], false);
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