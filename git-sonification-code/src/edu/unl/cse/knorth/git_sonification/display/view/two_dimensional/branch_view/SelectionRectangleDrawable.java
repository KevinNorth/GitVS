package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import java.util.ArrayList;
import java.util.List;
import processing.core.PGraphics;

public class SelectionRectangleDrawable extends Drawable {
    private Color fillColor;
    private Color borderColor;
    private float borderThickness;

    public SelectionRectangleDrawable(Color fillColor, Color borderColor,
            float borderThickness, Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor) {
        fillColor.apply(graphics);
        graphics.fill = true;
        graphics.rectMode(PGraphics.CORNERS);
        graphics.rect(locationOnScreen.getX1(), locationOnScreen.getY1(),
                locationOnScreen.getX2(), locationOnScreen.getY2());
        
        borderColor.apply(graphics);
        // Don't adjust the border thickness based on the zoom level because
        // conceptually, the SelectionRectangleDrawable is more of a UI
        // element attached to the camera than it is a part of the view that
        // the camera pans and zooms around
        graphics.strokeWeight(borderThickness);
        graphics.line(locationOnScreen.getX1(), locationOnScreen.getY1(),
                locationOnScreen.getX2(), locationOnScreen.getY1());
        graphics.line(locationOnScreen.getX1(), locationOnScreen.getY1(),
                locationOnScreen.getX1(), locationOnScreen.getY2());
        graphics.line(locationOnScreen.getX1(), locationOnScreen.getY2(),
                locationOnScreen.getX2(), locationOnScreen.getY2());
        graphics.line(locationOnScreen.getX2(), locationOnScreen.getY1(),
                locationOnScreen.getX2(), locationOnScreen.getY2());
    }

    @Override
    public boolean attachedToCamera() {
        return false;
    }
    
    public List<CommitDrawable> findIntersectedCommits(
            List<CommitDrawable> commits) {
        ArrayList<CommitDrawable> intersectedCommits = new ArrayList<>();
        
        for(CommitDrawable commit : commits) {
            if(getBoundingRectangle().intersects(commit.getBoundingRectangle()))
            {
                intersectedCommits.add(commit);
            }
        }
        
        return intersectedCommits;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public float getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(float borderThickness) {
        this.borderThickness = borderThickness;
    }
}