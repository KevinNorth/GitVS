package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PGraphics;

/**
 * Visually represents conflicts in a project.
 */
public class ConflictMarginDrawable extends Drawable {
    private int numConflicts;
    private Color color;

    public ConflictMarginDrawable(int numConflicts, Color color,
            Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.numConflicts = numConflicts;
        this.color = color;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen, float zoomFactor) {
        float widthOfRectangle = locationOnScreen.getWidth() / 8;
        color.apply(graphics);
        graphics.rectMode(PGraphics.CORNERS);
        graphics.strokeWeight = 1.0f;
        
        for(int conflict = 0; conflict < numConflicts
                ;//&& conflict < 4; // same limitation as the conflict sonificaiton
                conflict++) {
            float x1 = locationOnScreen.getX1() +
                    (widthOfRectangle * conflict * 2);
            float x2 = x1 + widthOfRectangle;
            
            float y1 = locationOnScreen.getY1();
            float y2 = locationOnScreen.getY2();
            
            graphics.rect(x1, y1, x2, y2);
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
