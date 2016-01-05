package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

import processing.core.PGraphics;

public abstract class Drawable implements Comparable<Drawable> {
    private Rectangle boundingRectangle;
    private int zOrdering;

    /**
     * @param boundingRectangle The location of the drawable relative to other
     * Drawables. These should be coordinates on the arbitrary grid that the
     * Camera transforms, not the actual screen coordinates.
     * @param zOrdering A value used to determine which other Drawables this one
     * should be drawn on top of or behind. Drawables with lower zOrderings are
     * drawn before Drawables with larger zOrderings.
     */
    public Drawable(Rectangle boundingRectangle, int zOrdering) {
        this.boundingRectangle = boundingRectangle;
        this.zOrdering = zOrdering;
    }
    
    /**
     * Implements the logic to draw the drawable to the screen.
     * @param graphics The <code>PGraphics</code> to use to draw to the screen.
     * @param locationOnScreen The bounding rectangle, in coordinates on the
     * screen. This rectangle will likely be different than
     * <code>this.boundingRectangle</code> because
     * <code>this.boundingRectangle</code> is the location of the drawable on
     * an arbitrary coordinate system used just to place drawables relative to
     * each other, whereas <code>locationOnScreen</code> corresponds directly to
     * pixels on the window being drawn to.
     * @param zoomFactor How far the camera is zoomed in or out. For some
     * elements of a drawing, such as line thickness or font size, a Drawable
     * should change these elements' values according to the zoom factor.
     */
    public abstract void draw(PGraphics graphics, Rectangle locationOnScreen,
            float zoomFactor);

    public float adjustZoomFactorFor2dShapes(float zoomFactor) {
        return (float) Math.sqrt(zoomFactor);
//        if(zoomFactor == 1.0f) {
//            return 1.0f;
//        }
//        
//        boolean grow = zoomFactor > 1;
//        float changeAmount;
//        if(grow) {
//            changeAmount = zoomFactor - 1.0f;
//        } else {
//            changeAmount = 1.0f - zoomFactor;
//        }
//        
//        float newChangeAmount = (float) Math.sqrt(changeAmount);
//        
//        if(grow) {
//            return 1.0f + newChangeAmount;
//        } else {
//            return 1.0f - newChangeAmount;
//        }
    }
    
    public Rectangle getBoundingRectangle() {
        return boundingRectangle;
    }

    public void setBoundingRectangle(Rectangle boundingRectangle) {
        this.boundingRectangle = boundingRectangle;
    }

    public int getZOrdering() {
        return zOrdering;
    }

    public void setZOrdering(int zOrdering) {
        this.zOrdering = zOrdering;
    }

    /**
     * Orders Drawables based on their zOrderings. This makes it easy to put
     * Drawables into an ordered collection and have them appear in the order
     * they should be drawn to the screen.
     * <p/>
     * Note that equality for Drawables is determined by identity, so this
     * ordering is inconsistent with equality.
     * 
     * @param other The Drawable to compare to
     * @return -1, 0, or 1 depending on whether <code>this</code> has a lower,
     * equal, or higher zOrdering. (Drawables with lower zOrderings should be
     * drawn first.)
     */
    @Override
    public int compareTo(Drawable other) {
        if(this.zOrdering < other.zOrdering) {
            return -1;
        } else if(this.zOrdering > other.zOrdering) {
            return 1;
        } else {
            return 0;
        }
    }
}