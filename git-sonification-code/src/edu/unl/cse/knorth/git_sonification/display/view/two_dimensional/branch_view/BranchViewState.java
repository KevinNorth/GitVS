package edu;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Camera;

public class BranchViewState {
    private Camera camera;
    
    public BranchViewState(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}