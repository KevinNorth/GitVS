package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PGraphics;

public class LineDrawable extends Drawable {
    private Direction direction;
    private Color color;
    private float strokeWeight;

    public LineDrawable(Direction direction, Color color, float strokeWeight, Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.direction = direction;
        this.color = color;
        this.strokeWeight = strokeWeight;
    }
    
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getStrokeWeight() {
        return strokeWeight;
    }

    public void setStrokeWeight(float strokeWeight) {
        this.strokeWeight = strokeWeight;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen) {
        color.apply(graphics);
        graphics.strokeWeight(strokeWeight);
        if(direction == Direction.STRAIGHT_DOWN) {
            graphics.line(locationOnScreen.horizontalCenter(),
                    locationOnScreen.getY1(),
                    locationOnScreen.horizontalCenter(),
                    locationOnScreen.getY2());
        } else {
            drawSCurve(graphics, locationOnScreen);
        }
    }
    
    private void drawSCurve(PGraphics graphics, Rectangle locationOnScreen) {
        if(direction == Direction.TOPLEFT_TO_BOTTOMRIGHT) {
        graphics.line(locationOnScreen.getX1(), locationOnScreen.getY1(),
            locationOnScreen.getX2(), locationOnScreen.getY2());
        } else {
        graphics.line(locationOnScreen.getX2(), locationOnScreen.getY1(),
            locationOnScreen.getX1(), locationOnScreen.getY2());
        }
    }
    
    public static enum Direction {
        STRAIGHT_DOWN, TOPLEFT_TO_BOTTOMRIGHT, TOPRIGHT_TO_BOTTOMLEFT;
    }
}