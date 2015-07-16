package edu.unl.cse.knorth.git_sonification.display.model.sonification;

import java.util.Comparator;
import org.joda.time.DateTime;

public class MeasureTimestampComparator implements Comparator<Measure> {
  public int compare(Measure measure1, Measure measure2) {
    DateTime timestamp1 = measure1.getTimestamp();
    DateTime timestamp2 = measure2.getTimestamp();

    if(timestamp1.isBefore(timestamp2)) {
      return -1;
    } else if(timestamp1.isAfter(timestamp2)) {
      return 1;
    } else {
      return 0;
    }
  }
}
