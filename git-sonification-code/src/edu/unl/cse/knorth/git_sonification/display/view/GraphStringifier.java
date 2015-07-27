package edu.unl.cse.knorth.git_sonification.display.view;

import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.RowType;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphStringifier {
    /**
     * Create a string representing all of the layers
     * @param visualizationData A VisualizationData with the layers to stringify
     * @return A string that can be output to console to visualize the data
     */
    public String stringifyVisualizationData(
            VisualizationData visualizationData) {
        StringBuilder str = new StringBuilder();
        
        int layerNum = 1;
        for(Layer layer : visualizationData.getLayers()) {
            str.append("Layer ");
            str.append(layerNum);
            str.append(":\n");
            str.append(stringifyLayer(layer));
            str.append("\n\n");
            layerNum++;
        }
        
        return str.toString();
    }
    
    /**
     * Stringifies a single layer as a Git tree. The visualization shows the
     * graph with the oldest commits on top. The String is generated by
     * processing each row in order. Each processed row doesn't know anything
     * about any other rows and doesn't need to.
     * @param layer The Layer to Stringify
     * @return The Stringified Layer
     */
    public String stringifyLayer(Layer layer) {
        StringBuilder str = new StringBuilder();
        
        for(Row row : layer.getRows()) {
            str.append(stringifyRow(row));
            str.append('\n');
        }
        
        return str.toString();
    }
    
    private String stringifyRow(Row row) {
        if(row.getType() == RowType.COMMIT) {
            return stringifyCommitRow(row);
        } else {
            return stringifyDaySeparatorRow(row);
        }
    }
    
    /**
     * Stringifies a Row representing a commit. This is done in two steps:
     * 1. Place all of the |, \, and /s before the commit to show branches
     * merging
     * 2. Draw the commit along with parallel branches
     * @param row
     * @return 
     */
    private String stringifyCommitRow(Row row) {
        StringBuilder str = new StringBuilder();
        
        // Get the nubmer of textual rows needed to place all of the \ and /s
        // necessary to show the rows
        int numBranchRows = 0;
        for(Line line : row.getIncomingLines()) {
            int numBranchRowsNeeded = Math.abs(line.toBranch - line.fromBranch);
            if(numBranchRowsNeeded > numBranchRows) {
                numBranchRows = numBranchRowsNeeded;
            } 
        }
        
        for(int branchRow = 1; branchRow <= numBranchRows; branchRow++) {
            List<BranchCharacter> branchChars = new ArrayList<>();
            
            // Get all of the characters we'll draw in this row and where to
            // place them. We don't know that the branch lines are in order,
            // so we have to determine all of the branch lines' placements
            // before appending any of them to the String
            for(Line line : row.getIncomingLines()) {
                if(line.toBranch == line.fromBranch) {
                    int position = (line.toBranch * 2) - 1;
                    branchChars.add(new BranchCharacter(position, '|'));
                } else if(line.toBranch > line.fromBranch) {
                    if(branchRow <= Math.abs(line.toBranch - line.fromBranch)) {
                        int position =
                                (line.fromBranch * 2) + (branchRow * 2 - 2);
                        branchChars.add(new BranchCharacter(position, '\\'));
                    } else {
                        branchChars.add(new BranchCharacter(
                                line.toBranch * 2 - 1 , '|'));
                    }
                } else {
                    if(branchRow <= Math.abs(line.toBranch - line.fromBranch)) {
                        int position = (line.fromBranch * 2) - (branchRow * 2);
                        branchChars.add(new BranchCharacter(position, '/'));
                    } else {
                        branchChars.add(new BranchCharacter(
                                line.toBranch * 2 - 1, '|'));
                    }
                }
            }
            
            Collections.sort(branchChars);
                
            // Now we append all of the / and \s to the String
            int currentCol = 1;
            for(BranchCharacter branchChar : branchChars) {
                for(/* currentCol */; currentCol < branchChar.getPosition();
                        currentCol++) {
                    str.append(' ');
                }
                str.append(branchChar.getCharacter());
                currentCol++;
            }
            
            str.append('\n');
        }
        
        
        // Finally, we draw the commit along with the branches running parallel
        // to it.
        for(int rowNum = 0; rowNum < getMaxToBranchFromRow(row) * 2 - 1;
                rowNum++) {
            if(rowNum % 2 == 0) {
                // Commits that are "visible" on the layer are *s. "Invisible"
                // commits as Os.
                if(rowNum == (row.getBranchLocation() - 1) * 2) {
                    if(row.isVisible()) {
                        str.append('*');
                    } else {
                        str.append('O');
                    }
                } else {
                    str.append('|');
                }
            } else {
                str.append(' ');
            }
        }
        
        return str.toString();
    }

    /**
     * Stringifies a row representing a day separator.
     * @param row
     * @return 
     */
    private String stringifyDaySeparatorRow(Row row) {
        StringBuilder str = new StringBuilder();
        
        for(int rowNum = 0; rowNum < row.getIncomingLines().size() * 2 - 1;
                rowNum++) {
            if(rowNum % 2 == 0) {
                str.append('+');
            } else {
                str.append('-');
            }
        }

        return str.toString();
    }
    
    private int getMaxToBranchFromRow(Row row) {
        List<Line> lines = row.getIncomingLines();
        int maxToBranch = lines.get(0).toBranch;
        
        for(Line line : lines) {
            if(line.toBranch > maxToBranch) {
                maxToBranch = line.toBranch;
            }
        }
        
        return maxToBranch;
    }
    
    private static final class BranchCharacter
        implements Comparable<BranchCharacter> {
        private final int position;
        private final char character;

        public BranchCharacter(int position, char character) {
            this.position = position;
            this.character = character;
        }

        public int getPosition() {
            return position;
        }

        public char getCharacter() {
            return character;
        }

        @Override
        public int compareTo(BranchCharacter other) {
            if(this.position > other.position) {
                return 1;
            } else if(this.position < other.position) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}