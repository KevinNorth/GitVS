package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.GitDataProcessor;
import edu.unl.cse.knorth.git_sonification.data_collection.components.CreateComponentTechniques;
import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Point;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.TwoDimensionalView;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java.TextDrawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class BranchView extends TwoDimensionalView<BranchViewState> {
    private DateTime since;
    private DateTime until;
    private String locationOfGitRepo;

    private ViewModel viewModel;
    private ArrayList<CommitDrawable> commits;
    private ArrayList<LineDrawable> lines;
    private ArrayList<DaySeparatorDrawable> daySeparators;
    private ArrayList<TextDrawable> timestamps;
    private SonificationCursorDrawable sonificationCursor;
    
    /* To hold onto the date values obtained from the command line arguments
     * until we're in a non-static context (i.e. not main()) */
    private static DateTime sinceFromArgs;
    private static DateTime untilFromArgs;
    private static String locationOfGitRepoFromArgs;
    
    public static void main(String args[]) {
        if(args.length == 2) {
            sinceFromArgs = DateTime.parse(args[0]);
            untilFromArgs = DateTime.parse(args[1]);
            locationOfGitRepoFromArgs = "../../voldemort/.git";
        } else if(args.length == 3) {
            sinceFromArgs = DateTime.parse(args[0]);
            untilFromArgs = DateTime.parse(args[1]);
            locationOfGitRepoFromArgs = args[2];
        } else {
            // Default date range is the first 10 days of November 2009
//            sinceFromArgs = new DateTime(2015, 1, 1, 0, 0);
//            untilFromArgs = new DateTime(2016, 1, 5, 0, 0);
            sinceFromArgs = new DateTime(2009, 11, 1, 0, 0);
            untilFromArgs = new DateTime(2009, 11, 10, 0, 0);
            locationOfGitRepoFromArgs = "../../voldemort/.git";
        }
        
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = BranchView.class.getCanonicalName();
        System.arraycopy(args, 0, newArgs, 1, args.length);

        PApplet.main(newArgs);
    }

    @Override
    public void initialize() {
        since = sinceFromArgs;
        until = untilFromArgs;
        locationOfGitRepo = locationOfGitRepoFromArgs;
    }
    
    @Override
    public int getSetupWidth() {
        return displayWidth / 2;
    }

    @Override
    public int getSetupHeight() {
        return displayHeight - 100;
    }

    @Override
    public Rectangle getInitialGridViewport() {
        return new Rectangle(0, 0, width, height);
    }

    @Override
    public ArrayList<Drawable> getInitialDrawables() {
        /* This function is rather involved. It calculates all of the commits
         * that need to be displayed, then determines where to place all of the
         * Drawables needed to represent those commits, all in one go.
         */

        try {
            viewModel = calculateViewModel();
        } catch(IOException err) {
            // Generally, an IOException means that the user didn't configure
            // the files the program needs to read correctly, or passed in the
            // wrong command line args. Showing them the stack trace gives them
            // a change to 
            err.printStackTrace();
            exit(); // Tells Processing to close program after setup() returns.
            return null;
        }
        
        DrawablesProducer drawablesProducer =  new DrawablesProducer();
        DrawablesProducer.CommitsAndLines commitsAndLines =
                drawablesProducer.produceCommitDrawables(viewModel);
        commits = commitsAndLines.getCommits();
        lines = commitsAndLines.getLines();
        
        DrawablesProducer.DaySeparatorsAndTimestamps daySeparatorsAndTimestamps
               = drawablesProducer.produceDaySeparatorsAndTimestamps(viewModel,
                       this);
        daySeparators = daySeparatorsAndTimestamps.getDaySeparators();
        timestamps = daySeparatorsAndTimestamps.getTimestamps();
        
        sonificationCursor =
                drawablesProducer.produceSonificationCursor(viewModel);
        
        ArrayList<Drawable> drawables = new ArrayList<>(1);
        drawables.addAll(commits);
        drawables.addAll(lines);
        drawables.addAll(timestamps);
        drawables.addAll(daySeparators);
        drawables.add(sonificationCursor);
        return drawables;
    }
    
    private ViewModel calculateViewModel() throws IOException {
        return new GitDataProcessor().processGitData(
                        locationOfGitRepo, since, until,
                        CreateComponentTechniques.EACH_INDIVIDUAL_FILE);
    }

    @Override
    public Color getInitialBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public void update(long delta) {
        CommitDrawable highlightedCommit =
                sonificationCursor.findCollidingCommit(commits);
        
        if(highlightedCommit != null) {
            highlightedCommit.setColor(Color.createHSBColor(
                    (char) (Math.random() * 256), Character.MAX_VALUE,
                    Character.MAX_VALUE));
        }
    }

    @Override
    public ArrayList<KeyboardEvent<BranchViewState>> getInitialKeybaordEvents() {
        ArrayList<KeyboardEvent<BranchViewState>> keyboardEvents =
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

        float cursorSpeed = 50.0f;
        
        List<Character> keys = new LinkedList<>();
        keys.add('w');
        keys.add('W');
        keyboardEvents.add(new MoveSonificationCursorKeyboardEvent(
                MoveSonificationCursorKeyboardEvent.MoveDirection.UP,
                cursorSpeed, keys, null));
        keys = new LinkedList<>();
        keys.add('s');
        keys.add('S');
        keyboardEvents.add(new MoveSonificationCursorKeyboardEvent(
                MoveSonificationCursorKeyboardEvent.MoveDirection.DOWN,
                cursorSpeed, keys, null));
        
        return keyboardEvents;
    }

    @Override
    public BranchViewState getWindowState() {
        return new BranchViewState(camera, sonificationCursor);
    }
    
    @Override
    public void handleMouseWheel(BranchViewState windowState,
            Point mouseLocationOnGridViewport, int numWheelClicks,
            MouseEvent rawMouseEvent) {
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