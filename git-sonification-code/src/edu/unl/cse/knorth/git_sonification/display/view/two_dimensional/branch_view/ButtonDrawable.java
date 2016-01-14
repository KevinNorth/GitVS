package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Camera;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Point;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;

public abstract class ButtonDrawable<WindowState> extends Drawable {
    public ButtonDrawable(Rectangle boundingRectangle, int zOrdering) {
        super(boundingRectangle, zOrdering);
    }
    
    public boolean isClicked(Point locationOfMouseClickOnScreen, Camera camera)
    {
        Rectangle buttonLocationOnScreen = getBoundingRectangle();
        
        if(!attachedToCamera()) {
            buttonLocationOnScreen =
                    camera.convertFromGridToScreen(buttonLocationOnScreen);
        }
        
        return buttonLocationOnScreen.contains(locationOfMouseClickOnScreen);
    }
    
    public abstract void onClick(WindowState windowState, float delta);
}