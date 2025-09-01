import javafx.application.Application;
import javafx.stage.Stage;

public class main extends Application {

    public static void main(String[] args) { 
        launch(args); 
    }

    // Create Graphics
    @Override
    public void start(Stage primaryStage) {
        Stage secondaryStage = new Stage();

        //// Create screen graphics
        //new screen(primaryStage);
        // Create system graphics
        dynamicSystem gasPumpSystem = new dynamicSystem(secondaryStage);

        // TEMORARY SEND METHOD WILL BE REPLACED WITH SOCKET API LATER XXXXXX
        new screen(primaryStage, buttonId -> {
            gasPumpSystem.showImage(buttonId);
        });
        // TEMORARY SEND METHOD WILL BE REPLACED WITH SOCKET API LATER XXXXXX
    }
}