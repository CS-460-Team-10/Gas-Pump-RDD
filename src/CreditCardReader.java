import helpers.imageLoader;
import javafx.application.Application;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CreditCardReader {

    // ioPort object to communicate with the card reader hardware
    private final ioPort api;

    /**
     * Constructor to initialize the credit card reader hardware.
     * @param deviceType Type of the device to choose.
     * @param connector Connector/port number that connects the device.
     * @throws Exception if the device initialization fails.
     */
    public CreditCardReader(int deviceType, int connector) throws Exception {
        api = ioPort.ChooseDevice(deviceType);  
        api.ioport(connector);
    }

    /**
     * Reads a card number and sends a message either stating
     * authorized or unauthorized.
     * @param cardNumber the card number to check.
     */
    private void readCard(String cardNumber) {
        boolean authorized = cardNumber != null && !cardNumber.isEmpty();
        if (authorized) {
            api.send("Authorized.");
        } else {
            api.send("Unauthorized.");
        }
    }

    /**
     * Inner class for the GUI representation of the card reader device.
     */
    public static class CardReaderGraphics extends Application {
        private CreditCardReader cardReader;

        @Override
        public void start(Stage primaryStage) {

            // Starts a background thread to initialize the card reader hardware.
            new Thread(() -> {
                try {
                    CreditCardReader c = new CreditCardReader(1, 3);
                    this.cardReader = c;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "CardReader-Conn").start();

            // Load images for the GUI
            imageLoader img = new imageLoader();
            img.loadImages();

            // Show idle reader image
            ImageView reader = new ImageView(img.imageList.get(3));
            reader.setPreserveRatio(true);
            reader.setFitWidth(300);
            reader.setSmooth(true);
            reader.setPickOnBounds(true);

            // Handle card tap events by clicking on the reader image
            reader.setOnMouseClicked(e -> {
                reader.setImage(img.imageList.get(2));

                PauseTransition first = new PauseTransition(Duration.millis(1000));
                PauseTransition second = new PauseTransition(Duration.millis(2000));

                first.setOnFinished(ev -> {
                    reader.setImage(img.imageList.get(0));
                    second.play();
                });

                second.setOnFinished(ev -> {
                    reader.setImage(img.imageList.get(3));
                });

                first.play();

                // Simulates reading a card
                this.cardReader.readCard("Card Tapped!");
            });

            // Set up for the main GUI layout
            StackPane root = new StackPane(reader);
            Scene scene = new Scene(root, 300, 200);

            primaryStage.setTitle("Credit Card Reader");
            primaryStage.setScene(scene);
            primaryStage.show();

        }
    }

    /**
     * Main method to launch the JavaFX app.
     */
    public static void main(String[] args) throws Exception {
        Application.launch(CreditCardReader.CardReaderGraphics.class, args);
    }
}
