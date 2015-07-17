package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class VisualizationData {
  private List<Layer> layers;
  private int numStart;
  private int numEnd;

  public VisualizationData(Collection<Layer> layers, int numStart, int numEnd) {
    this.layers = new ArrayList<Layer>(layers);
    this.numStart = numStart;
    this.numEnd = numEnd;
  }

  public List<Layer> getLayers() {
    return layers;
  }
  public int getNumStart() {
    return numStart;
  }
  public int getNumEnd() {
    return numEnd;
  }
}
