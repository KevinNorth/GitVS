package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.BranchViewState;
import edu.unl.cse.knorth.git_sonification.GitDataProcessor;
import edu.unl.cse.knorth.git_sonification.data_collection.components.CreateComponentTechniques;
import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.view.GraphStringifier;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
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
import processing.core.PFont;

public class BranchView extends TwoDimensionalView<BranchViewState> {
    private ViewModel viewModel;
    private DateTime since;
    private DateTime until;
    private String locationOfGitRepo;
    
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
        
        PFont font = createFont("Courier New", 14);
        Color textColor = Color.BLACK;
        String string = new GraphStringifier().stringifyVisualizationData(
                viewModel.getVisualizationData(), false);
        Rectangle location = new Rectangle(0, 20, 999999999, 999999999);
        
        Drawable stringifiedData = new TextDrawable(location, 0, string, font,
                textColor);
        
        ArrayList<Drawable> drawables = new ArrayList<>(1);
        drawables.add(stringifiedData);
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

        return keyboardEvents;
    }

    @Override
    public BranchViewState getWindowState() {
        return new BranchViewState(camera);
    }
}