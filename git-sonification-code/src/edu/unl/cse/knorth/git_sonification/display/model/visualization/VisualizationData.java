package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.List;

public class VisualizationData {
  private final Layer combinedLayer;
  private final List<String> components;
  private final int numStart;
  private final int numEnd;

  public VisualizationData(Layer combinedLayer, List<String> components,
          int numStart, int numEnd) {
    this.combinedLayer = combinedLayer;
    this.components = components;
    this.numStart = numStart;
    this.numEnd = numEnd;
  }

  public Layer getCombinedLayer() {
    return combinedLayer;
  }

  public List<String> getComponents() {
    return components;
  }
  
  public int getNumStart() {
    return numStart;
  }
  public int getNumEnd() {
    return numEnd;
  }
}
