package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

public class Camera {
    private Rectangle gridViewport;
    private Rectangle screenViewport;

    public Camera(Rectangle gridViewport, Rectangle screenViewport) {
        this.gridViewport = gridViewport;
        this.screenViewport = screenViewport;
    }

    public Point convertFromGridToScreen(Point point) {
        float widthProportion = (point.getX() - gridViewport.getX1())
                / gridViewport.getWidth();
        float heightProportion = (point.getY() - gridViewport.getY1())
                / gridViewport.getHeight();
        
        float screenX = screenViewport.getX1()
                + (screenViewport.getWidth() * widthProportion);
        float screenY = screenViewport.getY1()
                + (screenViewport.getHeight()* heightProportion);
        
        return new Point(screenX, screenY);
    }
    
    public Rectangle convertFromGridToScreen(Rectangle rectangle) {
        Point gridTopLeft = new Point(rectangle.getX1(), rectangle.getY1());
        Point gridBottomRight = new Point(rectangle.getX2(), rectangle.getY2());
        
        Point screenTopLeft = convertFromGridToScreen(gridTopLeft);
        Point screenBottomRight = convertFromGridToScreen(gridBottomRight);
        
        return new Rectangle(screenTopLeft, screenBottomRight);
    }
    
    public Point convertFromScreenToGrid(Point point) {
        float widthProportion = (point.getX() - screenViewport.getX1())
                / screenViewport.getWidth();
        float heightProportion = (point.getY() - screenViewport.getY1())
                / screenViewport.getHeight();
        
        float gridX = gridViewport.getX1()
                + (gridViewport.getWidth() * widthProportion);
        float gridY = gridViewport.getY1()
                + (gridViewport.getHeight()* heightProportion);
        
        return new Point(gridX, gridY);
    }
    
    public Rectangle convertFromScreenToGrid(Rectangle rectangle) {
        Point screenTopLeft = new Point(rectangle.getX1(), rectangle.getY1());
        Point screenBottomRight = new Point(rectangle.getX2(), rectangle.getY2());
        
        Point gridTopLeft = convertFromScreenToGrid(screenTopLeft);
        Point gridBottomRight = convertFromScreenToGrid(screenBottomRight);
        
        return new Rectangle(gridTopLeft, gridBottomRight);
    }
    
    public Rectangle getGridViewport() {
        return gridViewport;
    }

    public void setGridViewport(Rectangle gridViewport) {
        this.gridViewport = gridViewport;
    }

    public Rectangle getScreenViewport() {
        return screenViewport;
    }

    public void setScreenViewport(Rectangle screenViewport) {
        this.screenViewport = screenViewport;
    }
}