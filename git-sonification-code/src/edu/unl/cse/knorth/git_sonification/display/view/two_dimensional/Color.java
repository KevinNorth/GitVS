package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Represents a color using either RGB or HSB.
 */
public class Color {

    private char v1;
    private char v2;
    private char v3;
    private char alpha;
    private Mode mode;

    public static final Color BLACK = Color.createGray(Character.MIN_VALUE);
    public static final Color WHITE = Color.createGray(Character.MAX_VALUE);
    public static final Color RED = Color.createRGBColor((char) 255, (char) 0,
            (char) 0);
    public static final Color ORANGE = Color.createRGBColor((char) 255,
            (char) 127, (char) 0);
    public static final Color YELLOW = Color.createRGBColor((char) 255,
            (char) 255, (char) 0);
    public static final Color GREEN = Color.createRGBColor((char) 0, (char) 255,
            (char) 0);
    public static final Color CYAN = Color.createRGBColor((char) 0, (char) 255,
            (char) 255);
    public static final Color BLUE = Color.createRGBColor((char) 0, (char) 0,
            (char) 255);
    public static final Color PURPLE = Color.createRGBColor((char) 128,
            (char) 0, (char) 255);
    public static final Color MAGENTA = Color.createRGBColor((char) 255,
            (char) 0, (char) 255);
            
    public Color(char v1, char v2, char v3, char alpha, Mode mode) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.alpha = alpha;
        this.mode = mode;
    }

    public Color(char v1, char v2, char v3, Mode mode) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.alpha = (char) 255;
        this.mode = mode;
    }

    public static Color createRGBColor(char red, char green, char blue,
            char alpha) {
        return new Color(red, green, blue, alpha, Mode.RGB);
    }

    public static Color createRGBColor(char red, char green, char blue) {
        return new Color(red, green, blue, Mode.RGB);
    }

    public static Color createHSBColor(char hue, char saturation,
            char brightness, char alpha) {
        return new Color(hue, saturation, brightness, alpha, Mode.HSB);
    }

    public static Color createHSBColor(char hue, char saturation,
            char brightness) {
        return new Color(hue, saturation, brightness, Mode.HSB);
    }

    public static Color createGray(char brightness, char alpha) {
        return new Color(brightness, brightness, brightness, alpha, Mode.RGB);
    }

    public static Color createGray(char brightness) {
        return new Color(brightness, brightness, brightness, Mode.RGB);
    }

    /**
     * Runs commands on a Processing graphics object so that the subsequent draw
     * commands will use the color represented by <code>this</code>.
     *
     * @param graphics The PGraphics object to configure.
     */
    public void apply(PGraphics graphics) {
        setMode(graphics);
        graphics.fill(v1, v2, v3, alpha);
        graphics.stroke(v1, v2, v3, alpha);
    }

    public void applyToBackground(PGraphics graphics) {
        setMode(graphics);
        graphics.background(v1, v2, v3, alpha);
    }

    private void setMode(PGraphics graphics) {
        if (mode == Mode.RGB) {
            graphics.colorMode(PApplet.RGB, 255);
        } else {
            graphics.colorMode(PApplet.HSB, 255);
        }
    }

    public char getV1() {
        return v1;
    }

    public void setV1(char v1) {
        this.v1 = v1;
    }

    public char getV2() {
        return v2;
    }

    public void setV2(char v2) {
        this.v2 = v2;
    }

    public char getV3() {
        return v3;
    }

    public void setV3(char v3) {
        this.v3 = v3;
    }

    public char getAlpha() {
        return alpha;
    }

    public void setAlpha(char alpha) {
        this.alpha = alpha;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public static enum Mode {

        RGB, HSB;
    }
}
