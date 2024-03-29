package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.List;
import java.util.ArrayList;

public class Layer {
  private final List<Row> rows;

  public Layer() {
    this.rows = new ArrayList<>();
  }
  
  public Layer(List<Row> rows) {
    this.rows = new ArrayList<>(rows);
  }

  public List<Row> getRows() {
    return rows;
  }
    
  @Override
  public String toString() {
      StringBuilder str = new StringBuilder();
      
      str.append("Layer{rows=[");
      
      for(Row row : rows) {
          str.append('\n');
          str.append(row);
      }
      
      str.append("]}");
      
      return str.toString();
  }
}
