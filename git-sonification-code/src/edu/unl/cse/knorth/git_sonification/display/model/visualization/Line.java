package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import java.util.HashSet;
import java.util.Set;

public class Line {
  public int fromBranch;
  public int toBranch;
  public boolean isVisible;
  public Set<Component> modifiedComponents;

  public Line(int fromBranch, int toBranch, boolean isVisible) {
    this.fromBranch = fromBranch;
    this.toBranch = toBranch;
    this.isVisible = isVisible;
    this.modifiedComponents = new HashSet<>();
  }

    @Override
    public String toString() {
        return "Line{" + "fromBranch=" + fromBranch +
                ", toBranch=" + toBranch +
                ", isVisible=" + isVisible + '}';
    }
}
