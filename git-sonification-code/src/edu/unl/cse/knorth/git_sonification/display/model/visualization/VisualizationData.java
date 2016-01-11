package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class VisualizationData {
  private final List<Layer> layers;
  private final Layer combinedLayer;
  private final List<Component> components;
  private final int numStart;
  private final int numEnd;

  public VisualizationData(Collection<Layer> layers, Layer combinedLayer,
          List<Component> components, int numStart, int numEnd) {
    this.layers = new ArrayList<>(layers);
    this.combinedLayer = combinedLayer;
    this.components = components;
    this.numStart = numStart;
    this.numEnd = numEnd;
  }

  public List<Layer> getLayers() {
    return layers;
  }
  
  /**
   * Gets a List of Layers that have at least one visible commit, filtering out
   * all Layers that do not have any visible commits. Be aware that this method
   * recomputes the list every time it is called, which takes O(n) time when
   * there are n Layers in this VisualizationData.
   * @return A List of Layers that have at least one visible commit
   */
  public List<Layer> getVisibleLayers() {
      List<Layer> visibleLayers = new ArrayList<>();
      
      for(Layer layer : layers) {
          if(layer.hasVisibleCommits()) {
              visibleLayers.add(layer);
          }
      }
      
      return visibleLayers;
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
