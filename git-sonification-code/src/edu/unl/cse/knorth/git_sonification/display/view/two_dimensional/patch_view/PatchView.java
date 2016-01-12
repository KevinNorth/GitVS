package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.patch_view;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Commit;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Point;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.TwoDimensionalView;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import processing.core.PApplet;
import static processing.core.PConstants.DOWN;
import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;
import static processing.core.PConstants.UP;
import processing.event.MouseEvent;

public class PatchView extends TwoDimensionalView<PatchViewState> {
    private static List<Commit> holdCommits;
    private static ViewModel holdViewModel;
    private static List<Component> holdComponents;
    
    private List<Commit> commits;
    private ViewModel viewModel;
    private List<Component> allComponents;
    private List<CheckmarkDrawable> checkmarks;
    
    public void run(List<Commit> commits, ViewModel viewModel) {
        String[] emptyArgs = {};
        run(commits, viewModel, emptyArgs);
    }
    
    public void run(List<Commit> commits, ViewModel viewModel,
            String args[]) {
        holdCommits = commits;
        holdViewModel = viewModel;
        holdComponents = viewModel.getVisualizationData().getComponents();

        String[] newArgs = new String[args.length + 1];
        newArgs[0] = PatchView.class.getCanonicalName();
        System.arraycopy(args, 0, newArgs, 1, args.length);

        PApplet.main(newArgs);
    }

    @Override
    public boolean shouldProgramCloseWhenWindowIsClosed() {
        return false;
    }
    
    @Override
    public void initialize() {
        this.commits = holdCommits;
        this.viewModel = holdViewModel;
        this.allComponents = holdComponents;
    }

    @Override
    public int getSetupWidth() {
        return displayWidth / 2;
    }

    @Override
    public int getSetupHeight() {
        return displayHeight / 3;
    }

    @Override
    public Rectangle getInitialGridViewport() {
        return new Rectangle(0, 0, width, height);
    }

    @Override
    public ArrayList<Drawable> getInitialDrawables() {
        DrawablesProducer.PatchViewDrawables patchViewDrawables =
                new DrawablesProducer().produceDrawables(viewModel, commits,
                        allComponents, this);
        
        checkmarks = patchViewDrawables.getCheckmarks();
        
        ArrayList<Drawable> drawables = new ArrayList<>(checkmarks);
        drawables.addAll(patchViewDrawables.getNoninteractiveDrawables());
        return drawables;
    }

    @Override
    public Color getInitialBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public ArrayList<KeyboardEvent<PatchViewState>> getInitialKeybaordEvents() {
        ArrayList<KeyboardEvent<PatchViewState>> keyboardEvents =
                new ArrayList<>(4);
        
        float scrollSpeed = 1.5f;
        
        List<Integer> keyCodes;
        keyCodes = new LinkedList<>();
        keyCodes.add(UP);
        keyboardEvents.add(new ScrollWindowKeyboardEvent(
                ScrollWindowKeyboardEvent.ScrollDirection.UP, scrollSpeed, null,
                keyCodes));
        keyCodes = new LinkedList<>();
        keyCodes.add(DOWN);
        keyboardEvents.add(new ScrollWindowKeyboardEvent(
                ScrollWindowKeyboardEvent.ScrollDirection.DOWN, scrollSpeed,
                null, keyCodes));
        keyCodes = new LinkedList<>();
        keyCodes.add(LEFT);
        keyboardEvents.add(new ScrollWindowKeyboardEvent(
                ScrollWindowKeyboardEvent.ScrollDirection.LEFT, scrollSpeed,
                null, keyCodes));
        keyCodes = new LinkedList<>();
        keyCodes.add(RIGHT);
        keyboardEvents.add(new ScrollWindowKeyboardEvent(
                ScrollWindowKeyboardEvent.ScrollDirection.RIGHT, scrollSpeed,
                null, keyCodes));
        
        return keyboardEvents;
    }

    @Override
    public void update(long delta) {
    }

    @Override
    public PatchViewState getWindowState() {
        return new PatchViewState(camera);
    }

    @Override
    public void handleMouseWheel(Point mouseLocationOnGridViewport,
            int numWheelClicks, MouseEvent rawMouseEvent) {
        float changeZoom = numWheelClicks / 20.0f;
        float zoomFactor = 1 + changeZoom;

        // Make sure the zoom factor can't be negative - that would cause weird
        // problems
        if(zoomFactor < 0.01f) {
            zoomFactor = 0.01f;
        }
        
        camera.zoomFromCurrentView(zoomFactor, mouseLocationOnGridViewport);
    }
}