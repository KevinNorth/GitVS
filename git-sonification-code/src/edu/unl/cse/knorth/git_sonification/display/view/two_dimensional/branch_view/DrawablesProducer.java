package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.RowType;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java.TextDrawable;
import java.util.ArrayList;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Contains the logic to take a ViewModel and produce all of the Drawables
 * for a BranchView.
 * @author knorth
 */
public class DrawablesProducer {
    private final float leftMargin = 50;
    private final float topMargin = 50;
        
    private final float distanceBetweenRows = 50;
    private final float distanceBetweenLines = 50;
    private final float sizeOfCommits = 20;
    private final float radiusOfCommits = sizeOfCommits / 2.0f;
        
    private final float lineWeight = 10;
        
    private final int commitsZOrdering = 1;
    private final int linesZOrdering = 0;

    
    public CommitsAndLines produceCommitDrawables(ViewModel viewModel) {
        VisualizationData visualData = viewModel.getVisualizationData();
        
        int rowNum = 1;
                
        Color commitColor = Color.BLUE;
        Color lineColor = Color.RED;
        
        Layer combinedLayer = visualData.getCombinedLayer();
        
        ArrayList<CommitDrawable> commits = new ArrayList<>();
        ArrayList<LineDrawable> lines = new ArrayList<>();
        
        for(Row row : combinedLayer.getRows()) {
            float topOfRow = topMargin + (distanceBetweenRows * rowNum);
            
            if(row.getType() == RowType.COMMIT) {
                int commitBranchNum = row.getBranchLocation();
                float commitLeftEdge = leftMargin
                        + (distanceBetweenLines * commitBranchNum);
                Rectangle commitRect = new Rectangle(commitLeftEdge, topOfRow,
                    commitLeftEdge + sizeOfCommits, topOfRow + sizeOfCommits);
                commits.add(new CommitDrawable(commitColor, row.getCommit(),
                        row.getNumConflicts(), commitRect, commitsZOrdering));
            }
            
            float topOfPreviousRow = topOfRow - distanceBetweenRows;
            for(Line line : row.getIncomingLines()) {
                float topXCoordinate = leftMargin
                        + (distanceBetweenLines * line.fromBranch);
                float bottomXCoordinate = leftMargin
                        + (distanceBetweenLines * line.toBranch);
                
                if(line.fromBranch == line.toBranch) {
                    Rectangle lineRect = new Rectangle(topXCoordinate,
                            topOfPreviousRow + radiusOfCommits,
                            bottomXCoordinate + sizeOfCommits,
                            topOfRow + radiusOfCommits);
                    lines.add(new LineDrawable(
                            LineDrawable.Direction.STRAIGHT_DOWN, lineColor,
                            lineWeight, lineRect, linesZOrdering));
                } else if(line.fromBranch > line.toBranch) {
                    Rectangle lineRect = new Rectangle(
                            bottomXCoordinate + radiusOfCommits,
                            topOfPreviousRow + radiusOfCommits,
                            topXCoordinate + radiusOfCommits,
                            topOfRow + radiusOfCommits);
                    lines.add(new LineDrawable(
                            LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT,
                            lineColor, lineWeight, lineRect, linesZOrdering));
                } else {
                    Rectangle lineRect = new Rectangle(
                            topXCoordinate + radiusOfCommits,
                            topOfPreviousRow + radiusOfCommits,
                            bottomXCoordinate + radiusOfCommits,
                            topOfRow + radiusOfCommits);
                    lines.add(new LineDrawable(
                            LineDrawable.Direction.TOPLEFT_TO_BOTTOMRIGHT,
                            lineColor, lineWeight, lineRect, linesZOrdering));
                }
            }
            
            rowNum++;
        }
        
        return new CommitsAndLines(commits, lines);
    }
    
    public DaySeparatorsAndTimestamps produceDaySeparatorsAndTimestamps(
        ViewModel viewModel, PApplet context) {
        VisualizationData visualData = viewModel.getVisualizationData();
        Layer combinedLayer = visualData.getCombinedLayer();
        
        int maxNumBranches = 1;
        
        for(Row row : combinedLayer.getRows()) {
            for(Line line : row.getIncomingLines()) {
                if(line.fromBranch > maxNumBranches) {
                    maxNumBranches = line.fromBranch;
                }
                if(line.toBranch > maxNumBranches) {
                    maxNumBranches = line.toBranch;
                }
            }
        }
        
        ArrayList<DaySeparatorDrawable> daySeparators = new ArrayList<>();
        ArrayList<TextDrawable> timestamps = new ArrayList<>();
        
        final PFont daySeparatorFont = context.createFont("Arial", 16);
        final PFont timestampFont = context.createFont("Arial", 14);

        final Color daySeparatorTextColor = Color.BLACK;
        final Color timestampTextColor = Color.BLACK;
        
        final Color daySeparatorDividerColor = Color.BLUE;
        final float daySeparatorDividerThickness = 2.0f;
        
        final float daySeparatorWidth = (leftMargin * 2)
                + (distanceBetweenLines * maxNumBranches);
        
        final DateTimeFormatter timestampFormatter =
            DateTimeFormat.forPattern("h:mm a");
        
        int rowNum = 1;
        for(Row row : combinedLayer.getRows()) {
            float topOfRow = topMargin + (distanceBetweenRows * rowNum);
            if(row.getType() == RowType.DAY_SEPARATOR) {
                float bottomOfDaySeparator = topOfRow
                        + daySeparatorFont.getSize();
                
                Rectangle daySeparatorRect = new Rectangle(0, topOfRow,
                        daySeparatorWidth, bottomOfDaySeparator);
                daySeparators.add(new DaySeparatorDrawable(row.getCommitDate(),
                        daySeparatorFont, daySeparatorTextColor,
                        daySeparatorDividerThickness, daySeparatorDividerColor,
                        daySeparatorRect, 3));
            } else {
                float bottomOfTimestamp = topOfRow + timestampFont.getSize();
                
                Rectangle timestampRect = new Rectangle(0, topOfRow,
                        leftMargin, bottomOfTimestamp);
                String timestampString =
                        timestampFormatter.print(row.getCommitDate());
                
                timestamps.add(new TextDrawable(timestampRect, 2,
                        timestampString, timestampFont, timestampTextColor));
            }
            
            rowNum++;
        }

        //Add a "day separator" to mark the first date shown in the view
        Row firstRow = combinedLayer.getRows().get(0);
        if(firstRow.getType() == RowType.COMMIT) {
            Rectangle daySeparatorRect = new Rectangle(0, topMargin,
                    daySeparatorWidth, topMargin + daySeparatorFont.getSize());
            daySeparators.add(new DaySeparatorDrawable(firstRow.getCommitDate(),
                    daySeparatorFont, daySeparatorTextColor,
                    daySeparatorDividerThickness, daySeparatorDividerColor,
                    daySeparatorRect, 3));
        }
        
        return new DaySeparatorsAndTimestamps(daySeparators, timestamps);
    }
    
    public SonificationCursorDrawable produceSonificationCursor(
            ViewModel viewModel) {
        int maxNumBranches = 1;
        
        for(Row row :
                viewModel.getVisualizationData().getCombinedLayer().getRows()) {
            for(Line line : row.getIncomingLines()) {
                if(line.fromBranch > maxNumBranches) {
                    maxNumBranches = line.fromBranch;
                }
                if(line.toBranch > maxNumBranches) {
                    maxNumBranches = line.toBranch;
                }
            }
        }
        
        final float width = (leftMargin * 2f)
                + (distanceBetweenLines * maxNumBranches);
        final float height = sizeOfCommits;
        
        Rectangle rectangle = new Rectangle(0, 0, width, height);
        return new SonificationCursorDrawable(Color.CYAN, rectangle,
                Integer.MIN_VALUE);
    }
        
    public static class CommitsAndLines {
        private final ArrayList<CommitDrawable> commits;
        private final ArrayList<LineDrawable> lines;

        public CommitsAndLines(ArrayList<CommitDrawable> commits, ArrayList<LineDrawable> lines) {
            this.commits = commits;
            this.lines = lines;
        }

        public ArrayList<CommitDrawable> getCommits() {
            return commits;
        }

        public ArrayList<LineDrawable> getLines() {
            return lines;
        }
    }
    
    public static class DaySeparatorsAndTimestamps {
        private final ArrayList<DaySeparatorDrawable> daySeparators;
        private final ArrayList<TextDrawable> timestamps;

        public DaySeparatorsAndTimestamps(ArrayList<DaySeparatorDrawable> daySeparators, ArrayList<TextDrawable> timestamps) {
            this.daySeparators = daySeparators;
            this.timestamps = timestamps;
        }

        public ArrayList<DaySeparatorDrawable> getDaySeparators() {
            return daySeparators;
        }

        public ArrayList<TextDrawable> getTimestamps() {
            return timestamps;
        }
    }
}