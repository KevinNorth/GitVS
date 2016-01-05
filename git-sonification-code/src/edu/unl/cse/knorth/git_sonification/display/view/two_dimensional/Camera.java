package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional;

public class Camera {
    private Rectangle gridViewport;
    private Rectangle screenViewport;
    private final Rectangle initalGridViewport;

    public Camera(Rectangle gridViewport, Rectangle screenViewport) {
        this.gridViewport = gridViewport;
        this.initalGridViewport = gridViewport.copy();
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
    
    public void zoomFromInitialProportions(float zoomFactor, Point toZoomFrom) {
        final float currentProportionLeftOfToZoomFrom =
                (toZoomFrom.getX() - gridViewport.getX1())
                    / gridViewport.getWidth();
        final float currentProportionAboveToZoomFrom =
                (toZoomFrom.getY() - gridViewport.getY1())
                    / gridViewport.getHeight();
        
        final float originalWidth = initalGridViewport.getWidth();
        final float originalHeight = initalGridViewport.getHeight();
        
        final float newWidth = originalWidth * zoomFactor;
        final float newHeight = originalHeight * zoomFactor;

        final float newDistanceLeftOfToZoomFrom =
                newWidth * currentProportionLeftOfToZoomFrom;
        final float newDistanceAboveToZoomFrom =
                newHeight * currentProportionAboveToZoomFrom;
        
        final float newX1 = toZoomFrom.getX() - newDistanceLeftOfToZoomFrom;
        final float newY1 = toZoomFrom.getY() - newDistanceAboveToZoomFrom;
        final float newX2 = toZoomFrom.getX()
                + (newWidth - newDistanceLeftOfToZoomFrom);
        final float newY2 = toZoomFrom.getY()
                + (newHeight - newDistanceAboveToZoomFrom);
        
        gridViewport.setX1(newX1);
        gridViewport.setY1(newY1);
        gridViewport.setX2(newX2);
        gridViewport.setY2(newY2);
    }
    
    public void zoomFromCurrentView(float zoomFactor, Point toZoomFrom) {
        final float x1Distance = gridViewport.getX1() - toZoomFrom.getX();
        final float y1Distance = gridViewport.getY1() - toZoomFrom.getY();
        final float x2Distance = gridViewport.getX2() - toZoomFrom.getX();
        final float y2Distance = gridViewport.getY2() - toZoomFrom.getY();
        
        final float newX1 = (x1Distance * zoomFactor) + toZoomFrom.getX();
        final float newY1 = (y1Distance * zoomFactor) + toZoomFrom.getY();
        final float newX2 = (x2Distance * zoomFactor) + toZoomFrom.getX();
        final float newY2 = (y2Distance * zoomFactor) + toZoomFrom.getY();
        
        gridViewport.setX1(newX1);
        gridViewport.setY1(newY1);
        gridViewport.setX2(newX2);
        gridViewport.setY2(newY2);
    }

    /**
     * Calculates how far the camera is zoomed in or out compared to when it was
     * first initialized.
     * <p/>
     * To calculate this, the camera divides the area of the
     * gridViewport into the area the gridViewport had when it was first
     * initialized. This still produces a value when the gridViewport has
     * different proportions than when it was first created. Whether this value
     * is meaningful is something you'll need to decide as the developer.
     */
    public float calculateZoomFactor() {
        return initalGridViewport.getArea() / gridViewport.getArea();
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