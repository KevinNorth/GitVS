package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.experimental;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Point;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.TwoDimensionalView;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java.TextDrawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PFont;

public class ExperimentalView extends TwoDimensionalView<ExperimentalViewState> {
    private TextDrawable screenLocationTextDrawable;
    private TextDrawable gridLocationTextDrawable;
    
    public static void main(String args[]) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = ExperimentalView.class.getCanonicalName();
        System.arraycopy(args, 0, newArgs, 1, args.length);

        PApplet.main(newArgs);
    }

    @Override
    public void initialize() {
        // Nothing to do.
    }
    
    @Override
    public int getSetupWidth() {
        return displayWidth / 2;
    }

    @Override
    public int getSetupHeight() {
        return displayHeight / 2;
    }

    @Override
    public Rectangle getInitialGridViewport() {
        return new Rectangle(0, 0, width * 2, height * 2);
    }

    @Override
    public ArrayList<Drawable> getInitialDrawables() {
        PFont font = createFont("Arial", 36);
        Color textColor = Color.BLACK;
        
        screenLocationTextDrawable =
                new TextDrawable(new Rectangle(100, 100, 150, 150), 0, "", font,
                        textColor);
        gridLocationTextDrawable =
                new TextDrawable(new Rectangle(100, 200, 150, 250), 0, "", font,
                        textColor);
        
        ArrayList<Drawable> drawables = new ArrayList<>(2);
        drawables.add(screenLocationTextDrawable);
        drawables.add(gridLocationTextDrawable);
        return drawables;
    }

    @Override
    public Color getInitialBackgroundColor() {
        return Color.WHITE;
    }
    
    @Override
    public void update(long delta) {
        String screenLocationText =
                "Screen location: (" + mouseX + ", " + mouseY + ")";
        Point gridLocation =
                camera.convertFromScreenToGrid(new Point(mouseX, mouseY));
        String gridLocationText =
                "Grid Location: (" + gridLocation.getX() + ", "
                + gridLocation.getY() + ")";
        
        screenLocationTextDrawable.setString(screenLocationText);
        gridLocationTextDrawable.setString(gridLocationText);
    }

    @Override
    public ArrayList<KeyboardEvent<ExperimentalViewState>> getInitialKeybaordEvents() {
        return new ArrayList<>(0);
    }

    @Override
    public ExperimentalViewState getWindowState() {
        return new ExperimentalViewState();
    }
}