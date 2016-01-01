package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

import java.util.ArrayList;
import java.util.Collections;
import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class TwoDimensionalView extends PApplet {
    protected Camera camera;
    protected ArrayList<Drawable> drawables;
    protected Color backgroundColor;
        
    /**
     * Called at the very beginning of <code>setup()</code> giving your
     * implementation a chance to set its state before the view is rendered.
     * <p/>
     * This method is called before <code>this.size()</code>, so using the
     * <code>width</code> and <code>height</code> variables is <b>NOT</b>
     * safe in <code>initialize()</code>.
     */
    public abstract void initialize();
    
    /**
     * Gets what the width of the window should be when it's initially set up.
     * @return What the width of the window should be.
     */
    public abstract int getSetupWidth();

    /**
     * Gets what the height of the window should be when it's initially set up.
     * @return What the height of the window should be.
     */
    public abstract int getSetupHeight();
    
    /**
     * Gets the coordinates of the camera's grid viewport to start with.
     * <p/>
     * This method is called after the <code>PApplet.size()</code> method is
     * called, so you can safely use the <code>width</code> and
     * <code>height</code> variables.
     * @return A rectangle indicating the region of the gird that the camera
     * initially shows.
     */
    public abstract Rectangle getInitialGridViewport();

    /**
     * Gets the Drawables that should be present on the screen as of the first
     * iteration.
     * @return A list of Drawables that should be drawn starting on the first
     * frame.
     */
    public abstract ArrayList<Drawable> getInitialDrawables();
    
    /**
     * Gets the value that should be used for the background color in drawing
     * for the first frame. You can (but don't need to) change the background
     * color in the <code>update()</code> function in subsequent frames -
     * <code>backgroundColor</code> is of protected visibility.
     */
    public abstract Color getInitialBackgroundColor();    
    /**
     * Runs once per frame before drawing.
     * <p/>
     * Since the <code>drawables</code> member variable is of protected
     * visibility, you can directly modify it in this method. Don't worry about
     * having the <code>Drawable</code>s in the list in any particular order -
     * the list will be sorted after this method returns and before drawing.
     */
    public abstract void update();
    
    @Override
    public void setup() {
        initialize();
        
        size(getSetupWidth(), getSetupHeight(), P2D);
        
        Rectangle grid = getInitialGridViewport();
        Rectangle screen = new Rectangle(0, 0, width, height);
        camera = new Camera(grid, screen);
        
        drawables = getInitialDrawables();
        backgroundColor = getInitialBackgroundColor();
    }
        
    @Override
    public void draw() {
        update();
  
        backgroundColor.applyToBackground(g);
        Collections.sort(drawables);
        
        for(Drawable drawable : drawables) {
            Rectangle screenBoundingRectangle = camera.convertFromGridToScreen(
                    drawable.getBoundingRectangle());
            
            if(screenBoundingRectangle.intersects(camera.getScreenViewport())) {
                drawable.draw(g, screenBoundingRectangle);
            }
        }
    }
}