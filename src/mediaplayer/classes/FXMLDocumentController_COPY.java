
package mediaplayer.classes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import mediaplayer.classes.PlaylistHandler;
import mediaplayer.classes.PrefrencesHandler;
import mediaplayer.classes.RepeatState;
import mediaplayer.classes.ShuffleState;
import mediaplayer.ui.FileCell.File_Cell;


/**
 *
 * @author redayoub
 */
public class FXMLDocumentController_COPY implements Initializable {

    @FXML
    private Slider videoSeeker;
    @FXML
    private Slider volumeControl;
    @FXML
    private FontAwesomeIconView playOrPauseBtn;
    @FXML
    private FontAwesomeIconView volumeBtn;
    @FXML
    private MenuItem AlwaysOnTopMenuItem;
     @FXML
    private Text currentDur;
    @FXML
    private Text endDur;
    @FXML
    private LineChart<String, Double> chart;
    @FXML
    private StackPane viewerPane;
    @FXML
    private AnchorPane rootPane;
     @FXML
    private StackPane rootStackPane;
    
    @FXML
    private AnchorPane playlistPane;
    @FXML
    private ListView<File> mediaListView;
    @FXML
    private ImageView repeatIcon;
    @FXML
    private ImageView shuffeIcon;
    
    
    
    private Stage myStage; 
    private PrefrencesHandler prefrencesHandler;
    private PlaylistHandler playlistHandler;
    private MediaPlayer mediaPlayer;
    private  MediaView mediaView;
    
    @FXML
    private MenuItem playMenuItem;
    @FXML
    private MenuItem stopMenuItem;
    @FXML
    private MenuItem pervMenuItem;
    @FXML
    private MenuItem nextMenuItem;
    @FXML
    private MenuItem increaseVolMenuItem;
    @FXML
    private MenuItem decreaseVolMenuItem;
    @FXML
    private MenuItem fullscreenMenuItem;
    @FXML
    private MenuItem showIconOnSysTrayMenuItem;
    @FXML
    private MenuItem muteUnmuteMI;
    @FXML
    private MenuItem snapshotMI;
    @FXML
    private FontAwesomeIconView seekBackBtn;
    @FXML
    private FontAwesomeIconView seekForeBtn;
    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane uiControlBtnPane;
   
    
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        myStage=(Stage) rb.getObject("stage");
        prefrencesHandler=PrefrencesHandler.getInstance();
        myStage.setOnHidden((ev) -> {
            prefrencesHandler.flush();
        });
        initAppSettings();
         // init the playlist
        initPlaylistHandler();
        initMediaListView();
        // init cuurentDur && endDur
        currentDur.setText("00:00:00");
        endDur.setText("00:00:00");
        // init chart
        initChart();
        // init snapshot MI to not visible
        snapshotMI.setVisible(false);
        // init Memonic for menu btns
        initMemonics();
        // add rippler to icons
       addJFXRipplerToNode(repeatIcon,"#204A87");
       addJFXRipplerToNode(shuffeIcon,"#204A87");
       addJFXRipplerToNode(playOrPauseBtn, "#000000");
       // init seekForeBtn and seekBackBtn
       initSeekFore();
       initSeekBack();
        
    }    

    @FXML
    private void playOrPause(MouseEvent event) {
        if(mediaPlayer!=null){
            if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING){
                mediaPlayer.pause();
                // set icon to play
                playOrPauseBtn.setGlyphName("PLAY");
                // set menu item to play
                playMenuItem.setText("Play");
                
            }
            if (mediaPlayer.getStatus()==MediaPlayer.Status.PAUSED){
                mediaPlayer.play();
                // set icon to pause
                playOrPauseBtn.setGlyphName("PAUSE");
                // set menu item to pause
                playMenuItem.setText("Pause");
            }
            if (mediaPlayer.getStatus()==MediaPlayer.Status.STOPPED){
                int nextPlayingInd=playlistHandler.next();
                if(nextPlayingInd==-1){
                    stop(null);
                }else{
                    mediaListView.getSelectionModel().select(nextPlayingInd);
                    playFile();
                }
            }
            
        }
    }

    @FXML
    private void stop(MouseEvent event) {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            // set icon to play
            playOrPauseBtn.setGlyphName("PLAY");
            // set play menu item to play
            playMenuItem.setText("Play");
        }
    }


    private void seekForward(){
        if(mediaPlayer!=null){
            double t=videoSeeker.getValue();
            t+=10;
            videoSeeker.setValue(t);
            System.out.println("seeking forward");
        }
        
    }
    
    private void seekBackword(){
        
         if(mediaPlayer!=null){
            double t=videoSeeker.getValue();
            t-=10;
            videoSeeker.setValue(t);
            System.out.println("seeking forward");
        }
    }

    @FXML
    private void muteOrUnmute(MouseEvent event) {

        if (volumeControl.getValue() == 0) {
            volumeControl.setValue(prefrencesHandler.getVolumeLevel());
            muteUnmuteMI.setText("Mute");
        } else {
            volumeControl.setValue(0);
            muteUnmuteMI.setText("Unmute");
        }

    }
    
    @FXML
    private void setOnTop(ActionEvent event) {
         // get stage
        
        if(AlwaysOnTopMenuItem.getGraphic()==null){ // set on top
            myStage.setAlwaysOnTop(true);
            // set Check Icon
            FontAwesomeIconView iconView=new FontAwesomeIconView(FontAwesomeIcon.CHECK);
            AlwaysOnTopMenuItem.setGraphic(iconView);
            // save setting
            prefrencesHandler.setAlwaysOnTop(true);
        }else{ // un--set on top
            myStage.setAlwaysOnTop(false);
            AlwaysOnTopMenuItem.setGraphic(null);
            // save setting
            prefrencesHandler.setAlwaysOnTop(false);
        }
    }
    

    @FXML
    private void fileDragged(DragEvent event) {
        Dragboard db=event.getDragboard();
        if(db.hasFiles()){
            event.acceptTransferModes(TransferMode.COPY);
        }
    }

    @FXML
    private void fileDropped(DragEvent event) {
        Dragboard db=event.getDragboard();
        List<File> newPlaylist=new ArrayList<>();
        if(db.hasFiles()){
            List<File> droopedFiles=db.getFiles();
            for(File f:droopedFiles){
               
                if(isAcceptedFile(f)){
                    newPlaylist.add(f);
                    
                }
            }
        }
        if(newPlaylist.size()>0){             
            setPlaylist(newPlaylist);
            
            event.setDropCompleted(true);
        }else{
            event.setDropCompleted(true);
            // show alert format not accepted
        }
    }

    private void setPlaylist(List<File> newPlaylist) {
        // there's file accepted
        stop(null);
        
        mediaListView.getItems().setAll(newPlaylist);
        int nextPlayingInd=playlistHandler.next();
        if(nextPlayingInd==-1){
            stop(null);
        }else{
            mediaListView.getSelectionModel().select(nextPlayingInd);
            playFile();
        }
    }
    private boolean isAudio(Media media) {
        for (String format:prefrencesHandler.getAccAudioFormats()){
            return media.getSource().toLowerCase().endsWith(format);
        }
        return false;
    }
    private boolean isVideo(Media media) {
        for (String format:prefrencesHandler.getAccVideoFormats()){
            return media.getSource().toLowerCase().endsWith(format);
        }
        return false;
    }
    /**
     * @param file 
     * @return true if the file format is accepted
     */
    private boolean isAcceptedFile(File f) {
        
        for (String format:prefrencesHandler.getAccAudioFormats()){
            if(f.getAbsolutePath().toLowerCase().endsWith(format))
                return true ;
        }
        
        for (String format:prefrencesHandler.getAccVideoFormats()){
            if(f.getAbsolutePath().toLowerCase().endsWith(format))
                return true ;
        }
        
        return false;
    }
    /*
        this method play a file
    */
    private void playFile() {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
        if(mediaListView.getItems().isEmpty())return;
        Media media=new Media(mediaListView.getSelectionModel().getSelectedItem().toURI().toString());
        mediaPlayer=getMediaPlayer(media);
        
    }

    private MediaPlayer getMediaPlayer(Media media) {
        MediaPlayer mediaPlayer=new MediaPlayer(media);
        mediaPlayer.setOnReady(()->{
            videoSeeker.setValue(1);
            videoSeeker.setMax(mediaPlayer.getMedia().getDuration().toMillis()/1000);
            endDur.setText(durationToString(mediaPlayer.getMedia().getDuration()));
            playOrPauseBtn.setGlyphName("PAUSE");
            playMenuItem.setText("Pause");
            mediaPlayer.play();
                });
        mediaPlayer.setOnStopped(() -> {
                mediaPlayer.dispose();
                if(mediaView!=null)
                    mediaView=null;
                playFile();
            });
        mediaPlayer.setOnEndOfMedia(()->{
            // start a new file if there is use playlist constreint
            int nextPlayingInd=playlistHandler.next();
            if(nextPlayingInd==-1){
                stop(null);
            }else{
                 mediaListView.getSelectionModel().select(nextPlayingInd);
                playFile();
            }
            
            // init cuurentDur && endDur
            currentDur.setText("00:00:00");
            endDur.setText("00:00:00");
        });
        
        
        mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable,
                Duration oldValue, Duration newValue) -> {
            videoSeeker.setValue(newValue.toSeconds());
            currentDur.setText(durationToString(newValue));
        });
        if(isAudio(media)){
             snapshotMI.setVisible(false);
            // set to view chart
            viewerPane.getChildren().setAll(chart);
            // add data to chart
            mediaPlayer.setAudioSpectrumListener((double timestamp,
                    double duration, float[] magnitudes, float[] phases) -> {
                XYChart.Series series = new XYChart.Series();
                int index=0;
                for(float mag:magnitudes){
                    series.getData().add(new XYChart.Data<>(Integer.toString(index), mag));
                    index++;
                }
                chart.getData().clear();
                chart.getData().add(series);
            });
        }else{ // this is a vedo 
            // prepare for video
            snapshotMI.setVisible(true);
           mediaView=new MediaView(mediaPlayer);
            
            mediaView.setPreserveRatio(true);
            mediaView.setSmooth(true);
            mediaView.setFitHeight(viewerPane.getHeight());
            mediaView.setFitWidth(viewerPane.getWidth());
            // set the viewer to media view
           viewerPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
           viewerPane.getChildren().setAll(mediaView);
           viewerPane.heightProperty().addListener((ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) -> {
                mediaView.setFitHeight(0);
                mediaView.setFitHeight(newValue.doubleValue());
                
            });
            viewerPane.widthProperty().addListener((ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) -> {
                mediaView.setFitWidth(0);
                mediaView.setFitWidth(newValue.doubleValue());
                
            });
            
            
            
        }
        
        
        mediaPlayer.setVolume(volumeControl.getValue());
        initVideoSeeker(media);
        return mediaPlayer;
    }

    private void initVolumeControl() {
        volumeControl.valueProperty().addListener((ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) -> {
            
            if(mediaPlayer!=null){
               mediaPlayer.setVolume(newValue.doubleValue());
            }
            prefrencesHandler.setVolumeLevel(newValue.doubleValue());
            if (newValue.doubleValue() == 0) {
                volumeBtn.setGlyphName("VOLUME_OFF");
                muteUnmuteMI.setText("Unmute");
            } else if (newValue.doubleValue() == 1) {
                volumeBtn.setGlyphName("VOLUME_UP");
                muteUnmuteMI.setText("Mute");
            } else {
                volumeBtn.setGlyphName("VOLUME_DOWN");
                muteUnmuteMI.setText("Mute");
            }

        
        });
    }
    private void initVideoSeeker(Media media){
        videoSeeker.setMin(1);
        videoSeeker.valueProperty().addListener((ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) -> {
            int diff=newValue.intValue() - oldValue.intValue();
            /* this is just a trick to know if the user will have to change
                more than "1 second"Or the player changed duiration  */
            if((mediaPlayer!=null)&&((diff>1)||(diff<0))) {
                double newDur = newValue.intValue() * 1000;
                mediaPlayer.seek(new Duration(newDur));
            }
            
        });
    }

    private void initChart() {
        chart.getXAxis().setTickLabelsVisible(false);
        chart.getXAxis().setTickMarkVisible(false);
        chart.getXAxis().setVisible(false);
        chart.getYAxis().setTickLabelsVisible(false);
        chart.getYAxis().setTickMarkVisible(false);
        chart.getYAxis().setVisible(false);
        chart.getXAxis().setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.EMPTY)));
        chart.getYAxis().setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.EMPTY)));
    }    
    

    @FXML
    private void changePlaylistWidth(MouseEvent event) {
        //System.out.println(event.getSceneX());
        double newPos=event.getSceneX();
        AnchorPane.setLeftAnchor(playlistPane, newPos);
        AnchorPane.setRightAnchor(viewerPane, viewerPane.getScene().getWidth()-newPos);
    }

    @FXML
    private void showOrHidePlaylist(MouseEvent event) {
        if(AnchorPane.getRightAnchor(viewerPane)==0.0) { // show playlist
            playlistPane.setVisible(true);
            AnchorPane.setRightAnchor(viewerPane, playlistPane.getWidth());
            
        }else{ // hide playlist
            playlistPane.setVisible(false);
            AnchorPane.setRightAnchor(viewerPane, 0.0);
        }
    }

    
    private void initMediaListView() {
        mediaListView.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> param) {   
                return new File_Cell();
            }
        });
        mediaListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            stop(null);
            // the selected file will be played auto after stopping the current one
        });
    }

   
    @FXML
    private void loopSelector(MouseEvent event) {
        switch(getRepeatState()){
            case NO_Loop:
                setRepeatStateIcon(RepeatState.Loop_One);
                setRepeatStateIcon(RepeatState.Loop_One);
                break;
            case Loop_One:
                setRepeatStateIcon(RepeatState.Loop_All);
                setRepeatStateIcon(RepeatState.Loop_All);
                break;
            case Loop_All:
                setRepeatStateIcon(RepeatState.NO_Loop);
                setRepeatStateIcon(RepeatState.NO_Loop);
                break;
 
        }
    }

    @FXML
    private void shuffelSelector(MouseEvent event) {
        switch(getShuffleState()){
            case OFF:
                setShuffleIcon(ShuffleState.ON);
                playlistHandler.setShuffleState(ShuffleState.ON);
                break;
            case ON:
                setShuffleIcon(ShuffleState.OFF);
                playlistHandler.setShuffleState(ShuffleState.OFF);
                break;
        }
    }
    
    private void initAppSettings() {
        // ====== init always on top =========
        if(prefrencesHandler.getAlwaysOnTop()){ // set on top
            myStage.setAlwaysOnTop(true);
            // set Check Icon
            FontAwesomeIconView iconView=new FontAwesomeIconView(FontAwesomeIcon.CHECK);
            AlwaysOnTopMenuItem.setGraphic(iconView);
        }else{ // un--set on top
            myStage.setAlwaysOnTop(false);
            AlwaysOnTopMenuItem.setGraphic(null);
        }
        
        // ====== init system tray icon =========
        if(prefrencesHandler.getIconOnSystemTray()){ // add icon to sys tray
           addIconToSysTray(true);
            // set Check Icon
            FontAwesomeIconView iconView=new FontAwesomeIconView(FontAwesomeIcon.CHECK);
            showIconOnSysTrayMenuItem.setGraphic(iconView);
        }else{ // un--set on top
            addIconToSysTray(false);
            showIconOnSysTrayMenuItem.setGraphic(null);
        }
        // ========== init volume 
        initVolumeControl();
        volumeControl.setValue(prefrencesHandler.getVolumeLevel());
         // ========= init repeat and shuffle ======
        setRepeatStateIcon(prefrencesHandler.getRepeatState());
        setShuffleIcon(prefrencesHandler.getShuffleState());
        // add tooltip to btns
        
        
    }

    private void setShuffleIcon(ShuffleState ss) {
        switch(ss){
            case ON : 
                shuffeIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream("mediaplayer/ui/icons/icons8_Shuffle_64px.png")));
                break;
            case OFF : 
                shuffeIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream("mediaplayer/ui/icons/icons8_Stop_Shuffle_64px.png")));
                break;
        }
        shuffeIcon.setAccessibleText(ss.toString());
    }
    
    private ShuffleState getShuffleState() {
        
        return ShuffleState.valueOf(shuffeIcon.getAccessibleText());
    }

    private void setRepeatStateIcon(RepeatState rs) { 
        switch (rs) {
            case NO_Loop:
                repeatIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream("mediaplayer/ui/icons/icons8_No_Repeat_64px.png")));
                break;
            case Loop_One:
                repeatIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream("mediaplayer/ui/icons/icons8_Repeat_One_64px.png")));
                break;
            case Loop_All:
                repeatIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream("mediaplayer/ui/icons/icons8_Repeat_64px_1.png")));
                break;
        }
        repeatIcon.setAccessibleText(rs.toString());
    }
    
    private RepeatState getRepeatState() { 

        if(repeatIcon.getAccessibleText().equals(RepeatState.NO_Loop.toString()))
            return RepeatState.NO_Loop;
        if(repeatIcon.getAccessibleText().equals(RepeatState.Loop_One.toString()))
            return RepeatState.Loop_One;
        if(repeatIcon.getAccessibleText().equals(RepeatState.Loop_All.toString()))
            return RepeatState.Loop_All;    
        return null;
    }

    private String durationToString(Duration duration) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis((long) duration.toMillis());
        StringBuilder sb=new StringBuilder();
        sb.append(formatNumber(calendar.get(Calendar.HOUR))).append(":").
                append(formatNumber(calendar.get(Calendar.MINUTE))).append(":").
                append(formatNumber(calendar.get(Calendar.SECOND)));
        return sb.toString();
    }
    private String formatNumber(int nb){
        if(nb<10)
            return "0"+nb;
        else
            return Integer.toString(nb);
    }

    private void initPlaylistHandler() {
        playlistHandler=new PlaylistHandler(mediaListView, getShuffleState(), getRepeatState());
    }

    private void addJFXRipplerToNode(Node node, String color) {
        AnchorPane parent=(AnchorPane) node.getParent();
        Double leftAnch = AnchorPane.getLeftAnchor(node);
        Double rightAnch = AnchorPane.getRightAnchor(node);
        Double bottomAnch = AnchorPane.getBottomAnchor(node);
        Double UpperAnch = AnchorPane.getTopAnchor(node);

        JFXRippler rippler = new JFXRippler(node);
        rippler.setRipplerFill(Paint.valueOf(color));
        
        parent.getChildren().add(rippler);

        AnchorPane.setLeftAnchor(rippler, leftAnch);
        AnchorPane.setRightAnchor(rippler, rightAnch);
        AnchorPane.setBottomAnchor(rippler, bottomAnch);
        AnchorPane.setTopAnchor(rippler, UpperAnch);
      
    }

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser chooser=new FileChooser();
        ArrayList<String> exts=new ArrayList<>(Arrays.asList(prefrencesHandler.getAccVideoFormats()));
        exts.addAll(Arrays.asList(prefrencesHandler.getAccAudioFormats()));
        ArrayList<String> upperCaseExts=new ArrayList<>();
        // add "*." to exts
        for(int i=0;i<exts.size();i++){
            exts.set(i, "*."+exts.get(i));
            upperCaseExts.add(exts.get(i).toUpperCase());   
        }
        // add upper case exts to ext
        exts.addAll(upperCaseExts);
        chooser.getExtensionFilters().setAll(new ExtensionFilter("Audio/Video files",exts));
        chooser.setTitle("Select files");
        List<File> newPlaylist=chooser.showOpenMultipleDialog(null);
        if(newPlaylist!=null&&newPlaylist.size()>0)
            setPlaylist(newPlaylist);
    }

    @FXML
    private void quit(ActionEvent event) {
        if(mediaPlayer!=null){
            mediaPlayer.dispose();
            stop(null);
        }
        Platform.exit();
    }

    @FXML
    private void playMI(ActionEvent event) {
        playOrPause(null);
    }

    @FXML
    private void stopMI(ActionEvent event) {
        stop(null);
    }

    @FXML
    private void goToPerv(ActionEvent event) {
        int pervPlayingInd = playlistHandler.pervious();
        if (pervPlayingInd == -1) {
            stop(null);
        } else {
            mediaListView.getSelectionModel().select(pervPlayingInd);
            playFile();
        }
    }

    @FXML
    private void goToNext(ActionEvent event) {
        int nextPlayingInd = playlistHandler.next();
        if (nextPlayingInd == -1) {
            stop(null);
        } else {
            mediaListView.getSelectionModel().select(nextPlayingInd);
            playFile();
        }
    }

    @FXML
    private void increaseVol(ActionEvent event) {
        if(volumeControl.getValue()<1){
            volumeControl.setValue(volumeControl.getValue()+0.1);
        }
    }

    @FXML
    private void decreaseVol(ActionEvent event) {
        if(volumeControl.getValue()>0){
            volumeControl.setValue(volumeControl.getValue()-0.1);
        }
    }

    @FXML
    private void switchMuteMI(ActionEvent event) {
        muteOrUnmute(null);
    }
    
    @FXML
    private void switchFullscreen(ActionEvent event) {
        if(myStage.isFullScreen()){
            //uiControlBtnPane.setVisible(true);
            //menuBar.setDisable(true);
            myStage.setFullScreen(false);
        }else{
//             uiControlBtnPane.setVisible(false);
//            menuBar.setDisable(false);
            myStage.setFullScreen(true);
        }
    }

    @FXML
    private void takeSnapshot(ActionEvent event) {
        WritableImage wi=mediaView.snapshot(null, null);
        try {
            String homeDir=System.getProperty("user.home");
            String fileName="Snapshot: "+DateFormat.getDateTimeInstance().format(new Date());
            File output=new File(homeDir+System.getProperty("file.separator")+fileName);
            ImageIO.write(SwingFXUtils.fromFXImage(wi,null), "jpg", output);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController_COPY.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void showPreferences(ActionEvent event) {
    }

    @FXML
    private void showIconOnSysTray(ActionEvent event) {
        if(showIconOnSysTrayMenuItem.getGraphic()==null){ // add icon to sys tray
            
            addIconToSysTray(true);
            // set Check Icon
            FontAwesomeIconView iconView=new FontAwesomeIconView(FontAwesomeIcon.CHECK);
            showIconOnSysTrayMenuItem.setGraphic(iconView);
            // save setting
            prefrencesHandler.setIconOnSystemTray(true);
            System.out.println("prefrencesHandler.setIconOnSystemTray(true);");
        }else{ // delete icon from sys tray
            addIconToSysTray(false);
            // Un-set Check Icon
            showIconOnSysTrayMenuItem.setGraphic(null);
            // save setting
            prefrencesHandler.setIconOnSystemTray(false);
        }
    }

    @FXML
    private void showAbout(ActionEvent event) {
        
        JFXDialog dialog=new JFXDialog(rootStackPane, null, JFXDialog.DialogTransition.CENTER);
        JFXDialogLayout content=new JFXDialogLayout();
        content.setHeading(new Text("About"));
        String about="  This program can play Video / Audio Files \n"
                + "This is a project that it took from me about 2 Weeks with a lot of interuptions\n\n"
                + "Created by Red Ayoub ";
        ImageView pgmIcon=new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("mediaplayer/ui/icons/icons8_Circled_Play_48px.png")));
        
        content.setBody(new HBox(10,pgmIcon,new Text(about)));
        
        JFXButton button=new JFXButton("Okey");
        
        button.setRipplerFill(Paint.valueOf("#1F1F73"));
        button.setTextFill(Color.WHITE);
        
        button.setBackground(new Background(new BackgroundFill(Paint.valueOf("#4444ff"),new CornerRadii(10), Insets.EMPTY)));
        button.setOnAction((ev) -> {
            dialog.close();
        });
        content.setActions(button);
        
        dialog.setContent(content);
        dialog.show();
    }

    private void initMemonics() {

        playMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.SPACE, new KeyCombination.Modifier[]{}));
        stopMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, new KeyCombination.Modifier[]{}));
        pervMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P, new KeyCombination.Modifier[]{}));
        nextMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, new KeyCombination.Modifier[]{}));
        increaseVolMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.UP, new KeyCombination.Modifier[]{}));
        decreaseVolMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DOWN, new KeyCombination.Modifier[]{}));
        fullscreenMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F, new KeyCombination.Modifier[]{}));
    }
    // this var is needed for holder to know if it worked or not
    boolean holderWorked;
    private void initSeekFore() {
        PauseTransition holder=new PauseTransition(Duration.seconds(1));
        
        seekForeBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, (ev) -> {
            holderWorked=false;
            holder.playFromStart();
            holder.setOnFinished((e) -> {
                seekForward();
                holderWorked=true;
                holder.playFromStart();
            });
        });
        seekForeBtn.addEventHandler(MouseEvent.MOUSE_RELEASED, (ev) -> {
             holder.stop();
            //go next if holder didn't finshed one period
            if(!holderWorked){
                System.out.println("go to next");
                goToNext(null);
            }
        });
        seekForeBtn.addEventHandler(MouseEvent.MOUSE_DRAGGED, (ev) -> {
             holder.stop();
            //go next if holder didn't finshed one period
            if(!holderWorked){
                System.out.println("go to next");
                goToNext(null);
            }
        });
    }

    private void initSeekBack() {
        PauseTransition holder=new PauseTransition(Duration.seconds(1));
        seekBackBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, (ev) -> {
            holderWorked=false;
            holder.playFromStart();
            holder.setOnFinished((e) -> {
                holderWorked=true;
                seekBackword();
                holder.playFromStart();
            });
        });
        seekBackBtn.addEventHandler(MouseEvent.MOUSE_RELEASED, (ev) -> {
             holder.stop();
            //go perv
            if(!holderWorked) 
                goToPerv(null);
        });
        seekBackBtn.addEventHandler(MouseEvent.MOUSE_DRAGGED, (ev) -> {
             holder.stop();
            //go to perv
            if(!holderWorked)
                 goToPerv(null);
        });
    }
    // this is tray icon used for this pgm
    TrayIcon trayIcon;
    private void addIconToSysTray(boolean create) {
        if(!SystemTray.isSupported())return;
       
        SystemTray tray=SystemTray.getSystemTray();
        if(create){
            Platform.setImplicitExit(false);
           java.awt.Image trayIconImage=Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("mediaplayer/ui/icons/icons8_Circled_Play_48px.png"));
           
           PopupMenu popupMenu=new PopupMenu();
           //============================
           java.awt.MenuItem playItem=new java.awt.MenuItem("Play");
           playItem.addActionListener((e) -> {
               Platform.runLater(() -> {
                   playMI(null);
               });  
           });
           playMenuItem.textProperty().addListener((observable, oldValue, newValue) -> {
               playItem.setLabel(newValue);
           });
           //-----------------
           java.awt.MenuItem stopItem=new java.awt.MenuItem("Stop");
           stopItem.addActionListener((e) -> {
               Platform.runLater(() -> {
                   stopMI(null);
               });
           });
           //----------------
           java.awt.MenuItem nextItem=new java.awt.MenuItem("Next");
           nextItem.addActionListener((e) -> {
               Platform.runLater(() -> {
                   goToNext(null);
               });       
           });
           //-----------------
           java.awt.MenuItem pervItem=new java.awt.MenuItem("Pervious");
           pervItem.addActionListener((e) -> {
               Platform.runLater(() -> {
                   goToPerv(null);
               });  
           });
           //------------
           java.awt.MenuItem exitItem=new java.awt.MenuItem("Quit");
           exitItem.addActionListener((e) -> {
               Platform.runLater(() -> {
                   quit(null); 
               });
               System.exit(0);
           });
           //==========================
           popupMenu.add(playItem);
           popupMenu.add(stopItem);
           popupMenu.add(nextItem);
           popupMenu.add(pervItem);
           popupMenu.addSeparator();
           popupMenu.add(exitItem);
           trayIcon=new TrayIcon(trayIconImage, "Media Player", popupMenu);
           trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener((e) -> {
                if (!myStage.isShowing()) {
                    Platform.runLater(() -> {
                        myStage.show();
                    });
                }
            });
            try {
                tray.add(trayIcon);
            } catch (AWTException ex) {
                Logger.getLogger(FXMLDocumentController_COPY.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            if(trayIcon!=null)
                tray.remove(trayIcon);
        }
    }

   
}
