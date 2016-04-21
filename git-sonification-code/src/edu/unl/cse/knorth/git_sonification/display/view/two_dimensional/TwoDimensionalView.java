package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.interaction.keyboard.KeyboardEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
    private boolean isMouseBeingDragged;
    private Rectangle initialGridViewport;
    Point startOfMouseDragOnGrid;
       
    /**
     * @return A copy of the initial grid viewport. Returns a copy so that the
     * original value cannot be modified.
     */
    public Rectangle rememberInitialGridViewport() {
        return initialGridViewport.copy();
    }
    
    /**
     * @return <code>true</code> if the whole program should terminate when this
     * TwoDimensionalView is closed. <code>false</code> if the window should
     * close, but the rest of the program should continue to run.
     */
    public abstract boolean shouldProgramCloseWhenWindowIsClosed();
    
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
        
        if(!shouldProgramCloseWhenWindowIsClosed()) {
            /*
            * Set up the window so that when it's closed, it doesn't close the
            * whole program.
            *
            * Looking at the source code for Processing, it looks to me like
            * removing the pre-existing WindowListeners won't cause problems.
            * (see https://github.com/processing/processing/blob/processing-0227-2.2.1/core/src/processing/core/PApplet.java
            * Note that this.frame is a JFrame, and the only WindowListeners
            * that PApplet adds close the program when the window is closed, or
            * spit information to the console without any other side effects.)
            *
            * But there be dragons - if the window starts misbehaving, I might
            * have removed a WindowListener that did something important.
            */
           final PApplet thisPApplet = this;

           for(WindowListener windowListener : this.frame.getWindowListeners()) {
               this.frame.removeWindowListener(windowListener);
           }

           this.frame.addWindowListener(new WindowAdapter() {
               @Override
               public void windowClosed(WindowEvent e) {
                   thisPApplet.destroy();
                   e.getWindow().dispose();
               }
           });
        }
                
        size(getSetupWidth(), getSetupHeight(), P2D);
        
        initialGridViewport = getInitialGridViewport();
        Rectangle screen = new Rectangle(0, 0, width, height);
        camera = new Camera(initialGridViewport, screen);
        
        drawables = getInitialDrawables();
        backgroundColor = getInitialBackgroundColor();
        
        keyboardEvents = getInitialKeybaordEvents();
        activeKeyboardEvents = new HashSet<>();
        
        oldTime = System.currentTimeMillis();
        
        isMouseBeingDragged = false;
        startOfMouseDragOnGrid = null;
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
            Rectangle screenBoundingRectangle = drawable.getBoundingRectangle();
            
            if(!drawable.attachedToCamera()) {
                screenBoundingRectangle = camera.convertFromGridToScreen(
                    screenBoundingRectangle);
            }
            
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
    public final void mouseClicked(MouseEvent event) {
        Point screenLocation = new Point(event.getX(), event.getY());
        Point gridLocation = camera.convertFromScreenToGrid(screenLocation);
        switch(event.getButton()) {
            case PApplet.LEFT:
                handleLeftMouseButton(gridLocation, event);
                break;
            case PApplet.CENTER:
                handleMiddleMouseButton(gridLocation, event);
                break;
            case PApplet.RIGHT:
                handleRightMouseButton(gridLocation, event);
                break;
        }
    }
    
    public void handleLeftMouseButton(Point mouseLocationOnGridViewport,
            MouseEvent rawMouseEvent) {}

    public void handleMiddleMouseButton(Point mouseLocationOnGridViewport,
            MouseEvent rawMouseEvent) {}

    public void handleRightMouseButton(Point mouseLocationOnGridViewport,
            MouseEvent rawMouseEvent) {}

    
    @Override
    public final void mouseWheel(MouseEvent event) {
        int numWheelClicks = event.getCount();
        Point screenLocation = new Point(event.getX(), event.getY());
        Point gridLocation = camera.convertFromScreenToGrid(screenLocation);
        handleMouseWheel(gridLocation, numWheelClicks, event);
    }
    
    public void handleMouseWheel(Point mouseLocationOnGridViewport,
            int numWheelClicks, MouseEvent rawMouseEvent) {}

    @Override
    public final void mouseDragged(MouseEvent event) {
        Point mouseLocation = camera.convertFromScreenToGrid(
                new Point(event.getX(), event.getY()));
        
        if(!isMouseBeingDragged) {
            startOfMouseDragOnGrid = mouseLocation;
            isMouseBeingDragged = true;
        }
        
        float x1 = mouseLocation.getX();
        float y1 = mouseLocation.getY();
        float x2 = startOfMouseDragOnGrid.getX();
        float y2 = startOfMouseDragOnGrid.getY();
        
        if(x1 > x2) {
            float temp = x1;
            x1 = x2;
            x2 = temp;
        }
        
        if(y1 > y2) {
            float temp = y1;
            y1 = y2;
            y2 = temp;
        }
        
        Rectangle dragArea = new Rectangle(x1, y1, x2, y2);
        
        whileMouseDragged(mouseLocation, startOfMouseDragOnGrid, dragArea,
                event);
    }

    @Override
    public final void mouseReleased(MouseEvent event) {
        if(isMouseBeingDragged) {
            Point mouseLocation = camera.convertFromScreenToGrid(
                    new Point(event.getX(), event.getY()));
            
            float x1 = mouseLocation.getX();
            float y1 = mouseLocation.getY();
            float x2 = startOfMouseDragOnGrid.getX();
            float y2 = startOfMouseDragOnGrid.getY();

            if(x1 > x2) {
                float temp = x1;
                x1 = x2;
                x2 = temp;
            }

            if(y1 > y2) {
                float temp = y1;
                y1 = y2;
                y2 = temp;
            }

            Rectangle dragArea = new Rectangle(x1, y1, x2, y2);

            whenMouseDragReleased(mouseLocation, startOfMouseDragOnGrid,
                    dragArea, event);
        }
        
        startOfMouseDragOnGrid = null;
        isMouseBeingDragged = false;
    }

    public void whileMouseDragged(Point mouseLocationOnGridViewport,
            Point startOfDragOnGridViewport, Rectangle dragArea,
            MouseEvent rawEvent) {}
    
    public void whenMouseDragReleased(Point mouseLocationOnGridViewport,
            Point startOfDragOnGridViewport, Rectangle dragArea,
            MouseEvent rawEvent) {}
}