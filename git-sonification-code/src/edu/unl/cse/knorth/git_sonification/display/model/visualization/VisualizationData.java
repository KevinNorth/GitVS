package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class VisualizationData {
  private List<Layer> layers;

  public VisualizationData(Collection<Layer> layers) {
    this.layers = new ArrayList<Layer>(layers);
  }

  public List<Layer> getLayers() {
    return layers;
  }
}
