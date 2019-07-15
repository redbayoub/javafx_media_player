/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer.classes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.stage.Stage;

/**
 *
 * @author redayoub
 */
public class MyResources extends ResourceBundle{
    Stage myStage;
    public MyResources(Stage myStage) {
        this.myStage=myStage;
    }
    
    public Object handleGetObject(String key) {
         if (key.equals("stage")) return myStage;
         
         return null;
     }

     public Enumeration<String> getKeys() {
         return Collections.enumeration(keySet());
     }

     // Overrides handleKeySet() so that the getKeys() implementation
     // can rely on the keySet() value.
     protected Set<String> handleKeySet() {
         return new HashSet<String>(Arrays.asList("stage"));
     }
    
}
