
package mediaplayer.classes;

/**
 *
 * @author redayoub
 */
public enum RepeatState {
    Loop_One(1),Loop_All(3),NO_Loop(0);
    
    int num;
    private RepeatState(int num){
        this.num=num;
    }
    
    
}
