package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Layer {
  private List<Row> rows;

  public Layer(Collection<Row> rows) {
    this.rows = new ArrayList<Row>(rows);
    Collections.sort(this.rows, new RowDateComparator());
  }

  public List<Row> getRows() {
    return rows;
  }
}
