package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

import processing.core.PApplet;
import processing.core.PFont;

public class TwoDimensionalView extends PApplet {
    private Camera camera;
    
    public static void main(String args[]) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = TwoDimensionalView.class.getCanonicalName();
        System.arraycopy(args, 0, newArgs, 1, args.length);

        PApplet.main(newArgs);
    }
    
    @Override
    public void setup() {
        size(displayWidth / 2, displayHeight / 2, P2D);
        
        Rectangle grid = new Rectangle(width, height, width * 2, height * 2);
        Rectangle screen = new Rectangle(0, 0, width, height);
        camera = new Camera(grid, screen);
    }
    
    @Override
    public void draw() {
        background(0, 0, 0);
        PFont f = createFont("Arial",36,true);
        textFont(f);
        fill(255);
        text("Screen: (" + mouseX + ", " + mouseY + ")", 10, 100);
        Point gridPoint = camera.convertFromScreenToGrid(new Point(mouseX, mouseY));
        text("Grid: (" + gridPoint.getX() + ", " + gridPoint.getY() + ")", 10, 150);
    }
}