package mediaplayer.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author redayoub
 */
public class PrefrencesHandler {
    private final Preferences preferences;
    private static PrefrencesHandler prefrencesHandler;
    
    private boolean changeHasBeenMaden=false;
    
    private static final File mSettings=new File("app_settings.xml");
    //-----------------
    // private static final String ACC_AUDIO_FORMATS="acc_audio_formats";
    private static final String DEFAULT_AUDIO_FORMATS="mp3,wav";
    //-------------------
    // private static final String ACC_VIDEO_FORMATS="acc_video_formats";
    private static final String DEFAULT_VIDEO_FORMATS="mp4,flv";
     //-------------------
    private static final String VOLUME_LEVEL="volume_level";
    private static final double DEFAULT_VOLUME_LEVEL=1;
    //--------------------
    private static final String ALWAYS_ON_TOP="always_on_top";
    private static final boolean DEFAULT_ALWAYS_ON_TOP=false;
    //--------------------
    private static final String ICON_ON_SYSTEM_TRAY="icon_on_system_tray";
    private static final boolean DEFAULT_ICON_ON_SYSTEM_TRAY=false;
    //--------------------
    private static final String SHUFFLE_ON="shuffle_on";
    private static final String DEFAULT_SHUFFLE_ON=ShuffleState.ON.toString();
    //--------------------
    private static final String REPEAT_STATE="repeat_state";
    private static final String DEFAULT_REPEAT_STATE=RepeatState.NO_Loop.toString(); // 0 : No_Repeat | 1 : One_Repeat | 3 : All_Repeat

    
    private PrefrencesHandler() {
        preferences=getPreferences(mSettings);
    }
    
    
    private static Preferences getPreferences(File file){
       
        Preferences preferences=Preferences.userNodeForPackage(MediaPlayer.class);
        if (file.exists()){
            try {
                Preferences.importPreferences(new FileInputStream(file));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PrefrencesHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | InvalidPreferencesFormatException ex) {
                Logger.getLogger(PrefrencesHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{
           // init prefrences
            //preferences.put(ACC_AUDIO_FORMATS, DEFAULT_AUDIO_FORMATS);
            //preferences.put(ACC_VIDEO_FORMATS, DEFAULT_VIDEO_FORMATS);
            preferences.putDouble(VOLUME_LEVEL, DEFAULT_VOLUME_LEVEL);
            preferences.putBoolean(ALWAYS_ON_TOP, DEFAULT_ALWAYS_ON_TOP);
            preferences.putBoolean(ICON_ON_SYSTEM_TRAY, DEFAULT_ICON_ON_SYSTEM_TRAY);
            preferences.put(SHUFFLE_ON, DEFAULT_SHUFFLE_ON);
            preferences.put(REPEAT_STATE, DEFAULT_REPEAT_STATE);
            try {
                preferences.exportNode(new FileOutputStream(mSettings));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PrefrencesHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PrefrencesHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BackingStoreException ex) {
                Logger.getLogger(PrefrencesHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return preferences;
    }
    
    public static PrefrencesHandler getInstance(){
        if(prefrencesHandler==null){
            prefrencesHandler=new PrefrencesHandler();
        }
        return prefrencesHandler;
    }
   
    // ========= geterrs and setters
    public double getVolumeLevel(){
        return preferences.getDouble(VOLUME_LEVEL, DEFAULT_VOLUME_LEVEL);
    }
    public void setVolumeLevel(double vl){
        preferences.putDouble(VOLUME_LEVEL, vl);
        changeHasBeenMaden=true;
    }
    // -----------------
    public boolean getAlwaysOnTop(){
        return preferences.getBoolean(ALWAYS_ON_TOP, DEFAULT_ALWAYS_ON_TOP);
    }
    public void setAlwaysOnTop(boolean aot){
        preferences.putBoolean(ALWAYS_ON_TOP, aot);
        changeHasBeenMaden=true;
    }
    // -----------------
    public boolean getIconOnSystemTray(){
        return preferences.getBoolean(ICON_ON_SYSTEM_TRAY, DEFAULT_ICON_ON_SYSTEM_TRAY);
    }
    public void setIconOnSystemTray(boolean iost){
        preferences.putBoolean(ICON_ON_SYSTEM_TRAY, iost);
        changeHasBeenMaden=true;
        
    }
    // -----------------
    public ShuffleState getShuffleState(){
        return ShuffleState.valueOf(preferences.get(SHUFFLE_ON, DEFAULT_SHUFFLE_ON));
    }
    public void setShuffleOn(ShuffleState sst){
        preferences.put(SHUFFLE_ON, sst.toString());
        changeHasBeenMaden=true;
    }
    // -----------------
    public RepeatState getRepeatState(){
        return RepeatState.valueOf(preferences.get(REPEAT_STATE, DEFAULT_REPEAT_STATE));
    }
    // -----------------
    public void setRepeatState(RepeatState rs){
        preferences.put(REPEAT_STATE, rs.toString());
        changeHasBeenMaden=true;
    }
    
    // -----------------
    public String[] getAccAudioFormats(){
        return  DEFAULT_AUDIO_FORMATS.split(",");
    }
    
    // -----------------
    public String[] getAccVideoFormats(){
        return  DEFAULT_VIDEO_FORMATS.split(",");
    }
    // -----------------
    
    public void flush(){
        try {
            preferences.flush();
            if(changeHasBeenMaden)
                preferences.exportNode(new FileOutputStream(mSettings));
        } catch (BackingStoreException ex) {
            Logger.getLogger(PrefrencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrefrencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrefrencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
