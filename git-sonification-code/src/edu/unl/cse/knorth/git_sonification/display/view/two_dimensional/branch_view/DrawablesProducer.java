package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import java.util.ArrayList;

/**
 * Contains the logic to take a ViewModel and produce all of the Drawables
 * for a BranchView.
 * @author knorth
 */
public class DrawablesProducer {
    public CommitsAndLines produceCommitDrawables(ViewModel viewModel) {
        VisualizationData visualData = viewModel.getVisualizationData();
        
        int rowNum = 1;
        
        final float leftMargin = 50;
        final float topMargin = 50;
        
        final float distanceBetweenRows = 50;
        final float distanceBetweenLines = 50;
        final float sizeOfCommits = 20;
        final float radiusOfCommits = sizeOfCommits / 2.0f;
        
        final float lineWeight = 10;
        
        final int commitsZOrdering = 1;
        final int linesZOrdering = 0;
        
        Color commitColor = Color.BLUE;
        Color lineColor = Color.RED;
        
        Layer combinedLayer = visualData.getCombinedLayer();
        
        ArrayList<CommitDrawable> commits = new ArrayList<>();
        ArrayList<LineDrawable> lines = new ArrayList<>();
        
        for(Row row : combinedLayer.getRows()) {
            float topOfRow = topMargin + (distanceBetweenRows * rowNum);
            
            int commitBranchNum = row.getBranchLocation();
            float commitLeftEdge = leftMargin
                    + (distanceBetweenLines * commitBranchNum);
            Rectangle commitRect = new Rectangle(commitLeftEdge, topOfRow,
                commitLeftEdge + sizeOfCommits, topOfRow + sizeOfCommits);
            commits.add(new CommitDrawable(commitColor, commitRect,
                    commitsZOrdering));
            
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
}