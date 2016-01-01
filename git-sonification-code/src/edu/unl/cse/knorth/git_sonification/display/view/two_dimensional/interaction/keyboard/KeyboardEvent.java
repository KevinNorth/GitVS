package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import processing.core.PApplet;

/**
 * Handles
 * @param <WindowState> An object that holds the information about the parent
 * <code>TwoDimensionalView</code>'s state that the KeyboardEvent is allowed to
 * read and manipulate.
 */
public abstract class KeyboardEvent<WindowState> {
    private final List<Character> keysToRespondTo;
    private final List<Integer> codedKeysToRespondTo;
        
    /**
     * @param keysToRespondTo The keyboard buttons that the KeyboardEvent
     * will respond to. Can be null if the KeyboardEvent only responds to coded
     * keys.
     * @param codedKeysToRespondTo The keyboard buttons represented by the
     * <code>PApplet.keyCode</code> variable that the KeyboardEvent will respond
     * to. Can be null if the KeyboardEvent only responds to noncoded keys.
     */
    public KeyboardEvent(Collection<Character> keysToRespondTo,
            Collection<Integer> codedKeysToRespondTo) {
        if(keysToRespondTo == null) {
            this.keysToRespondTo = null;
        } else {
            this.keysToRespondTo = new LinkedList<>(keysToRespondTo);
        }
        
        if(codedKeysToRespondTo == null) {
            this.codedKeysToRespondTo = null;
        } else {
            this.codedKeysToRespondTo = new LinkedList<>(codedKeysToRespondTo);
        }
    }
    
    public boolean respondsToKey(char key, Integer keyCode) {
        if(keysToRespondTo != null) {
            if(keysToRespondTo.contains(key)) {
                return true;
            }
        }
        
        if(key == PApplet.CODED && keyCode != null) {
            if(codedKeysToRespondTo != null) {
                if(codedKeysToRespondTo.contains(keyCode)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public abstract void onKeyDown(WindowState state);
    
    public abstract void whileKeyHeld(WindowState state, long delta);
    
    public abstract void onKeyUp(WindowState state);
}