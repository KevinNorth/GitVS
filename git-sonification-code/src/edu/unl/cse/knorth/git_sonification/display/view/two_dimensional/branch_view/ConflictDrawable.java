package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PGraphics;

/**
 * Visually represents conflicts in a project.
 */
public class ConflictDrawable extends Drawable {
    private int numConflicts;
    private Color color;

    public ConflictDrawable(int numConflicts, Color color, Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.numConflicts = numConflicts;
        this.color = color;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen, float zoomFactor) {
        float widthOfRectangle = locationOnScreen.getWidth() / 8;

        color.apply(graphics);
        graphics.ellipseMode(PGraphics.CENTER);
        graphics.fill = false;
        graphics.strokeWeight = 4f * zoomFactor;

        final float centerX = locationOnScreen.center().getX();
        final float centerY = locationOnScreen.center().getY();
        
        for(int conflict = 0; conflict < numConflicts; conflict++) {
            
            float radius = locationOnScreen.getWidth() - (conflict * 6f);
            
            graphics.ellipse(centerX, centerY, radius * 2, radius * 2);
        }
    }

    @Override
    public boolean attachedToCamera() {
        return false;
    }

    public int getNumConflicts() {
        return numConflicts;
    }

    public void setNumConflicts(int numConflicts) {
        this.numConflicts = numConflicts;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
