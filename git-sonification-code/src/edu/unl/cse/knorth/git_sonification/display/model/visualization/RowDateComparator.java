package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.Comparator;
import org.joda.time.DateTime;

public class RowDateComparator implements Comparator<Row> {
  public int compare(Row row1, Row row2) {
    DateTime time1 = row1.getCommitDate();
    DateTime time2 = row2.getCommitDate();
      
      if(time1.isBefore(time2)) {
      return -1;
    } else if(time1.isAfter(time2)) {
      return 1;
    } else {
      return 0;
    }
  }
}
