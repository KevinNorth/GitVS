package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.patch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

/**
 * Draws a string to the screen. The string will always be left-aligned starting
 * at the bottom-left of its bounding rectangle. The string may go past the
 * right or top of its bounding rectangle - the drawing algorithm just spits
 * out the string without making any adjustments to be sure it fits the bounding
 * rectangle.
 */
public class VerticalTextDrawable extends Drawable {
    private String string;
    private int fontSize;
    private PFont font;
    private Color color;

    public VerticalTextDrawable(Rectangle boundingRectangle, int zOrdering,
            String string, PFont font, Color color) {
        super(boundingRectangle, zOrdering);
        this.string = string;
        this.fontSize = font.getSize();
        this.font = font;
        this.color = color;
    }

    public VerticalTextDrawable(Rectangle boundingRectangle, int zOrdering,
            String string, String fontName, int fontSize, Color color,
            PApplet context) {
        super(boundingRectangle, zOrdering);
        this.string = string;
        this.fontSize = fontSize;
        this.font = context.createFont(fontName, fontSize);
        this.color = color;
    }

        public VerticalTextDrawable(Rectangle boundingRectangle, int zOrdering,
            String string, String fontName, int fontSize, boolean smoothing,
            Color color, PApplet context) {
        super(boundingRectangle, zOrdering);
        this.string = string;
        this.fontSize = fontSize;
        this.font = context.createFont(fontName, fontSize, smoothing);
        this.color = color;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor) {
        float zoomedFontSize = fontSize * zoomFactor;
        
        graphics.pushMatrix();
        graphics.translate(locationOnScreen.getX1(), locationOnScreen.getY2());
        graphics.rotate(-PGraphics.HALF_PI);
        
        color.apply(graphics);
        graphics.textFont(font, zoomedFontSize);
        graphics.text(string, 0, 0);
        
        graphics.popMatrix();
    }

    @Override
    public boolean attachedToCamera() {
        return false;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
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