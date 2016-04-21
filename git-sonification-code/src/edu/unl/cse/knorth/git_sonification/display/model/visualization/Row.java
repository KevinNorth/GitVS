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
  private int numConflicts;
  private int numDaysPassed;
  private Commit commit;

  /**
   * Creates a new Row that represents a commit.
   */
  public Row(String author, DateTime commitDate, int branchLocation,
    int numConflicts, Commit commit, Collection<Line> incomingLines) {
    this.author = author;
    this.commitDate = commitDate;
    this.type = RowType.COMMIT;
    this.branchLocation = branchLocation;
    this.incomingLines = new ArrayList<>(incomingLines);
    this.numConflicts = numConflicts;
    this.commit = commit;
  }

  /**
   * Creates a new Row that represents a commit.
   */
  public Row(String author, DateTime commitDate, int branchLocation,
    int numConflicts, Commit commit, Line... incomingLines) {
      this(author, commitDate, branchLocation, numConflicts, commit,
              Arrays.asList(incomingLines));
  }
  
  /**
   * Creates a new Row that represents a day separator.
   */
  public Row(DateTime startOfNewDay, int numConflicts, int numDaysPassed,
          Collection<Line> incomingLines) {
    this.author = null;
    this.commitDate = startOfNewDay;
    this.incomingLines = new ArrayList<>(incomingLines);
    this.type = RowType.DAY_SEPARATOR;
    this.numDaysPassed = numDaysPassed;
    this.branchLocation = NO_BRANCH_LOCATION;
    this.numConflicts = numConflicts;
    this.commit = null;
  }

    /**
   * Creates a new Row that represents a day separator.
   */
  public Row(DateTime startOfNewDay, int numConflicts, int numDaysPassed,
          Line... incomingLines) {
      this(startOfNewDay, numConflicts, numDaysPassed,
              Arrays.asList(incomingLines));
  }

  
  public String getAuthor() {
    return author;
  }

  public DateTime getCommitDate() {
    return commitDate;
  }

  public List<Line> getIncomingLines() {
    return incomingLines;
  }

  public RowType getType() {
    return type;
  }

  public int getBranchLocation() {
    return branchLocation;
  }

  public int getNumDaysPassed() {
      return numDaysPassed;
  }
  
  public int getNumConflicts() {
    return numConflicts;
  }

    public Commit getCommit() {
        return commit;
    }
  
  @Override
  public String toString() {
    return "Row{" + "author=" + author +
            ", commitDate=" + commitDate +
            ", incomingLines=" + incomingLines +
            ", type=" + type +
            ", branchLocation=" + branchLocation +
            ", numConflicts=" + numConflicts + 
            ", commit=" + commit.toString() +
            '}';
  }
}
