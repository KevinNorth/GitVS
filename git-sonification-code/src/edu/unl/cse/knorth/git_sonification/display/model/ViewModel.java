package edu.unl.cse.knorth.git_sonification.display.model;

import edu.unl.cse.knorth.git_sonification.display.model.sonification.SonificationData;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;

/**
 * Holds all of the information needed to render the GitVS visualization
 * and sonification. The GitVS Git parser will produce an instance of this class
 * as output.
 */
public class ViewModel {
  private VisualizationData visualizationData;
  private SonificationData sonificationData;

  public ViewModel(VisualizationData visualizationData,
    SonificationData sonificationData) {
      this.visualizationData = visualizationData;
      this.sonificationData = sonificationData;
  }

  public VisualizationData getVisualizationData() {
    return visualizationData;
  }

  public SonificationData getSonificiationData() {
    return sonificationData;
  }
}
