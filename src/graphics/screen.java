import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class screen {
    private screenConfig config;
    private ArrayList<horizontal> hList = new ArrayList<>();
    private int hCount = 5; // Number of rows to create in the terminal
    private String[] textFont; // 3 Fonts Supported
    private String[] screenColor; // 5 Screen Colors Supported
    private int[] textSize; // 3 Text Sizes Supported
    private int[] fuelTypes; // 3-5 Fuel Types

    // Screen graphics
    public screen(Stage screenStage) {
        initializeScreen(screenStage);
    }

    // Change screen configs
    public void setConfig(int display) {
        this.config = new screenConfig(display);
    }

    // Initialize the screen
    private void initializeScreen(Stage screenStage){
        VBox vertical = new VBox(3);
        vertical.setFillWidth(true); // Children stretch to VBox width
        vertical.setMaxWidth(Double.MAX_VALUE); // VBox fills Scene width
        vertical.setMaxHeight(Double.MAX_VALUE); // VBox fills Scene height

        // Create screen layout
        for(int i = 1; i <= hCount; i++){

            horizontal h = new horizontal(i);
            h.bL.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-font-weight: bold;");
            h.bR.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-font-weight: bold;");
            h.tL.setStyle("-fx-text-fill: white;");
            h.tLR.setStyle("-fx-text-fill: white;");
            h.tR.setStyle("-fx-text-fill: white;");

            hList.add(h); // Add horizontal's to a list for access
            VBox.setVgrow(h.h, Priority.ALWAYS); // Make HBox grow vertically
            vertical.getChildren().add(h.h);
        }

        vertical.setStyle("-fx-background-color: #1e3a8a;"); // blue background for entire UI
        Scene scene = new Scene(vertical, 600, 400);
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

            bL = new Button();
            bL.setOnAction(event -> {
                buttonAction(leftID);
            });

            tL = new Label();
            tLR = new Label();
            tR = new Label();

            bR = new Button();
            bR.setOnAction(event -> {
                buttonAction(rightID);
            });

            // Wrap buttons and fields into HBox (In the order: Button, Field, Field, Field, Button)
            HBox.setHgrow(bL, Priority.ALWAYS);
            bL.setMaxWidth(80);
            bL.setMinWidth(80);
            bL.setMaxHeight(80);
            bL.setMinHeight(80);

            HBox.setHgrow(tL, Priority.ALWAYS);
            tL.setMaxWidth(Double.MAX_VALUE);
            tL.setMaxHeight(Double.MAX_VALUE);
            tL.setAlignment(Pos.CENTER_LEFT);
            tL.setWrapText(true);
            HBox.setHgrow(tLR, Priority.ALWAYS);
            tLR.setMaxWidth(Double.MAX_VALUE);
            tLR.setMaxHeight(Double.MAX_VALUE);
            tLR.setAlignment(Pos.CENTER);
            tLR.setWrapText(true);
            HBox.setHgrow(tR, Priority.ALWAYS);
            tR.setMaxWidth(Double.MAX_VALUE);
            tR.setMaxHeight(Double.MAX_VALUE);
            tR.setAlignment(Pos.CENTER_RIGHT);
            tR.setWrapText(true);

            HBox.setHgrow(bR, Priority.ALWAYS);
            bR.setMaxWidth(80);
            bR.setMinWidth(80);
            bR.setMaxHeight(80);
            bR.setMinHeight(80);
            
            h.getChildren().addAll(bL, tL, tLR, tR, bR);
        }

        // Handle UI button navigation (Could pass in a current screen state maybe for future control)
        private void buttonAction(int buttonNum){
            String message = "Button " + buttonNum + " was clicked!";
            System.out.println(message);
            setConfig(buttonNum);
        }
    }

    private class screenConfig {
        public screenConfig(int display){
            blankScreen();
            switch (display) {
                case 1:
                    welcomeScreen();
                    break;

                case 2:
                    chooseFuelScreen();
                    break;
            
                case 3:
                    connectHoseScreen();
                    break;
            
                case 4:
                    fuelingScreen();
                    break;
            
                case 5:
                    transactionCompleteScreen();
                    break;
            
                default:
                    welcomeScreen();
                    break;
            }
        }

        public void welcomeScreen(){
            horizontal row1 = hList.get(0);
            horizontal row2 = hList.get(1);
            combineTextFields(row1.tL, row1.tLR, row1.tR);
            combineTextFields(row2.tL, row2.tLR, row2.tR);

            row1.tLR.setFont(Font.font("Serif", 30));
            row1.tLR.setText("Welcome!");
            makeBold(row1.tLR);

            row2.tLR.setFont(Font.font("Serif", 15));
            row2.tLR.setText("Use the card reader to begin your transaction.");

            horizontal row3 = hList.get(2);
            combineTextFields(row3.tL, row3.tLR, row3.tR);
            row3.tLR.setFont(Font.font("Serif", 50));
            row3.tLR.setText("*");
        }

        public void chooseFuelScreen(){
            horizontal row1 = hList.get(0);
            horizontal row2 = hList.get(1);
            horizontal row3 = hList.get(2);
            horizontal row4 = hList.get(3);
            horizontal row5 = hList.get(4);
            combineTextFields(row1.tL, row1.tLR, row1.tR);

            row1.tLR.setFont(Font.font("Serif", 20));
            row1.tLR.setText("Payment approved. Select fuel type:");

            row2.tL.setFont(Font.font("Serif", 15));
            row2.tL.setText("Unleaded");
            row2.tR.setFont(Font.font("Serif", 15));
            row2.tR.setText("Confirm");

            row3.tL.setFont(Font.font("Serif", 15));
            row3.tL.setText("Premium");

            row4.tL.setFont(Font.font("Serif", 15));
            row4.tL.setText("Premium Plus");

            row5.tL.setFont(Font.font("Serif", 15));
            row5.tL.setText("Gasoline");

            makeItalic(row2.tL);
            makeItalic(row3.tL);
            makeItalic(row4.tL);
            makeItalic(row5.tL);
            makeBold(row1.tLR);
        }

        public void connectHoseScreen(){
            horizontal row1 = hList.get(2);
            combineTextFields(row1.tL, row1.tLR, row1.tR);

            row1.tLR.setFont(Font.font("Serif", 20));
            row1.tLR.setText("Attach the hose to your vehicle's \n       gas tank to begin fueling.");
            makeBold(row1.tLR);

            horizontal row2 = hList.get(3);
            combineTextFields(row2.tL, row2.tLR, row2.tR);
            row2.tLR.setFont(Font.font("Serif", 50));
            row2.tLR.setText("*");
        }

        public void fuelingScreen(){
            horizontal row1 = hList.get(2);
            combineTextFields(row1.tL, row1.tLR, row1.tR);

            row1.tLR.setFont(Font.font("Serif", 20));
            row1.tLR.setText("Fueling in progress...");
            makeBold(row1.tLR);

            horizontal row2 = hList.get(3);
            combineTextFields(row2.tL, row2.tLR, row2.tR);
            row2.tLR.setFont(Font.font("Serif", 50));
            row2.tLR.setText("*");
        }

        public void transactionCompleteScreen(){
            horizontal row1 = hList.get(0);
            horizontal row2 = hList.get(2);
            combineTextFields(row1.tL, row1.tLR, row1.tR);
            combineTextFields(row2.tL, row2.tLR, row2.tR);

            row1.tLR.setFont(Font.font("Serif", 20));
            row1.tLR.setText("Transaction complete.");
            makeBold(row1.tLR);
            row2.tLR.setFont(Font.font("Serif", 20));
            row2.tLR.setText("Thank you!");
            makeItalic(row2.tLR);

            horizontal row3 = hList.get(3);
            combineTextFields(row3.tL, row3.tLR, row3.tR);
            row3.tLR.setFont(Font.font("Serif", 50));
            row3.tLR.setText("*");
        }

        public void blankScreen(){
            for(horizontal row : hList){
                row.tL.setText("");
                row.tLR.setText("");
                row.tR.setText("");
                divideTextFields(row.tL, row.tLR, row.tR);
            }
        }

        // Make textfield Bold
        private void makeBold(Label a){ a.setFont(Font.font(a.getFont().getFamily(), FontWeight.BOLD, a.getFont().getSize())); }

        // Make textfield italic
        private void makeItalic(Label a){ a.setFont(Font.font(a.getFont().getFamily(), FontPosture.ITALIC, a.getFont().getSize())); }

        // Combine 2 left/right text fields into 1 center field
        private void combineTextFields(Label a, Label ab, Label b){
            a.setMaxWidth(0);
            b.setMaxWidth(0);
            ab.setMaxWidth(Double.MAX_VALUE);
            a.setText("");
            b.setText("");
        }

        // Divide 1 center text field into 2 left/right fields
        private void divideTextFields(Label a, Label ab, Label b){
            a.setMaxWidth(Double.MAX_VALUE);
            b.setMaxWidth(Double.MAX_VALUE);
            ab.setMaxWidth(0);
            ab.setText("");
        }
    }
}