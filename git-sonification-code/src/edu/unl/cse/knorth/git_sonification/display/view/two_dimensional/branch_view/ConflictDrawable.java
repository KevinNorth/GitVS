package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PGraphics;

/**
 * Visually represents conflicts in a project.
 */
public class ConflictDrawable extends Drawable {
    private Color color;

    public ConflictDrawable(Color color, Rectangle boundingRectangle,
            int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.color = color;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor) {
        color.apply(graphics);
        graphics.ellipseMode(PGraphics.CENTER);
        graphics.fill = false;
        graphics.strokeWeight = 10f * zoomFactor;

        final float centerX = locationOnScreen.center().getX();
        final float centerY = locationOnScreen.center().getY();
                    
        float diameter = locationOnScreen.getWidth();
            
            graphics.ellipse(centerX, centerY, diameter, diameter);
        }

    @Override
    public boolean attachedToCamera() {
        return false;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
