import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
        @Override
        public void start(Stage primaryStage) {
            Label label = new Label("Hose Ready");
            StackPane root = new StackPane(label);

            Scene scene = new Scene(root, 300, 200);
            primaryStage.setTitle("Hose");
            primaryStage.setScene(scene);
            primaryStage.show();

            new Thread(() -> {
                try {
                    Hose hose = new Hose(1, 1);
                    while (true) {
                        hose.updateSenor(true, false);
                        Thread.sleep(2000);
                        hose.updateSenor(false, true);
                        Thread.sleep(2000);
                        hose.updateSenor(false, false);
                        Thread.sleep(2000);
                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Hose-Conn").start();
        }
    }

    public static void main(String[] args) throws InterruptedException, Exception {
        Application.launch(Hose.HoseGraphics.class, args);
    }
}