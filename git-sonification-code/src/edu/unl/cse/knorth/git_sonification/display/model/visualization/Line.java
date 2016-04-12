package edu.unl.cse.knorth.git_sonification.display.model.visualization;

public class Line {
  public int fromBranch;
  public int toBranch;
  public int color;

  public Line(int fromBranch, int toBranch, int color) {
    this.fromBranch = fromBranch;
    this.toBranch = toBranch;
    this.color = color;
  }

    @Override
    public String toString() {
        return "Line{" + "fromBranch=" + fromBranch + ", toBranch=" + toBranch
                + ", color=" + color + '}';
    }
}
