package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PGraphics;

public class SonificationCursorDrawable extends Drawable {
    private Color color;

    public SonificationCursorDrawable(Color color, Rectangle boundingRectangle,
            int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.color = color;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor) {
        color.apply(graphics);
        graphics.rectMode(PGraphics.CORNERS);
        graphics.rect(locationOnScreen.getX1(), locationOnScreen.getY1(),
                locationOnScreen.getX2(), locationOnScreen.getY2());
    }

    @Override
    public boolean attachedToCamera() {
        return false;
    }

    public void moveVertically(float distanceToMove) {
        Rectangle boundingRectangle = getBoundingRectangle();
        
        boundingRectangle.setY1(boundingRectangle.getY1() + distanceToMove);
        boundingRectangle.setY2(boundingRectangle.getY2() + distanceToMove);
    }
    
    public void setVerticalLocation(float newYPos) {
        Rectangle boundingRectangle = getBoundingRectangle();

        final float height = boundingRectangle.getHeight();
        
        boundingRectangle.setY1(newYPos - (height / 2f));
        boundingRectangle.setY2(newYPos + (height / 2f));
    }
    
    public CommitDrawable
        findCollidingCommit(Iterable<CommitDrawable> commits) {
        for(CommitDrawable commit : commits) {
            if(doesCollidesWithCommit(commit)) {
                return commit;
            }
        }
        
        return null;
    }
    
    public boolean doesCollidesWithCommit(CommitDrawable commit) {
        return getBoundingRectangle().intersects(commit.getBoundingRectangle());
    }
}