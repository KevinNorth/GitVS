package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Point;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PFont;
import processing.core.PGraphics;

/**
 * Visually represents conflicts in a project.
 */
public class ConflictAnnotationDrawable extends Drawable {
    private int numConflicts;
    private PFont font;
    private Color color;
    private float fontSize;

    public ConflictAnnotationDrawable(int numConflicts, PFont font, Color color,
            Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.numConflicts = numConflicts;
        this.font = font;
        this.color = color;
        this.fontSize = font.getSize();
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor) {
        float zoomedFontSize = fontSize * zoomFactor;
        
        color.apply(graphics);
        graphics.textFont(font, zoomedFontSize);
        
        String string;
        
        switch(numConflicts) {
            case 0:
                return; // Don't draw conflicts over commits without conflicts
            case 1:
                string = "1";
                break;
            case 2:
                string = "2";
                break;
            case 3:
                string = "3";
                break;
            default:
                string = "4+";
        }

        graphics.textAlign(PGraphics.CENTER, PGraphics.CENTER);
        Point center = locationOnScreen.center();
        graphics.text(string, center.getX(), center.getY());
        graphics.text(string, center.getX() + 1, center.getY());
        graphics.text(string, center.getX() - 1, center.getY());
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

    public PFont getFont() {
        return font;
    }

    public void setFont(PFont font) {
        this.font = font;
        this.fontSize = font.getSize();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
