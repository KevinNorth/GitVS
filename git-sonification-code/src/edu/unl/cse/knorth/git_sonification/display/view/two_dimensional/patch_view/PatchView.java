package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.patch_view;

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
    private static List<String> holdComponents;
    private static boolean holdVisualConflictsFlag;
    
    private List<Commit> commits;
    private ViewModel viewModel;
    private List<String> allComponents;
    private boolean visualConflictsFlag;
    private List<CheckmarkDrawable> checkmarks;
    
    public void run(List<Commit> commits, ViewModel viewModel,
            boolean visualConflictsFlag) {
        String[] emptyArgs = {};
        run(commits, viewModel, visualConflictsFlag, emptyArgs);
    }
    
    public void run(List<Commit> commits, ViewModel viewModel,
            boolean visualConflictsFlag, String args[]) {
        System.out.println("B-1");
        System.out.flush();

        holdCommits = commits;
        holdViewModel = viewModel;
        holdComponents = viewModel.getVisualizationData().getComponents();
        holdVisualConflictsFlag = visualConflictsFlag;

        System.out.println("B-2");
        System.out.flush();
        
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = PatchView.class.getCanonicalName();
        System.arraycopy(args, 0, newArgs, 1, args.length);

        System.out.println("B-3");
        System.out.flush();
        
        PApplet.main(newArgs);
        
        System.out.println("B-4");
        System.out.flush();
    }

    @Override
    public boolean shouldProgramCloseWhenWindowIsClosed() {
        return false;
    }
    
    @Override
    public void initialize() {
        System.out.println("B-5");
        System.out.flush();

        this.commits = holdCommits;
        this.viewModel = holdViewModel;
        this.allComponents = holdComponents;
        this.visualConflictsFlag = holdVisualConflictsFlag;

        System.out.println("B-6");
        System.out.flush();
    }

    @Override
    public String getWindowTitle() {
        System.out.println("B-7");
        System.out.flush();

        return "Details View (" + commits.get(0).getHash().substring(0, 7) +
                "..." + commits.get(commits.size()-1).getHash().substring(0, 7)
                + ")";
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
        System.out.println("B-8");
        System.out.flush();
        
        DrawablesProducer.PatchViewDrawables patchViewDrawables;
        
        if(visualConflictsFlag) {
            patchViewDrawables = new DrawablesProducer()
                    .produceDrawablesWithConflicts(viewModel, commits,
                        allComponents, this);
        } else {
            patchViewDrawables = new DrawablesProducer()
                    .produceDrawablesWithoutConflicts(viewModel, commits,
                        allComponents, this);
        }
        
        System.out.println("B-9");
        System.out.flush();
        
        checkmarks = patchViewDrawables.getCheckmarks();
        
        System.out.println("B-10");
        System.out.flush();
        
        ArrayList<Drawable> drawables = new ArrayList<Drawable>(checkmarks);
        drawables.addAll(patchViewDrawables.getNoninteractiveDrawables());
        
        System.out.println("B-11");
        System.out.flush();
        
        return drawables;
    }

    @Override
    public Color getInitialBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public ArrayList<KeyboardEvent<PatchViewState>> getInitialKeybaordEvents() {
        System.out.println("B-12");
        System.out.flush();

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
        
        System.out.println("B-13");
        System.out.flush();
        
        return keyboardEvents;
    }

    @Override
    public void update(long delta) {
        System.out.println("B-14");
        System.out.flush();
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