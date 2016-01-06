package edu.unl.cse.knorth.git_sonification.display.model.sonification;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class SonificationData {
  private List<Measure> measures;
  private List<String> authorsInOrderOfCommitCounts;
  
  public SonificationData(Collection<Measure> measures,
          List<String> authorsInOrderOfCommitCounts) {
    this.measures = new ArrayList<Measure>(measures);
    Collections.sort(this.measures, new MeasureTimestampComparator());
    this.authorsInOrderOfCommitCounts = authorsInOrderOfCommitCounts;
  }

  public List<Measure> getMeasures() {
    return measures;
  }

    public List<String> getAuthorsInOrderOfCommitCounts() {
        return authorsInOrderOfCommitCounts;
    }
}
