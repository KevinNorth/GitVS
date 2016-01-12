package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PGraphics;

public class LineDrawable extends Drawable {
    private Direction direction;
    private float width;
    private Color color;

    public LineDrawable(Direction direction, float width, Color color, Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.direction = direction;
        this.width = width;
        this.color = color;
    }

    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen, float zoomFactor) {
        color.apply(graphics);
        graphics.strokeWeight(width * zoomFactor);
        
        if(direction == Direction.TOPLEFT_TO_BOTTOMRIGHT) {
            graphics.line(locationOnScreen.getX1(), locationOnScreen.getY1(),
                    locationOnScreen.getX2(), locationOnScreen.getY2());
        } else {
            graphics.line(locationOnScreen.getX1(), locationOnScreen.getY2(),
                    locationOnScreen.getX2(), locationOnScreen.getY1());
        }
    }
    
    public static enum Direction {
        TOPRIGHT_TO_BOTTOMLEFT, TOPLEFT_TO_BOTTOMRIGHT;
    }
    
}