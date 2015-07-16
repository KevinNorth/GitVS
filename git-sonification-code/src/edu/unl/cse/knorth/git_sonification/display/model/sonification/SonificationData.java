package edu.unl.cse.knorth.git_sonification.display.model.sonification;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SonificationData {
  private List<Measure> measures;

  public SonificationData(Collection<Measure> measures) {
    this.measures = new ArrayList<Measure>(measures);
    Collections.sort(this.measures, new MeasureTimestampComparator());
  }

  public List<Measure> getMeasures() {
    return measures;
  }
}
