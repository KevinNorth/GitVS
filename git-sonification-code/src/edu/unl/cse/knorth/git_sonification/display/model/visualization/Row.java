package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.joda.time.DateTime;

public class Row {
  public static final int NO_BRANCH_LOCATION = -1;

  private String author;
  private DateTime commitDate;
  private List<Line> incomingLines;
  private RowType type;
  private int branchLocation;
  private boolean isVisible;

  /**
   * Creates a new Row that represents a commit.
   */
  public Row(String author, DateTime commitDate, int branchLocation,
    boolean isVisible, Collection<Line> incomingLines) {
    this.author = author;
    this.commitDate = commitDate;
    this.type = RowType.COMMIT;
    this.branchLocation = branchLocation;
    this.isVisible = isVisible;
    this.incomingLines = new ArrayList<>(incomingLines);
  }

  /**
   * Creates a new Row that represents a commit.
   */
  public Row(String author, DateTime commitDate, int branchLocation,
    boolean isVisible, Line... incomingLines) {
      this(author, commitDate, branchLocation, isVisible,
              Arrays.asList(incomingLines));
  }
  
  /**
   * Creates a new Row that represents a day separator.
   */
  public Row(DateTime startOfNewDay, Collection<Line> incomingLines) {
    this.author = null;
    this.commitDate = startOfNewDay;
    this.incomingLines = new ArrayList<>(incomingLines);
    this.type = RowType.DAY_SEPARATOR;
    this.branchLocation = NO_BRANCH_LOCATION;
    this.isVisible = false;
  }

    /**
   * Creates a new Row that represents a day separator.
   */
  public Row(DateTime startOfNewDay, Line... incomingLines) {
      this(startOfNewDay, Arrays.asList(incomingLines));
  }

  
  public String getAuthor() {
    return author;
  }

  public DateTime getCommitDate() {
    return commitDate;
  }

  public List<Line> getOutgoingLines() {
    return incomingLines;
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
