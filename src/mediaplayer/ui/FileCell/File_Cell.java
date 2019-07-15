/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer.ui.FileCell;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import mediaplayer.ui.mainView.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author redayoub
 */
public class File_Cell extends ListCell<File>{
    @FXML
    private Label filenameLabel;
    
    public File_Cell() {
        loadFXML();
        filenameLabel.setOnMousePressed((event) -> {
            userClickListener();
        });
    }

    private void loadFXML() {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("file_cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(File_Cell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            
            setText(null);
            //set to text only.
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else{
            // set fields
          filenameLabel.setText(item.getName());
          filenameLabel.setAccessibleText(item.getAbsolutePath());

            //set to graphic only
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        }
    }

    private void userClickListener() {
        FXMLDocumentController.getInstance().userClickedOnFileInML(filenameLabel.getAccessibleText());
    }

    
   
}
