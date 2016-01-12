package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.GitDataProcessor;
import edu.unl.cse.knorth.git_sonification.data_collection.components.CreateComponentTechniques;
import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Commit;
import edu.unl.cse.knorth.git_sonification.display.view.deprecated3d.VisMain;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Point;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.TwoDimensionalView;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java.TextDrawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.patch_view.PatchView;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class BranchView extends TwoDimensionalView<BranchViewState> {
    private DateTime since;
    private DateTime until;
    private String locationOfGitRepo;

    private ViewModel viewModel;
    private ArrayList<CommitDrawable> commits;
    private ArrayList<BranchLineDrawable> lines;
    private ArrayList<DaySeparatorDrawable> daySeparators;
    private ArrayList<TextDrawable> timestamps;
    private SonificationCursorDrawable sonificationCursor;
    private SelectionRectangleDrawable patchSelection;
    
    private CommitDrawable previouslyHighlightedCommit;
    
    private Clip currentCommitClip;
    private Clip[] developerClips;
    private Clip daySeperatorClip;
    private Clip currentConflictClip;
    private Clip[] conflictClips;

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
    public boolean shouldProgramCloseWhenWindowIsClosed() {
        return true;
    }
    
    @Override
    public void initialize() {
        since = sinceFromArgs;
        until = untilFromArgs;
        locationOfGitRepo = locationOfGitRepoFromArgs;
        
        try {
            developerClips = new Clip[14];
            conflictClips = new Clip[4];
            
            for (int i = 0; i < 14; i++) {
                developerClips[i] = AudioSystem.getClip();
                developerClips[i].open(AudioSystem.getAudioInputStream(
                        new File("audio/dev" + (i + 1) + ".wav")));
            }
            daySeperatorClip = AudioSystem.getClip();
            daySeperatorClip.open(AudioSystem.getAudioInputStream(
                    new File("audio/day_separator.wav")));
            for (int i = 0; i < 4; i++) {
                conflictClips[i] = AudioSystem.getClip();
                conflictClips[i].open(AudioSystem.getAudioInputStream(new File("audio/conflict_drums_" + (i + 1) + ".wav")));
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(VisMain.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
        patchSelection = null;
        
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
        
        if(highlightedCommit != null
                && highlightedCommit != previouslyHighlightedCommit) {
            playSoundForCommit(highlightedCommit);
            previouslyHighlightedCommit = highlightedCommit;
        }
    }

    private void playSoundForCommit(CommitDrawable commit) {
        /*
         * Developer sound
         */
        List<String> authorsInOrderOfCommitCounts = viewModel.
                getSonificiationData().getAuthorsInOrderOfCommitCounts();
        
        String author = commit.getCommit().getAuthor();
        
        Clip developerClip = null;
        for(int i = 0; i < developerClips.length - 1
                && i < authorsInOrderOfCommitCounts.size(); i++) {
            if(author.equals(authorsInOrderOfCommitCounts.get(i))) {
                developerClip = developerClips[i];
                break;
            }
        }
        if(developerClip == null) {
            developerClip = developerClips[developerClips.length - 1];
        }
        
        if(currentCommitClip != null && currentCommitClip.isActive()
                && currentCommitClip != developerClip) {
            currentCommitClip.setFramePosition(0);
            currentCommitClip.stop();
        }

        
        developerClip.setFramePosition(0);
        developerClip.start();
        
        currentCommitClip = developerClip;
        
        /*
         * Conflict drums
         */
        if(commit.getNumConflicts() > 0) {
            Clip conflictClip = null;
            
            if(commit.getNumConflicts() > conflictClips.length) {
                conflictClip = conflictClips[conflictClips.length - 1];
            } else {
                conflictClip = conflictClips[commit.getNumConflicts() - 1];
            }
            
            if(currentConflictClip != null && currentConflictClip.isActive()
                    && currentConflictClip != conflictClip) {
                currentConflictClip.setFramePosition(0);
                currentConflictClip.stop();
            }
            
            conflictClip.setFramePosition(0);
            conflictClip.start();
            
            currentConflictClip = conflictClip;
        } else {
            if(currentConflictClip != null && currentConflictClip.isActive()) {
                currentConflictClip.setFramePosition(0);
                currentConflictClip.stop();
            }
            currentConflictClip = null;
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
    public void handleLeftMouseButton(Point mouseLocationOnGridViewport,
            MouseEvent rawMouseEvent) {
        System.out.println("Left mouse button");
    }

    @Override
    public void handleMiddleMouseButton(Point mouseLocationOnGridViewport,
            MouseEvent rawMouseEvent) {
        System.out.println("Middle mouse button");
    }
    
    @Override
    public void handleRightMouseButton(Point mouseLocationOnGridViewport,
            MouseEvent rawMouseEvent) {
        sonificationCursor.setVerticalLocation(
                mouseLocationOnGridViewport.getY());
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

    @Override
    public void whileMouseDragged(Point mouseLocationOnGridViewport,
            Point startOfDragOnGridViewport, Rectangle dragArea,
            MouseEvent rawEvent) {
        if(patchSelection == null) {
            Color fillColor = Color.createRGBColor((char) 20, (char) 20,
                    Character.MAX_VALUE, (char) 127);
            Color borderColor = Color.createRGBColor((char) 20, (char) 20,
                    Character.MAX_VALUE);
            patchSelection = new SelectionRectangleDrawable(fillColor,
                    borderColor, 1.0f, dragArea, Integer.MAX_VALUE);
            
            drawables.add(patchSelection);
        } else {
            patchSelection.setBoundingRectangle(dragArea);
        }
    }
    
    @Override
    public void whenMouseDragReleased(Point mouseLocationOnGridViewport,
            Point startOfDragOnGridViewport, Rectangle dragArea,
            MouseEvent rawEvent) {
        List<CommitDrawable> selectedCommitDrawables =
                patchSelection.findIntersectedCommits(commits);
        
        List<Commit> selectedCommits = new ArrayList<Commit>(
                selectedCommitDrawables.size());
        
        for(CommitDrawable commitDrawable : selectedCommitDrawables) {
            selectedCommits.add(commitDrawable.getCommit());
        }
        
        if(!selectedCommits.isEmpty()) {
            new PatchView().run(selectedCommits, viewModel);
        }
        
        drawables.remove(patchSelection);
        patchSelection = null;
    }
}