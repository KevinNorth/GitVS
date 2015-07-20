package edu.unl.cse.knorth.git_sonification.display.model.sonification;

import org.joda.time.DateTime;

public class Measure {
  private String author;
  private DateTime timestamp;
  private boolean isDaySeparator;
  private int numConflicts;

  public Measure(String author, DateTime timestamp, boolean isDaySeparator,
    int numConflicts) {
    this.author = author;
    this.timestamp = timestamp;
    this.isDaySeparator = isDaySeparator;
    this.numConflicts = numConflicts;
  }

  public String getAuthor() {
    return author;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }

  public boolean isDaySeparator() {
    return isDaySeparator;
  }

  public int getNumConflicts() {
    return numConflicts;
  }

    @Override
    public String toString() {
        return "Measure{" + "author=" + author + ", timestamp=" + timestamp +
                ", isDaySeparator=" + isDaySeparator + ", numConflicts=" +
                numConflicts + '}';
    }
}
