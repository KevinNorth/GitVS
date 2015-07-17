package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.joda.time.DateTime;

public class Row {
  public static final int NO_BRANCH_LOCATION = -1;

  private String author;
  private DateTime commitDate;
  private List<Line> outgoingLines;
  private RowType type;
  private int branchLocation;
  private boolean isVisible;

  /**
   * Creates a new Row that represents a commit.
   */
  public Row(String author, DateTime commitDate, Collection<Line> outgoingLines,
    int branchLocation, boolean isVisible) {
    this.author = author;
    this.commitDate = commitDate;
    this.outgoingLines = new ArrayList<Line>(outgoingLines);
    this.type = RowType.COMMIT;
    this.branchLocation = branchLocation;
    this.isVisible = isVisible;
  }

  /**
   * Creates a new Row that represents a day separator.
   */
  public Row(DateTime startOfNewDay, Collection<Line> outgoingLines) {
    this.author = null;
    this.commitDate = startOfNewDay;
    this.outgoingLines = new ArrayList<Line>(outgoingLines);
    this.type = RowType.DAY_SEPARATOR;
    this.branchLocation = NO_BRANCH_LOCATION;
    this.isVisible = false;
  }

  public String getAuthor() {
    return author;
  }

  public DateTime getCommitDate() {
    return commitDate;
  }

  public List<Line> getOutgoingLines() {
    return outgoingLines;
  }

  public RowType getType() {
    return type;
  }

  public int getBranchLocation() {
    return branchLocation;
  }
  
  public boolean isVisable(){
      return isVisible;
  }
}
