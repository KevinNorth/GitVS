package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

/**
 * Draws a string to the screen. The string will always be left-aligned starting
 * at the bottom-left of its bounding rectangle. The string may go past the
 * right or top of its bounding rectangle - the drawing algorithm just spits out
 * the string without making any adjustments to be sure it fits the bounding
 * rectangle.
 */
public class TextDrawable extends Drawable {
    private String string;
    private PFont font;
    private Color color;

    public TextDrawable(Rectangle boundingRectangle, int zOrdering,
            String string, PFont font, Color color) {
        super(boundingRectangle, zOrdering);
        this.string = string;
        this.font = font;
        this.color = color;
    }

    public TextDrawable(Rectangle boundingRectangle, int zOrdering,
            String string, String fontName, int fontSize, Color color,
            PApplet context) {
        super(boundingRectangle, zOrdering);
        this.string = string;
        this.font = context.createFont(fontName, fontSize);
        this.color = color;
    }

        public TextDrawable(Rectangle boundingRectangle, int zOrdering,
            String string, String fontName, int fontSize, boolean smoothing,
            Color color, PApplet context) {
        super(boundingRectangle, zOrdering);
        this.string = string;
        this.font = context.createFont(fontName, fontSize, smoothing);
        this.color = color;
    }
    
    @Override
    public void draw(PGraphics graphics, Rectangle locationOnScreen) {
        color.apply(graphics);
        graphics.textFont(font);
        graphics.text(string, locationOnScreen.getX1(),
                locationOnScreen.getY2());
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
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}