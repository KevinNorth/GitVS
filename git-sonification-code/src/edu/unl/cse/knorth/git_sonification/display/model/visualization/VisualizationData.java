package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import java.util.List;

public class VisualizationData {
  private final Layer combinedLayer;
  private final List<Component> components;
  private final int numStart;
  private final int numEnd;

  public VisualizationData(Layer combinedLayer, List<Component> components,
          int numStart, int numEnd) {
    this.combinedLayer = combinedLayer;
    this.components = components;
    this.numStart = numStart;
    this.numEnd = numEnd;
  }

  public Layer getCombinedLayer() {
    return combinedLayer;
  }

  public List<Component> getComponents() {
    return components;
  }
  
  public int getNumStart() {
    return numStart;
  }
  public int getNumEnd() {
    return numEnd;
  }
}
