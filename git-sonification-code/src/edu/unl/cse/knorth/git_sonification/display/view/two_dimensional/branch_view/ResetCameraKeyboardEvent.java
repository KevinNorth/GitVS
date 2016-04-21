package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.util.Collection;

public class ResetCameraKeyboardEvent extends KeyboardEvent<BranchViewState> {
    private final Rectangle locationToResetTo;

    public ResetCameraKeyboardEvent(Rectangle locationToResetTo,
            Collection<Character> keysToRespondTo,
            Collection<Integer> codedKeysToRespondTo) {
        super(keysToRespondTo, codedKeysToRespondTo);
        this.locationToResetTo = locationToResetTo;
    }
    
    @Override
    public void onKeyDown(BranchViewState state) {
        state.getCamera().setGridViewport(locationToResetTo);
    }

    @Override
    public void whileKeyHeld(BranchViewState state, long delta) {
        // Do nothing
    }

    @Override
    public void onKeyUp(BranchViewState state) {
        // Do nothing
    }
}