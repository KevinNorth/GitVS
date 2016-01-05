package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.util.Collection;

public class ScrollWindowKeyboardEvent extends KeyboardEvent<BranchViewState> {
    private final ScrollDirection direction;
    private final float speed;

    public ScrollWindowKeyboardEvent(ScrollDirection direction, float speed,
            Collection<Character> keysToRespondTo,
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
        
        final float changePerSecond;
        if(direction.isVertical()) {
            changePerSecond = speed * gridViewport.getHeight();
        } else {
            changePerSecond = speed * gridViewport.getWidth();
        }
        
        final float seconds = delta / 1000f;
        final float overallChange = changePerSecond * seconds;
        
        switch(direction) {
            case UP:
                gridViewport.setY1(gridViewport.getY1() - overallChange);
                gridViewport.setY2(gridViewport.getY2() - overallChange);
                break;
            case DOWN:
                gridViewport.setY1(gridViewport.getY1() + overallChange);
                gridViewport.setY2(gridViewport.getY2() + overallChange);
                break;
            case LEFT:
                gridViewport.setX1(gridViewport.getX1() - overallChange);
                gridViewport.setX2(gridViewport.getX2() - overallChange);
                break;
            case RIGHT:
                gridViewport.setX1(gridViewport.getX1() + overallChange);
                gridViewport.setX2(gridViewport.getX2() + overallChange);
                break;
        }
    }

    @Override
    public void onKeyUp(BranchViewState state) {
        // Do nothing
    }
    
    public static enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT;
        
        public boolean isVertical() {
            return this == UP || this == DOWN;
        }
        
        public boolean isHorizontal() {
            return this == LEFT || this == RIGHT;
        }
    }
}