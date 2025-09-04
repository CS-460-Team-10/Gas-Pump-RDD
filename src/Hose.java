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
     * Hose has a boolean just stating that It's attached or not
     * @param connector is just the port #
     * @throws IOException 
     */
    public Hose(int devieType, int connector) throws IOException {
        this.attached = false;
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
            api.send("Hose attached");
        } else if (!sensorAttached && attached) {
            attached = false;
            api.send("Hose detached");
        }
        // checks if the tank is full
        if (sensorTankFull && !tankFull) {
            tankFull = true;
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
            new Thread(() -> {
                try {
                    Hose h = new Hose(1, 4);
                    this.hose = h;
                    System.out.println("Hose connected (client on 4)");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Hose-Conn").start();

            imageLoader img = new imageLoader();
            img.loadImages();

            // Show idle reader image
            ImageView hoseView = new ImageView(img.imageList.get(1));
            hoseView.setPreserveRatio(true);
            hoseView.setFitWidth(300);
            hoseView.setSmooth(true);
            hoseView.setPickOnBounds(true);

            final boolean[] toggled = {false}; // Clean this later XXXXXXXXXXXXXX - works for now
            hoseView.setOnMouseClicked(e -> {
                if (toggled[0]) {
                    hoseView.setImage(img.imageList.get(1));
                } else {
                    hoseView.setImage(img.imageList.get(4));
                }
                toggled[0] = !toggled[0];
                if (this.hose != null) {
                    this.hose.updateSenor(toggled[0], false);
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