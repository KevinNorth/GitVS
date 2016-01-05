package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

/**
 * Represents a rectangular region in the 2d view.
 * 
 * This class always assumes that x1 &lt; x2 and y1 &lt; y2. If these
 * assumptions aren't true, the center(), intersects(), contains(), getWidth(),
 * and getHeight() methods won't return correct results.
 */
public class Rectangle {
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public Rectangle(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    public Rectangle(Point topLeft, Point bottomRight) {
        x1 = topLeft.getX();
        y1 = topLeft.getY();
        x2 = bottomRight.getX();
        y2 = bottomRight.getY();
    }
    
    public Rectangle(Point center, float height, float width) {
        x1 = center.getX() - (width / 2.0f);
        y1 = center.getY() - (height / 2.0f);
        x2 = center.getX() + (width / 2.0f);
        y2 = center.getY() + (height / 2.0f);
    }

    public Rectangle copy() {
        return new Rectangle(x1, y1, x2, y2);
    }
    
    public Point center() {
        return new Point((x1 + x2) / 2.0f, (y1 + y2) / 2.0f);
    }

    public float horizontalCenter() {
        return (x1 + x2) / 2.0f;
    }
    
    public float verticalCenter() {
        return (y1 + y2) / 2.0f;
    }
    
    public float getWidth() {
        return x2 - x1;
    }
    
    public float getHeight() {
        return y2 - y1;
    }
    
    public float getArea() {
        return getWidth() * getHeight();
    }
    
    public float getPerimeter() {
        return (getWidth() * 2) + (getHeight() * 2);
    }
    
    /**
     * Determines whether this rectangle intersects the given point.
     */
    public boolean intersects(Point point) {
        boolean isInWidth = (point.getX() <= x2) && (point.getX() >= x1);
        boolean isInHeight = (point.getY() <= y2) && (point.getY() >= y1);
        return isInWidth && isInHeight;
    }
    
    /**
     * Determines whether this rectangle intersects the given rectangle.
     */
    public boolean intersects(Rectangle otherRect) {
        // code borrowed from http://stackoverflow.com/a/13390495/473792
        if(this.x2 < otherRect.getX1()
                || otherRect.getX2() < this.x1
                || this.y2 < otherRect.getY1()
                || otherRect.getY2() < this.y1) {
            return false;
        } else {
                return true;
        }
    }

    /**
     * Determines whether this rectangle contains the given point.
     */
    public boolean contains(Point point) {
        boolean isInWidth = (point.getX() <= x2) && (point.getX() >= x1);
        boolean isInHeight = (point.getY() <= y2) && (point.getY() >= y1);
        return isInWidth && isInHeight;
    }

    /**
     * Determines whether this rectangle contains the given rectangle, so that
     * the given rectangle exists entirely within <code>this</code>.
     */
    public boolean contains(Rectangle otherRect) {
        return this.x1 <= otherRect.getX1()
                && this.x2 >= otherRect.getX2()
                && this.y1 <= otherRect.getY1()
                && this.y2 >= otherRect.getY2();
    }
    
    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Float.floatToIntBits(this.x1);
        hash = 67 * hash + Float.floatToIntBits(this.y1);
        hash = 67 * hash + Float.floatToIntBits(this.x2);
        hash = 67 * hash + Float.floatToIntBits(this.y2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rectangle other = (Rectangle) obj;
        if (Float.floatToIntBits(this.x1) != Float.floatToIntBits(other.x1)) {
            return false;
        }
        if (Float.floatToIntBits(this.y1) != Float.floatToIntBits(other.y1)) {
            return false;
        }
        if (Float.floatToIntBits(this.x2) != Float.floatToIntBits(other.x2)) {
            return false;
        }
        if (Float.floatToIntBits(this.y2) != Float.floatToIntBits(other.y2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rectangle{" + "x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + '}';
    }
}