
package mediaplayer.main;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mediaplayer.classes.MyResources;

public class MediaPlayer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle rb=new MyResources(stage);
        Parent root = FXMLLoader.load(getClass().getResource("/mediaplayer/ui/mainView/FXMLDocument.fxml"),rb);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Media Player");
        stage.setMinHeight(100);
        stage.setMinWidth(665);
        stage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
