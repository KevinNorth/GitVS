package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.util.Collection;

public class MoveSonificationCursorKeyboardEvent
    extends KeyboardEvent<BranchViewState> {
    private final MoveDirection direction;
    private final float speed;

    public MoveSonificationCursorKeyboardEvent(MoveDirection direction,
            float speed, Collection<Character> keysToRespondTo,
            Collection<Integer> codedKeysToRespondTo) {
        super(keysToRespondTo, codedKeysToRespondTo);
        this.direction = direction;
        this.speed = speed;
    }
    
    @Override
    public void onKeyDown(BranchViewState state) {
        // Do nothing
    }

    @Override
    public void whileKeyHeld(BranchViewState state, long delta) {
        Rectangle gridViewport = state.getCamera().getGridViewport();
        
        final float changePerSecond = speed;
        final float seconds = delta / 1000f;
        final float overallChange = changePerSecond * seconds;
        
        SonificationCursorDrawable sonificationCursor =
                state.getSonificationCursor();
        
        if(direction == MoveDirection.DOWN) {
            sonificationCursor.moveVertically(overallChange);
        } else {
            sonificationCursor.moveVertically(-overallChange);
        }
    }

    @Override
    public void onKeyUp(BranchViewState state) {
        // do nothing
    }
    
    public static enum MoveDirection {
        UP, DOWN;
    }
}