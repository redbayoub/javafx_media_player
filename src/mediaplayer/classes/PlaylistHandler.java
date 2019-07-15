package mediaplayer.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;

/**
 *
 * @author redayoub
 */
public class PlaylistHandler {
    private ListView<File> mediaListView;
    private int[] internalPlylist;
    private int currentPlayingIndex;
    private ShuffleState shuffleState;
    private RepeatState repeatState;
    

    public PlaylistHandler(ListView<File> mediaListView, ShuffleState shuffleState, RepeatState repeatState) {
        this.mediaListView = mediaListView;
        this.shuffleState = shuffleState;
        this.repeatState = repeatState;
        
        initMediaListView();
    }
    
    public int next(){
        int nextPlayingIndex=currentPlayingIndex+1;
        if(nextPlayingIndex>=internalPlylist.length){
           switch(repeatState){
                case Loop_One:
                    repeatState=RepeatState.NO_Loop;
                    break;
                case NO_Loop:
                    return -1;
           }
           nextPlayingIndex=0;     
        }
        currentPlayingIndex=nextPlayingIndex;
        return internalPlylist[currentPlayingIndex];
    }
    public int pervious(){
        int pervPlayingIndex=currentPlayingIndex-1;
        if(pervPlayingIndex<0){
            pervPlayingIndex=internalPlylist.length-1;
        }
        currentPlayingIndex=pervPlayingIndex;
        return internalPlylist[currentPlayingIndex];  
    }

    private void initMediaListView() {
        mediaListView.getItems().addListener(new ListChangeListener<File>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends File> c) {
                initInternalPlaylist(false);
            }       
        });
    }
    
    private void initInternalPlaylist(boolean fromOnToOff) {
        int size=mediaListView.getItems().size();
        internalPlylist=new int[size];
        
        switch(shuffleState){
            case ON :{
                ArrayList<Integer> arr = new ArrayList<>();
                Random rand = new Random(System.currentTimeMillis());
                while (arr.size() < size) {
                    int nb = rand.nextInt(size);
                    if (!arr.contains(nb)) { // make sure that nb not contains in arr
                        arr.add(nb);
                    }
                }
                int i=0;
                for(int nb:arr){
                    internalPlylist[i++]=nb;
                    //System.out.println(nb);
                }
                break;
            }
            case OFF:{
                int newCurrentPlayingInd=-1;
                if(currentPlayingIndex==-1)currentPlayingIndex=0;
                for(int i=0;i<size;i++){
                    internalPlylist[i]=i;
                    if(fromOnToOff && i==internalPlylist[currentPlayingIndex]) newCurrentPlayingInd=i;
                }
                if(newCurrentPlayingInd!=-1){
                    currentPlayingIndex=newCurrentPlayingInd;
                    return;
                }
                break; 
            }
        }
        currentPlayingIndex=-1;
    }

    public void setShuffleState(ShuffleState sSt) {
        if(shuffleState.equals(ShuffleState.ON)&&sSt.equals(ShuffleState.OFF)){
            this.shuffleState = sSt;
            initInternalPlaylist(true);
        }else{
            this.shuffleState = sSt;
             initInternalPlaylist(false);
        }
        
       
    }

    public void setRepeatState(RepeatState repeatState) {
        this.repeatState = repeatState;
    }
    
    
    
    
}
