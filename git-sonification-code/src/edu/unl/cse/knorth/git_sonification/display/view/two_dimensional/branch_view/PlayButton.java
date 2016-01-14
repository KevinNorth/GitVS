package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PGraphics;
import processing.core.PImage;

public class PlayButton extends ButtonDrawable<BranchViewState> {
    private PlayDirection playDirection;
    private float speed;
    private PImage image;

    public PlayButton(PlayDirection playDirection, float speed, PImage image,
            Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.playDirection = playDirection;
        this.speed = speed;
        this.image = image;
    }
    
    @Override
    public void onClick(BranchViewState state, float delta) {
        Rectangle gridViewport = state.getCamera().getGridViewport();
        
        final float changePerSecond = speed;
        final float seconds = delta / 1000f;
        final float overallChange = changePerSecond * seconds;
        
        SonificationCursorDrawable sonificationCursor =
                state.getSonificationCursor();
        
        if(playDirection == PlayDirection.FORWARDS) {
            sonificationCursor.moveVertically(overallChange);
        } else {
            sonificationCursor.moveVertically(-overallChange);
        }
    }

    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor) {
        graphics.imageMode(PGraphics.CORNERS);
        graphics.image(image, locationOnScreen.getX1(),
                locationOnScreen.getY1(), locationOnScreen.getX2(),
                locationOnScreen.getY2());
    }

    @Override
    public boolean attachedToCamera() {
        return true;
    }

    public PlayDirection getPlayDirection() {
        return playDirection;
    }

    public void setPlayDirection(PlayDirection playDirection) {
        this.playDirection = playDirection;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public PImage getImage() {
        return image;
    }

    public void setImage(PImage image) {
        this.image = image;
    }
    
    public static enum PlayDirection {
        FORWARDS, REVERSE;
    }
}