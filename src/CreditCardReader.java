import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CreditCardReader {
    ioPort api;

    public CreditCardReader(int deviceType, int connector) throws Exception {
        api = new StatusPort();  
        api.ioport(connector);      
        
        while (true) {
            String msg = api.get();
            if (msg != null) {
                boolean authorized = readCard(msg);
                if (authorized) {
                    api.send(msg + " Authorized.");
                } else {
                    api.send(msg + " Unauthorized.");
                }
            }
        }
    }

    private static boolean readCard(String cardNumber) {
        return cardNumber != null && !cardNumber.isEmpty();
    }

    public static class CardReaderGraphics extends Application {
        @Override
        public void start(Stage primaryStage) {
            Label label = new Label("Credit Card Reader Ready");
            StackPane root = new StackPane(label);

            Scene scene = new Scene(root, 300, 200);
            primaryStage.setTitle("Credit Card Reader");
            primaryStage.setScene(scene);
            primaryStage.show();

            new Thread(() -> {
                try {
                    new CreditCardReader(1, 1); // runs the connection
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "CardReader-Conn").start();
        }
    }

    public static void main(String[] args) throws Exception {
        Application.launch(CreditCardReader.CardReaderGraphics.class, args);
    }
}
