import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class screen {
    private ArrayList<horizontal> hList = new ArrayList<>();
    private int hCount = 5; // Number of rows to create in the terminal
    Consumer<Integer> tempTillSockets; // (Delete consumer variable once sockets implemented) XXXX

    // Setup UI screen graphics (Delete consumer variable once sockets implemented) XXXX
    public screen(Stage screenStage, Consumer<Integer> onButton) {
        tempTillSockets = onButton; // (Delete consumer variable once sockets implemented XXXX)
        VBox vertical = new VBox(3);
        vertical.setFillWidth(true); // Children stretch to VBox width
        vertical.setMaxWidth(Double.MAX_VALUE); // VBox fills Scene width
        vertical.setMaxHeight(Double.MAX_VALUE); // VBox fills Scene height

        // Create screen layout
        for(int i = 1; i <= hCount; i++){

            horizontal h = new horizontal(i);
            h.bL.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-font-weight: bold;");
            h.bR.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-font-weight: bold;");
            h.tL.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            h.tLR.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            h.tR.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            hList.add(h); // Add horizontal's to a list for access
            VBox.setVgrow(h.h, Priority.ALWAYS); // Make HBox grow vertically
            vertical.getChildren().add(h.h);
        }

        vertical.setStyle("-fx-background-color: #1e3a8a;"); // blue background for entire UI
        Scene scene = new Scene(vertical, 500, 400);
        screenStage.setTitle("Gas Pump Terminal");
        screenStage.setScene(scene);
        screenStage.show();
    }

    // Horizontal subclass consisting of L/R buttons, fields, and the combined text field
    private class horizontal {
        HBox h;
        Button bL;
        Label tL;
        Label tLR;
        Label tR;
        Button bR;

        // Initializes all horizontal entities in a single line (buttons & fields)
        private horizontal(int count) {
            int leftID = (count-1)*2;
            int rightID = leftID+1;
            
            h = new HBox(10); // HBox to wrap everything into

            bL = new Button("B" + leftID);
            bL.setOnAction(event -> {
                buttonAction(leftID);
            });

            tL = new Label("T" + leftID);
            tLR = new Label(" T" + leftID + rightID + " ");
            tR = new Label("T" + rightID);

            bR = new Button("B" + rightID);
            bR.setOnAction(event -> {
                buttonAction(rightID);
            });

            // Wrap buttons and fields into HBox (In the order: Button, Field, Field, Field, Button)
            HBox.setHgrow(bL, Priority.ALWAYS);
            bL.setMaxWidth(Double.MAX_VALUE);
            bL.setMaxHeight(Double.MAX_VALUE);

            HBox.setHgrow(tL, Priority.ALWAYS);
            tL.setMaxWidth(Double.MAX_VALUE);
            tL.setAlignment(Pos.CENTER);
            HBox.setHgrow(tLR, Priority.ALWAYS);
            tLR.setMaxWidth(Double.MAX_VALUE);
            tLR.setAlignment(Pos.CENTER);
            HBox.setHgrow(tR, Priority.ALWAYS);
            tR.setMaxWidth(Double.MAX_VALUE);
            tR.setAlignment(Pos.CENTER);

            HBox.setHgrow(bR, Priority.ALWAYS);
            bR.setMaxWidth(Double.MAX_VALUE);
            bR.setMaxHeight(Double.MAX_VALUE);
            
            h.getChildren().addAll(bL, tL, tLR, tR, bR);
        }

        // Handle UI button navigation (Could pass in a current screen state maybe for future control)
        private void buttonAction(int buttonNum){
            String message = "Button " + buttonNum + " was clicked!";
            System.out.println(message);
            sendData(buttonNum);
        }
    }

    // TEMORARY SEND METHOD WILL BE REPLACED WITH SOCKET API LATER XXXXXX
    private void sendData(int data){
        if (tempTillSockets != null) tempTillSockets.accept(data);  // notify main
    }

}