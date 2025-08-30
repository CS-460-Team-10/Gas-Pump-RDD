import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Stream;

public class dynamicSystem {
    private final ImageView imageView = new ImageView();
    private final ArrayList<Image> imageList = new ArrayList<>(); 
    private final String IMAGE_PATH = "res/";

    // Create gas pump system graphics
    public dynamicSystem(Stage systemStage) {
        loadImages();

        // Fill the whole window
        imageView.setPreserveRatio(false);

        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 900, 600);
        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());
        showImage(0);

        systemStage.setScene(scene);
        systemStage.setTitle("Gas Pump System");
        systemStage.show();
    }

    // Display loaded image at index
    public void showImage(int i) { imageView.setImage(imageList.get(i)); }

    // Load image resources to imageList array
    private void loadImages(){
        try (Stream<Path> s = Files.walk(Paths.get(IMAGE_PATH))) {

            // File filtering
            Stream<Path> images = s.filter((Path p) -> {
                String name = p.getFileName().toString().toLowerCase(Locale.ROOT);
                return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
            });

            // Add all image files to the array list
            images.forEach(i -> {
                String uri = i.toUri().toString();
                Image img = new Image(uri, true);
                imageList.add(img);
            });

        } catch (Exception e) { e.printStackTrace(); }
    }
}
