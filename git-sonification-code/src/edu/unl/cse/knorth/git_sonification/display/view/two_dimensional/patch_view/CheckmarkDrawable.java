package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.patch_view;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Commit;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PGraphics;

public class CheckmarkDrawable extends Drawable {
    private Color color;
    private Commit commit;
    private Component component;

    public CheckmarkDrawable(Color color, Commit commit, Component component, Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.color = color;
        this.commit = commit;
        this.component = component;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor) {
        color.apply(graphics);
        graphics.ellipseMode(PGraphics.CORNERS);
        graphics.ellipse(locationOnScreen.getX1(), locationOnScreen.getY1(),
                locationOnScreen.getX2(), locationOnScreen.getY2());
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

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }    
}