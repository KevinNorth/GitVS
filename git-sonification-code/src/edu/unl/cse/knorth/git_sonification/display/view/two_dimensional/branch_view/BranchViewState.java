package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Camera;

public class BranchViewState {
    private Camera camera;
    private SonificationCursorDrawable sonificationCursor;

    public BranchViewState(Camera camera, SonificationCursorDrawable sonificationCursor) {
        this.camera = camera;
        this.sonificationCursor = sonificationCursor;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public SonificationCursorDrawable getSonificationCursor() {
        return sonificationCursor;
    }

    public void setSonificationCursor(SonificationCursorDrawable sonificationCursor) {
        this.sonificationCursor = sonificationCursor;
    }
}