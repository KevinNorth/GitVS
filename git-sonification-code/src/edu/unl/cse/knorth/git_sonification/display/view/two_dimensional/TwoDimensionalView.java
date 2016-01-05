package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import processing.core.PApplet;
import processing.event.MouseEvent;

public abstract class TwoDimensionalView<WindowState> extends PApplet {
    protected Camera camera;
    protected ArrayList<Drawable> drawables;
    protected Color backgroundColor;
    protected ArrayList<KeyboardEvent<WindowState>> keyboardEvents;
    private Set<KeyboardEvent<WindowState>> activeKeyboardEvents;
    private long oldTime;
        
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
     * Gets the keyboard events that should be bound starting on the first
     * frame. Keyboard events can be added, removed, and rebound on future
     * frames by updating the <code>keyboardEvents</code> variable. (If you
     * remove a keyboard event while its button is held down, it will continue
     * to run its <code>whileKeyHeld()</code> and <code>onKeyUp()</code> methods
     * until the key is released, at which point the keyboard event will truly
     * be removed.)
     */
    public abstract ArrayList<KeyboardEvent<WindowState>>
            getInitialKeybaordEvents();
    
    /**
     * Runs once per frame before drawing and after handling held key events.
     * <p/>
     * Since the <code>drawables</code> member variable is of protected
     * visibility, you can directly modify it in this method. Don't worry about
     * having the <code>Drawable</code>s in the list in any particular order -
     * the list will be sorted after this method returns and before drawing.
     * 
     * @param delta The amount of time, in milliseconds, since the previous
     * frame.
     */
    public abstract void update(long delta);
    
    /**
     * Gets the current state of the window. This is used to provide
     * KeyboardEvents and the mouse event handlers with the information they can
     * read and manipulate in order to implement their behaviors.
     */
    public abstract WindowState getWindowState();
    
    @Override
    public final void setup() {
        initialize();
        
        size(getSetupWidth(), getSetupHeight(), P2D);
        
        Rectangle grid = getInitialGridViewport();
        Rectangle screen = new Rectangle(0, 0, width, height);
        camera = new Camera(grid, screen);
        
        drawables = getInitialDrawables();
        backgroundColor = getInitialBackgroundColor();
        
        keyboardEvents = getInitialKeybaordEvents();
        activeKeyboardEvents = new HashSet<>();
        
        oldTime = System.currentTimeMillis();
    }
        
    @Override
    public final void draw() {
        long time = System.currentTimeMillis();
        long delta = time - oldTime;
        oldTime = time;
        
        for(KeyboardEvent<WindowState> keyboardEvent : activeKeyboardEvents) {
            keyboardEvent.whileKeyHeld(getWindowState(), delta);
        }
        
        update(delta);
  
        backgroundColor.applyToBackground(g);
        Collections.sort(drawables);
        
        for(Drawable drawable : drawables) {
            Rectangle screenBoundingRectangle = camera.convertFromGridToScreen(
                    drawable.getBoundingRectangle());
            
            if(screenBoundingRectangle.intersects(camera.getScreenViewport())) {
                drawable.draw(g, screenBoundingRectangle,
                        camera.calculateZoomFactor());
            }
        }
    }
    
    @Override
    public final void keyPressed() {
        for(KeyboardEvent<WindowState> keyboardEvent : keyboardEvents) {
            if(keyboardEvent.respondsToKey(key, keyCode)) {
                keyboardEvent.onKeyDown(getWindowState());
                activeKeyboardEvents.add(keyboardEvent);
            }
        }
    }
    
    @Override
    public final void keyReleased() {
        ArrayList<KeyboardEvent<WindowState>> eventsToRemove
                = new ArrayList<>();
                
        for(KeyboardEvent<WindowState> keyboardEvent : activeKeyboardEvents) {
            if(keyboardEvent.respondsToKey(key, keyCode)) {
                keyboardEvent.onKeyUp(getWindowState());
                eventsToRemove.add(keyboardEvent);
            }
        }

        activeKeyboardEvents.removeAll(eventsToRemove);
    }
    
    @Override
    public final void mouseWheel(MouseEvent event) {
        int numWheelClicks = event.getCount();
        Point screenLocation = new Point(event.getX(), event.getY());
        Point gridLocation = camera.convertFromScreenToGrid(screenLocation);
        handleMouseWheel(getWindowState(), gridLocation, numWheelClicks, event);
    }
    
    public void handleMouseWheel(WindowState windowState,
            Point mouseLocationOnGridViewport, int numWheelClicks,
            MouseEvent rawMouseEvent) {}
}