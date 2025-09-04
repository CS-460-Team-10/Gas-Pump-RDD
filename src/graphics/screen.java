import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class screen {
    ioPort api;

    public screen(int deviceType, int connector) throws UnknownHostException, IOException { 
        api = ioPort.ChooseDevice(deviceType);  
        api.ioport(connector);     
     }
    
    public static class screenGraphics extends Application {
        private screen sc;
        private screenConfig config;
        private ArrayList<horizontal> hList = new ArrayList<>();
        private String[] textFont = { "Serif", "Times New Roman", "Courier New" }; // 3 Fonts Supported
        private Color[] textColor = { Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }; // 5 Text Colors Supported
        private Color[] screenColor = { Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }; // 5 Screen Colors Supported
        private int[] textSize = { 30, 20, 15 }; // 3 Text Sizes Supported
        private int hCount = 5; // Number of rows to create in the terminal

        @Override
        public void start(Stage primaryStage) {
            initializeScreen(primaryStage);

            // Connect to hub
            new Thread(() -> {
                try {
                    this.sc = new screen(1, 4);
                    while (true) {
                        String msg = sc.api.get();
                        if (msg != null) {
                            System.out.println("Screen Receiving: \n" + msg);
                            // Optionally, update UI with Platform.runLater(() -> { /* apply msg to labels */ });
                        }
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Screen-Conn").start();
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
                    sendData(bL, leftID);
                });

                tL = new Label();
                tLR = new Label();
                tR = new Label();

                bR = new Button();
                bR.setOnAction(event -> {
                    sendData(bR, rightID);
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
        }

        // Send data through api
        private void sendData(Button b, int id){
            System.out.println("SCREEN: bp" + id);
            sc.api.send("bp" + id);
        }

        // Class for sets of screen configurations
        private class screenConfig {
            public screenConfig(String msg){
                if(msg != null || msg != ""){
                    interpretMessage(msg);
                }
            }

            // Interpret markup message (Only pass in what is changing)
            private void interpretMessage(String msg){
                String[] tokens = msg.split(":"); // Partition the message
                boolean wasCombined = false;
                for(String token : tokens){
                    String[] settings = token.split("/"); // Partition the field settings
                    // Interpret textfield info
                    if(token.charAt(0)=='t'){
                        boolean combinedField = false;

                        // Determine row number
                        int fieldNum = Character.getNumericValue(settings[0].charAt(1));
                        if(Character.isDigit(settings[0].charAt(2))){ combinedField = true; }
                        if(fieldNum%2==0){ wasCombined = false; } // Reset combine history every even field number
                        int rowNum = fieldNum/2;
                        horizontal row = hList.get(rowNum);

                        // Determine if field is/was combined and set the textfield configuration
                        if(combinedField){ 
                            wasCombined = true;
                            combineTextFields(row.tL, row.tLR, row.tR);
                            fieldConfig(row.tLR, settings); 
                        } 
                        else if(fieldNum%2==0 && !wasCombined){ fieldConfig(row.tL, settings); } 
                        else if (!wasCombined){ fieldConfig(row.tR, settings); }
                    }
                    // Interpret button info
                    else if(token.charAt(0)=='b'){ 
                        // Determine row number
                        int buttonNum = Character.getNumericValue(token.charAt(1));
                        int rowNum = buttonNum/2;
                        horizontal row = hList.get(rowNum);

                        if(buttonNum%2==0){ buttonConfig(row.bL, settings); } 
                        else{ buttonConfig(row.bR, settings); }
                    }
                }
            }

            // Configure text field
            private void fieldConfig(Label t, String[] tokens){
                int size = Character.getNumericValue(tokens[1].charAt(1))-1;
                char style = tokens[1].charAt(2);
                int font = Character.getNumericValue(tokens[2].charAt(1))-1;
                int color = Character.getNumericValue(tokens[3].charAt(1))-1;
                String displayText = tokens[4].replace("\"", "");

                t.setTextFill(textColor[color]);
                t.setFont(Font.font(textFont[font], textSize[size]));
                if(style == 'B'){makeBold(t);}
                else if(style == 'I') {makeItalic(t);}
                t.setText(displayText);
            }

            // Configure button
            private void buttonConfig(Button b, String[] tokens){
                boolean active = tokens[1].substring(1).equals("1");
                b.setDisable(!active);
            }

            /*
            public void welcomeScreen(){                // Format t01/s1/f1/c4/"Welcome!":t23/s3/f1/c4/"Use the card reader to begin your transaction.":t45/s1/f1/c4/"*"
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
            } */

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

    public static void main(String[] args) { Application.launch(screen.screenGraphics.class, args); }
}