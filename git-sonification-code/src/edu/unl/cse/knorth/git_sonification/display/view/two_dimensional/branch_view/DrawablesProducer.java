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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

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
    
    private final float minCommitSize = sizeOfCommits / 2f;
    private final float maxCommitSize = sizeOfCommits * 2f;
    private final float commitSizeIncrement = (maxCommitSize - minCommitSize)
            / 20f;
    private final float conflictSize = maxCommitSize;
    
    private final float lineWeight = 8;
        
    private final int commitsZOrdering = 1;
    private final int linesZOrdering = 0;

    private final float playButtonsHeight = 105f;
    private final float playButtonsWidth = playButtonsHeight;
    
    public CommitsConflictsAndLines produceCommitDrawables(ViewModel viewModel,
            PApplet context) {
        VisualizationData visualData = viewModel.getVisualizationData();

        int rowNum = 1;
                
        Color commitColor = Color.BLUE;
        Color conflictColor = // orange to contrast with commits' deep blue
                Color.createHSBColor((char)(255/12), (char)195, (char)255);
        PFont conflictFont = context.createFont("Arial", 24);
        Map<Integer, Color> lineColors = determineLineColors(visualData);
        
        Layer combinedLayer = visualData.getCombinedLayer();
        
        ArrayList<CommitDrawable> commits = new ArrayList<>();
        ArrayList<BranchLineDrawable> lines = new ArrayList<>();
        ArrayList<ConflictDrawable> conflicts = new ArrayList<>();
        
        for(Row row : combinedLayer.getRows()) {
            float topOfRow = topMargin + (distanceBetweenRows * rowNum);
            
            if(row.getType() == RowType.COMMIT) {
                int numCommits = row.getCommit().getComponentsModified().size();
                int numConflicts = row.getNumConflicts();
                int commitBranchNum = row.getBranchLocation();

                float commitSize = Math.min(minCommitSize +
                        (commitSizeIncrement * numCommits), maxCommitSize);
                float commitRadius = commitSize / 2f;
                float commitOffset = radiusOfCommits - commitRadius;
                
                float commitLeftEdge = leftMargin
                        + (distanceBetweenLines * commitBranchNum);
                Rectangle commitRect = new Rectangle(
                    commitLeftEdge + commitOffset, topOfRow + commitOffset,
                    commitLeftEdge + commitSize + commitOffset,
                    topOfRow + commitSize + commitOffset);
                commits.add(new CommitDrawable(commitColor, row.getCommit(),
                        row.getNumConflicts(), commitRect, commitsZOrdering));
                Rectangle conflictRect = new Rectangle(commitRect.center(),
                        conflictSize, conflictSize);
                conflicts.add(new ConflictDrawable(numConflicts, conflictFont,
                        conflictColor, conflictRect, commitsZOrdering + 1));
            }
            
            float topOfPreviousRow = topOfRow - distanceBetweenRows;
            for(Line line : row.getIncomingLines()) {
                Color lineColor = lineColors.get(line.color);
                
                float topXCoordinate = leftMargin
                        + (distanceBetweenLines * line.fromBranch);
                float bottomXCoordinate = leftMargin
                        + (distanceBetweenLines * line.toBranch);
                
                if(line.fromBranch == line.toBranch) {
                    Rectangle lineRect = new Rectangle(topXCoordinate,
                            topOfPreviousRow + radiusOfCommits,
                            bottomXCoordinate + sizeOfCommits,
                            topOfRow + radiusOfCommits);
                    lines.add(new BranchLineDrawable(
                            BranchLineDrawable.Direction.STRAIGHT_DOWN, lineColor,
                            lineWeight, lineRect, linesZOrdering));
                } else if(line.fromBranch > line.toBranch) {
                    Rectangle lineRect = new Rectangle(
                            bottomXCoordinate + radiusOfCommits,
                            topOfPreviousRow + radiusOfCommits,
                            topXCoordinate + radiusOfCommits,
                            topOfRow + radiusOfCommits);
                    lines.add(new BranchLineDrawable(
                            BranchLineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT,
                            lineColor, lineWeight, lineRect, linesZOrdering));
                } else {
                    Rectangle lineRect = new Rectangle(
                            topXCoordinate + radiusOfCommits,
                            topOfPreviousRow + radiusOfCommits,
                            bottomXCoordinate + radiusOfCommits,
                            topOfRow + radiusOfCommits);
                    lines.add(new BranchLineDrawable(
                            BranchLineDrawable.Direction.TOPLEFT_TO_BOTTOMRIGHT,
                            lineColor, lineWeight, lineRect, linesZOrdering));
                }
            }
            
            rowNum++;
        }
        
        return new CommitsConflictsAndLines(commits, conflicts, lines);
    }
    
    public DaySeparatorsAndTimestamps
        produceDaySeparatorsAndTimestamps(ViewModel viewModel, PApplet
                context) {
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
        
        return new DaySeparatorsAndTimestamps(daySeparators,
                timestamps);
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
    
    public ArrayList<PlayButton> producePlayButtons(PApplet context) {
        PImage forwardImage = context.loadImage("images/play.png");
        PImage reverseImage = context.loadImage("images/reverse.png");
        
        Rectangle forwardButtonLocation = new Rectangle(
                context.width - playButtonsWidth,
                context.height - playButtonsHeight, context.width,
                context.height);
        Rectangle reverseButtonLocation = new Rectangle(
                context.width - (playButtonsWidth * 2),
                context.height - playButtonsHeight,
                context.width - playButtonsWidth,
                context.height);

        float playSpeed = 150.0f;
        
        PlayButton forwardButton = new PlayButton(
                PlayButton.PlayDirection.FORWARDS, playSpeed, forwardImage,
                forwardButtonLocation, Integer.MAX_VALUE);
        PlayButton reverseButton = new PlayButton(
                PlayButton.PlayDirection.REVERSE, playSpeed, reverseImage,
                reverseButtonLocation, Integer.MAX_VALUE);
        
        ArrayList<PlayButton> playButtons = new ArrayList<>(2);
        playButtons.add(forwardButton);
        playButtons.add(reverseButton);
        return playButtons;
    }

    private Map<Integer, Color> determineLineColors(VisualizationData visualData) {
        List<Row> rows = visualData.getCombinedLayer().getRows();
        
        int maxColor = 0;
        for(Row row : rows) {
            for(Line line : row.getIncomingLines()) {
                if(line.color > maxColor) {
                    maxColor = line.color;
                }
            }
        }
        
        Map<Integer, Color> colorMap = new HashMap<>();
        int maxColorThird = (maxColor / 9) + 1;
        int colorNum = 1;
        for(int saturationNum = 0; saturationNum <= maxColorThird;
                saturationNum++) {
            for(int brightnessNum = 0; brightnessNum <= maxColorThird;
                    brightnessNum++) {
                for(int hueNum = 0; hueNum <= maxColorThird; hueNum++) {
                    char hue = (char) ((((float)(maxColorThird + 1 - hueNum))
                            / (float) (maxColorThird + 1)) * (char) 255);
                    char brightness =
                            (char) (((((float)(maxColorThird - brightnessNum))
                            / (float) (maxColorThird)) * 155) + 100);
                    char saturation =
                            (char) (((((float)(maxColorThird - saturationNum))
                            / (float) (maxColorThird)) * 155) + 100);
                    Color color = Color.createHSBColor(hue, saturation,
                            brightness);
                    colorMap.put(colorNum, color);
                    colorNum++;
                }
            }
        }

        
        return colorMap;
    }
        
    public static class CommitsConflictsAndLines {
        private final ArrayList<CommitDrawable> commits;
        private final ArrayList<BranchLineDrawable> lines;
        private final ArrayList<ConflictDrawable> conflicts;

        public CommitsConflictsAndLines(ArrayList<CommitDrawable> commits,
                ArrayList<ConflictDrawable> conflicts,
                ArrayList<BranchLineDrawable> lines) {
            this.commits = commits;
            this.lines = lines;
            this.conflicts = conflicts;
        }

        public ArrayList<CommitDrawable> getCommits() {
            return commits;
        }

        public ArrayList<BranchLineDrawable> getLines() {
            return lines;
        }

        public ArrayList<ConflictDrawable> getConflicts() {
            return conflicts;
        }
    }
    
    public static class DaySeparatorsAndTimestamps {
        private final ArrayList<DaySeparatorDrawable> daySeparators;
        private final ArrayList<TextDrawable> timestamps;

        public DaySeparatorsAndTimestamps(
                ArrayList<DaySeparatorDrawable> daySeparators,
                ArrayList<TextDrawable> timestamps) {
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