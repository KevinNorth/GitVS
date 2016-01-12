package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.patch_view;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Camera;

public class PatchViewState {
    private final Camera camera;

    public PatchViewState(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }
}