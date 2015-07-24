package edu.unl.cse.knorth.git_sonification.display.model.visualization;

public class Line {
  public int fromBranch;
  public int toBranch;
  public boolean isVisible;

  public Line(int fromBranch, int toBranch, boolean isVisible) {
    this.fromBranch = fromBranch;
    this.toBranch = toBranch;
    this.isVisible = isVisible;
  }

    @Override
    public String toString() {
        return "Line{" + "fromBranch=" + fromBranch +
                ", toBranch=" + toBranch +
                ", isVisible=" + isVisible + '}';
    }
}
