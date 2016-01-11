package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.patch_view;

import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Commit;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.TwoDimensionalView;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view.CommitDrawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java.TextDrawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

public class PatchView extends TwoDimensionalView<PatchViewState> {
    private List<Commit> commits;
    private ViewModel viewModel;
    
    public void run(List<Commit> commits, ViewModel viewModel) {
        String[] emptyArgs = {};
        run(commits, viewModel, emptyArgs);
    }
    
    public void run(List<Commit> commits, ViewModel viewModel,
            String args[]) {
        this.commits = commits;
        this.viewModel = viewModel;

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
        ArrayList<Drawable> drawables = new ArrayList<>(1);
        Rectangle rect = new Rectangle(0, 0, 100, 100);
        drawables.add(new TextDrawable(rect, 0, "Test", "Arial", 24,
                Color.BLACK, this));
        return drawables;
    }

    @Override
    public Color getInitialBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public ArrayList<KeyboardEvent<PatchViewState>> getInitialKeybaordEvents() {
        return new ArrayList<>();
    }

    @Override
    public void update(long delta) {
        // Do nothing
    }

    @Override
    public PatchViewState getWindowState() {
        return new PatchViewState();
    }
}