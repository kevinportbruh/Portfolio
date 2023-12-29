package reminder;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.scene.image.Image;


/*
 * This class will start with ./gradlew run
 * This class will load the fxml file and start the application
 * This was first the Hello World GUI demo from the JavaFX tutorial and I modified it to fit our needs.
 * It should work all good.
 * 
 * 
 * @author Kevin Portillo self-proclaimed coding wizard 🧙
 */
public class HelloFX extends Application {

   
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
    
        // Use ClassLoader to load the FXML file with a relative path
        URL fxmlUrl = getClass().getClassLoader().getResource("fxml/HomeScene.fxml");
        loader.setLocation(fxmlUrl);
    
      
    
        try {
            Parent root = loader.load();
            root.getStylesheets().add("styles/darkMode.css");
            primaryStage.getIcons().add(new Image("styles/tray.gif"));
            primaryStage.setTitle("Assignment Reminder App!");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}