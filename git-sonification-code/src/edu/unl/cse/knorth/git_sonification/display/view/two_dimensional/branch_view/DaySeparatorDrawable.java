package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import processing.core.PFont;
import processing.core.PGraphics;

public class DaySeparatorDrawable extends Drawable {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormat.forPattern("E, M/d/YY");
    private DateTime day;
    private PFont font;
    private int textSize;
    private Color textColor;
    private float dividerWeight;
    private Color dividerColor;
    private int numDaysPassed;

    public DaySeparatorDrawable(DateTime day, PFont font, Color textColor,
            float dividerWeight, Color dividerColor, int numDaysPassed,
            Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
        this.day = day;
        this.font = font;
        this.textSize = font.getSize();
        this.textColor = textColor;
        this.dividerWeight = dividerWeight;
        this.dividerColor = dividerColor;
        this.numDaysPassed = numDaysPassed;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor) {
        String string = DATE_FORMATTER.print(day);
        
        float zoomedTextSize = textSize * zoomFactor;
        textColor.apply(graphics);
        graphics.textAlign(PGraphics.LEFT, PGraphics.CENTER);
        graphics.textFont(font, zoomedTextSize);
        graphics.text(string, locationOnScreen.getX1(),
                locationOnScreen.getY1() + zoomedTextSize);
        
        dividerColor.apply(graphics);
        graphics.strokeWeight(dividerWeight * zoomFactor);
        graphics.line(locationOnScreen.getX1(), locationOnScreen.getY1(),
                locationOnScreen.getX2(), locationOnScreen.getY1());
    }

    @Override
    public boolean attachedToCamera() {
        return false;
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
    }

    public PFont getFont() {
        return font;
    }

    public void setFont(PFont font) {
        this.font = font;
        this.textSize = font.getSize();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public float getDividerWeight() {
        return dividerWeight;
    }

    public void setDividerWeight(float dividerWeight) {
        this.dividerWeight = dividerWeight;
    }

    public Color getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(Color dividerColor) {
        this.dividerColor = dividerColor;
    }

    public int getNumDaysPassed() {
        return numDaysPassed;
    }

    public void setNumDaysPassed(int numDaysPassed) {
        this.numDaysPassed = numDaysPassed;
    }
}