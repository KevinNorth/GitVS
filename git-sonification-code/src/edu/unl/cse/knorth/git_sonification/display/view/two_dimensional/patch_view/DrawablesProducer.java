package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.patch_view;

import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Commit;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Color;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Drawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.Rectangle;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java.LineDrawable;
import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.common_drawables.java.TextDrawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import processing.core.PApplet;

public class DrawablesProducer {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormat.forPattern("E, M/d/YY h:mm a");

    private final float tableLineWidth = 1.0f;
    
    private final int bodyTextSize = 14;
    private final int headerTextSize = 16;
    private final String bodyFontName = "Arial";
    private final String bodyFixedWithFontName = "Courier";
    private final String headerFontName = "Arial";
    
    private final float cellMargin = 6f;
    
    private final int numCharsToPrintInHash = 8;
    
    private final Color headerColor = Color.BLACK;
    private final Color bodyColor = Color.BLACK;
    private final Color lineColor = Color.BLACK;
    
    public PatchViewDrawables produceDrawablesWithoutConflicts(
            ViewModel viewModel, List<Commit> selectedCommits,
            List<String> allComponents, PApplet context) {
        ArrayList<Drawable> noninteractiveDrawables = new ArrayList<>();
        ArrayList<CheckmarkDrawable> checkmarks = new ArrayList<>();
        
        Map<String, Color> componentColors =
                createComponentColors(allComponents);
        
        Set<String> selectedComponentsSet = new HashSet<>();
        for(Commit commit : selectedCommits) {
            for(String component : commit.getComponentsModified()) {
                selectedComponentsSet.add(component);
            }
        }
        
        List<String> selectedComponents =
                new ArrayList<>(selectedComponentsSet);
        Collections.sort(selectedComponents);
        
        final int numBodyColumns = selectedComponentsSet.size();
        final int numBodyRows = selectedCommits.size();
        
        List<String> authors = new LinkedList<>();
        Map<Commit, String> timestamps = new HashMap<>(selectedCommits.size());
        Map<Commit, Integer> numsFilesModified =
                new HashMap<>(selectedCommits.size());
        List<String> componentNames = new LinkedList<>();
        int maxFilesModified = 0;
        
        for(Commit commit : selectedCommits) {
            int numFilesModified = commit.getComponentsModified().size();
            
            authors.add(commit.getAuthor());
            timestamps.put(commit, DATE_FORMATTER.print(commit.getTimestamp()));
            numsFilesModified.put(commit, numFilesModified);

            if(numFilesModified > maxFilesModified) { 
                maxFilesModified = numFilesModified;
            }
            
            for(String component : commit.getComponentsModified()) {
                componentNames.add(component);
            }
        }
                
        int lengthOfLongestAuthor = getNumCharsInLongestString(authors);
        int lengthOfLongestTimestamp =
                getNumCharsInLongestString(timestamps.values());
        int lengthOfLongestComponent =
                getNumCharsInLongestString(componentNames);
        int lengthOfLongestFilesModified =
                Math.max(Integer.toString(maxFilesModified).length(),
                        "# of Files".length());
        
        float heightOfHeader = lengthOfLongestComponent * headerTextSize * 0.6f;
        
        float hashColWidth = numCharsToPrintInHash * (bodyTextSize * 0.6f);
        float authorColWidth = lengthOfLongestAuthor * (bodyTextSize * 0.6f);
        float timestampColWidth = lengthOfLongestTimestamp *(bodyTextSize*0.6f);
        float lengthOfNumFilesModifiedWidth = lengthOfLongestFilesModified
                * (bodyTextSize*0.6f);
        float checkmarksLeftMargin = hashColWidth + authorColWidth
                + timestampColWidth + lengthOfNumFilesModifiedWidth +
                (cellMargin * 7);
        
        float heightOfVerticalLines =
                (numBodyRows * ((bodyTextSize) + (2 * cellMargin)))
                + heightOfHeader + (cellMargin * 2);
        float widthOfHorizontalLines = checkmarksLeftMargin +
                (numBodyColumns * ((bodyTextSize) + (2 * cellMargin)))
                - tableLineWidth;
        
        /* Add header */
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(cellMargin, 0, hashColWidth,
                        headerTextSize), 0, "Commit",
                        headerFontName, headerTextSize, headerColor, context));
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(hashColWidth + (cellMargin * 2),
                        0, hashColWidth + (cellMargin * 2) + authorColWidth,
                        headerTextSize), 0, "Author",
                        headerFontName, headerTextSize, headerColor, context));
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(
                        hashColWidth + authorColWidth + (cellMargin * 4),
                        0, hashColWidth + authorColWidth + timestampColWidth + (cellMargin * 4), headerTextSize), 0, "Timestamp",
                        headerFontName, headerTextSize, headerColor, context));
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(
                        hashColWidth + authorColWidth + timestampColWidth + (cellMargin * 6),
                        0, checkmarksLeftMargin, headerTextSize), 0, "# of Files",
                        headerFontName, headerTextSize, headerColor, context));
        for(int i = 0; i < selectedComponents.size(); i++) {
            String component = selectedComponents.get(i);
            float x1 = checkmarksLeftMargin + (bodyTextSize * i)
                + (cellMargin * (2 * (i + 1)));
            float x2 = x1 + bodyTextSize;

            noninteractiveDrawables.add(
                new VerticalTextDrawable(new Rectangle(
                        x1, -heightOfHeader + headerTextSize, x2, headerTextSize), 0,
                        component, headerFontName, headerTextSize,
                        headerColor, context));
        }
        
        /* Add vertical lines */
        {
            float y1 = -heightOfHeader + headerTextSize;
            float y2 = y1 + heightOfVerticalLines;
        
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(0,
                    y1, 0, y2), 2));
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(hashColWidth + (cellMargin),
                    y1, hashColWidth + (cellMargin), y2), 2));
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(
                    hashColWidth + authorColWidth + (cellMargin * 3),
                    y1, hashColWidth + authorColWidth + (cellMargin * 3), y2),
                    2));
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(
                    hashColWidth + authorColWidth + timestampColWidth + (cellMargin * 5),
                    y1, hashColWidth + authorColWidth + timestampColWidth + (cellMargin * 5), y2),
                    2));
            for(int i = 0;
                i < selectedComponents.size() + 1 /* for outer border line */;
                i++) {
                noninteractiveDrawables.add(new LineDrawable(
                        LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT,
                        tableLineWidth,
                        lineColor, new Rectangle(checkmarksLeftMargin
                                + (cellMargin * 2 * i - 1) + (bodyTextSize * i),
                        y1, checkmarksLeftMargin
                                + (cellMargin * 2 * i - 1) + (bodyTextSize * i),
                        y2), 2));
            }
        }
        
        /* Add body */
        for(int rowNum = 0; rowNum < selectedCommits.size(); rowNum++) {
            Commit commit = selectedCommits.get(rowNum);
            
            float rowTop = headerTextSize + (bodyTextSize * rowNum)
                    + (cellMargin * 2 * (rowNum + 1) + 1);
            float rowBottom = rowTop + bodyTextSize;
            
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(cellMargin, rowTop, hashColWidth,
                            rowBottom), 0, commit.getHash()
                                    .substring(0, numCharsToPrintInHash - 1),
                            bodyFixedWithFontName, bodyTextSize, bodyColor,
                            context));
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(hashColWidth + (cellMargin * 2),
                            rowTop,
                            hashColWidth + (cellMargin * 2) + authorColWidth,
                            rowBottom), 0, commit.getAuthor(), bodyFontName,
                            bodyTextSize, bodyColor, context));
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(
                            hashColWidth + authorColWidth + (cellMargin * 4),
                            rowTop, hashColWidth + authorColWidth + (cellMargin * 4) + timestampColWidth, rowBottom), 0,
                            timestamps.get(commit), bodyFontName, bodyTextSize,
                            bodyColor, context));
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(
                            hashColWidth + authorColWidth + (cellMargin * 6) + timestampColWidth,
                            rowTop, checkmarksLeftMargin, rowBottom), 0,
                            Integer.toString(numsFilesModified.get(commit)), bodyFontName, bodyTextSize,
                            bodyColor, context));
            for(int i = 0; i < selectedComponents.size(); i++) {
                String component = selectedComponents.get(i);
                
                if(!commit.getComponentsModified().contains(component)) {
                    continue;
                }
                
                float x1 = checkmarksLeftMargin + (bodyTextSize * i)
                    + (cellMargin * (2 * i + 1));
                float x2 = x1 + bodyTextSize;

                Color color = componentColors.get(component);
                
                checkmarks.add(
                    new CheckmarkDrawable(color, commit, component,
                            new Rectangle(x1, rowTop, x2, rowBottom), 1));
            }
        }
        
        /* Add horizontal lines */
        for(int rowNum = 0; rowNum < selectedCommits.size(); rowNum++) {
            float y = headerTextSize + (bodyTextSize * rowNum)
                    + (cellMargin * 2 * (rowNum + 1) - 1);
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT,
                    tableLineWidth, lineColor,
                    new Rectangle(0, y, widthOfHorizontalLines, y), 2));
        }
        float bottomLineY = -heightOfHeader + headerTextSize
                + heightOfVerticalLines;
        noninteractiveDrawables.add(new LineDrawable(
                LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT,
                tableLineWidth, lineColor,
                new Rectangle(0, bottomLineY, widthOfHorizontalLines,
                        bottomLineY), 2));
        

        
        return new PatchViewDrawables(checkmarks, noninteractiveDrawables);
   }
        
    public PatchViewDrawables produceDrawablesWithConflicts(
            ViewModel viewModel, List<Commit> selectedCommits,
            List<String> allComponents, PApplet context) {
        ArrayList<Drawable> noninteractiveDrawables = new ArrayList<>();
        ArrayList<CheckmarkDrawable> checkmarks = new ArrayList<>();
        
        Map<String, Color> componentColors =
                createComponentColors(allComponents);
        
        Set<String> selectedComponentsSet = new HashSet<>();
        for(Commit commit : selectedCommits) {
            for(String component : commit.getComponentsModified()) {
                selectedComponentsSet.add(component);
            }
        }
        
        List<String> selectedComponents =
                new ArrayList<>(selectedComponentsSet);
        Collections.sort(selectedComponents);
        
        final int numBodyColumns = selectedComponentsSet.size();
        final int numBodyRows = selectedCommits.size();
        
        List<String> authors = new LinkedList<>();
        Map<Commit, String> timestamps = new HashMap<>(selectedCommits.size());
        Map<Commit, Integer> numsFilesModified =
                new HashMap<>(selectedCommits.size());
        List<String> componentNames = new LinkedList<>();
        int maxFilesModified = 0;
        
        for(Commit commit : selectedCommits) {
            int numFilesModified = commit.getComponentsModified().size();
            
            authors.add(commit.getAuthor());
            timestamps.put(commit, DATE_FORMATTER.print(commit.getTimestamp()));
            numsFilesModified.put(commit, numFilesModified);

            if(numFilesModified > maxFilesModified) { 
                maxFilesModified = numFilesModified;
            }
            
            for(String component : commit.getComponentsModified()) {
                componentNames.add(component);
            }
            
            System.out.println(commit.getHash() + " " + numFilesModified);
        }
        
        System.out.println(maxFilesModified);
                
        int lengthOfLongestAuthor = getNumCharsInLongestString(authors);
        int lengthOfLongestTimestamp =
                getNumCharsInLongestString(timestamps.values());
        int lengthOfLongestComponent =
                getNumCharsInLongestString(componentNames);
        int lengthOfLongestFilesModified =
                Math.max(Integer.toString(maxFilesModified).length(),
                        "# of Files".length());
        int lengthOfConflicts = "# of Conflicts".length();
        
        float heightOfHeader = lengthOfLongestComponent * headerTextSize * 0.6f;
        
        float hashColWidth = numCharsToPrintInHash * (bodyTextSize * 0.6f);
        float authorColWidth = lengthOfLongestAuthor * (bodyTextSize * 0.6f);
        float timestampColWidth = lengthOfLongestTimestamp *(bodyTextSize*0.6f);
        float conflictsColWidth = lengthOfConflicts * (bodyTextSize * 0.6f);
        float lengthOfNumFilesModifiedWidth = lengthOfLongestFilesModified
                * (bodyTextSize*0.6f);
        float checkmarksLeftMargin = hashColWidth + authorColWidth
                + timestampColWidth + conflictsColWidth
                + lengthOfNumFilesModifiedWidth + (cellMargin * 7);
        
        float heightOfVerticalLines =
                (numBodyRows * ((bodyTextSize) + (2 * cellMargin)))
                + heightOfHeader + (cellMargin * 2);
        float widthOfHorizontalLines = checkmarksLeftMargin +
                (numBodyColumns * ((bodyTextSize) + (2 * cellMargin)))
                - tableLineWidth;
        
        /* Add header */
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(cellMargin, 0, hashColWidth,
                        headerTextSize), 0, "Commit",
                        headerFontName, headerTextSize, headerColor, context));
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(hashColWidth + (cellMargin * 2),
                        0, hashColWidth + (cellMargin * 2) + authorColWidth,
                        headerTextSize), 0, "Author",
                        headerFontName, headerTextSize, headerColor, context));
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(
                        hashColWidth + authorColWidth + (cellMargin * 4),
                        0, hashColWidth + authorColWidth + timestampColWidth + (cellMargin * 4), headerTextSize), 0, "Timestamp",
                        headerFontName, headerTextSize, headerColor, context));
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(
                        hashColWidth + authorColWidth + timestampColWidth + (cellMargin * 6),
                        0, hashColWidth + authorColWidth + timestampColWidth + conflictsColWidth + (cellMargin * 6), headerTextSize), 0, "# of Conflicts",
                        headerFontName, headerTextSize, headerColor, context));
        noninteractiveDrawables.add(
                new TextDrawable(new Rectangle(
                        hashColWidth + authorColWidth + timestampColWidth + conflictsColWidth + (cellMargin * 8),
                        0, checkmarksLeftMargin, headerTextSize), 0, "# of Files",
                        headerFontName, headerTextSize, headerColor, context));
        for(int i = 0; i < selectedComponents.size(); i++) {
            String component = selectedComponents.get(i);
            float x1 = checkmarksLeftMargin + (bodyTextSize * i)
                + (cellMargin * (2 * (i + 1)));
            float x2 = x1 + bodyTextSize;

            noninteractiveDrawables.add(
                new VerticalTextDrawable(new Rectangle(
                        x1, -heightOfHeader + headerTextSize, x2, headerTextSize), 0,
                        component, headerFontName, headerTextSize,
                        headerColor, context));
        }
        
        /* Add vertical lines */
        {
            float y1 = -heightOfHeader + headerTextSize;
            float y2 = y1 + heightOfVerticalLines;
        
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(0,
                    y1, 0, y2), 2));
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(hashColWidth + (cellMargin),
                    y1, hashColWidth + (cellMargin), y2), 2));
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(
                    hashColWidth + authorColWidth + (cellMargin * 3),
                    y1, hashColWidth + authorColWidth + (cellMargin * 3), y2),
                    2));
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(
                    hashColWidth + authorColWidth + timestampColWidth + (cellMargin * 5),
                    y1, hashColWidth + authorColWidth + timestampColWidth + (cellMargin * 5), y2),
                    2));
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT, tableLineWidth,
                    lineColor, new Rectangle(
                    hashColWidth + authorColWidth + timestampColWidth + conflictsColWidth + (cellMargin * 7),
                    y1, hashColWidth + authorColWidth + timestampColWidth + conflictsColWidth + (cellMargin * 7), y2),
                    2));
            for(int i = 0;
                i < selectedComponents.size() + 1 /* for outer border line */;
                i++) {
                noninteractiveDrawables.add(new LineDrawable(
                        LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT,
                        tableLineWidth,
                        lineColor, new Rectangle(checkmarksLeftMargin
                                + (cellMargin * 2 * i - 1) + (bodyTextSize * i),
                        y1, checkmarksLeftMargin
                                + (cellMargin * 2 * i - 1) + (bodyTextSize * i),
                        y2), 2));
            }
        }
        
        /* Add body */
        for(int rowNum = 0; rowNum < selectedCommits.size(); rowNum++) {
            Commit commit = selectedCommits.get(rowNum);
            Row commitRow = null;
            
            for(Row row : viewModel.getVisualizationData().getCombinedLayer().getRows()) {
                if(row.getCommit() == commit) {
                    commitRow = row;
                    break;
                }
            }
            
            float rowTop = headerTextSize + (bodyTextSize * rowNum)
                    + (cellMargin * 2 * (rowNum + 1) + 1);
            float rowBottom = rowTop + bodyTextSize;
            
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(cellMargin, rowTop, hashColWidth,
                            rowBottom), 0, commit.getHash()
                                    .substring(0, numCharsToPrintInHash - 1),
                            bodyFixedWithFontName, bodyTextSize, bodyColor,
                            context));
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(hashColWidth + (cellMargin * 2),
                            rowTop,
                            hashColWidth + (cellMargin * 2) + authorColWidth,
                            rowBottom), 0, commit.getAuthor(), bodyFontName,
                            bodyTextSize, bodyColor, context));
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(
                            hashColWidth + authorColWidth + (cellMargin * 4),
                            rowTop, hashColWidth + authorColWidth + (cellMargin * 4) + timestampColWidth, rowBottom), 0,
                            timestamps.get(commit), bodyFontName, bodyTextSize,
                            bodyColor, context));
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(
                            hashColWidth + authorColWidth + (cellMargin * 6) + timestampColWidth,
                            rowTop, hashColWidth + authorColWidth + (cellMargin * 6) + timestampColWidth + conflictsColWidth, rowBottom), 0,
                            Integer.toString(commitRow.getNumConflicts()), bodyFontName, bodyTextSize,
                            bodyColor, context));
            noninteractiveDrawables.add(
                    new TextDrawable(new Rectangle(
                            hashColWidth + authorColWidth + (cellMargin * 8) + timestampColWidth + conflictsColWidth,
                            rowTop, checkmarksLeftMargin, rowBottom), 0,
                            Integer.toString(numsFilesModified.get(commit)), bodyFontName, bodyTextSize,
                            bodyColor, context));
            for(int i = 0; i < selectedComponents.size(); i++) {
                String component = selectedComponents.get(i);
                
                if(!commit.getComponentsModified().contains(component)) {
                    continue;
                }
                
                float x1 = checkmarksLeftMargin + (bodyTextSize * i)
                    + (cellMargin * (2 * i + 1));
                float x2 = x1 + bodyTextSize;

                Color color = componentColors.get(component);
                
                checkmarks.add(
                    new CheckmarkDrawable(color, commit, component,
                            new Rectangle(x1, rowTop, x2, rowBottom), 1));
            }
        }
        
        /* Add horizontal lines */
        for(int rowNum = 0; rowNum < selectedCommits.size(); rowNum++) {
            float y = headerTextSize + (bodyTextSize * rowNum)
                    + (cellMargin * 2 * (rowNum + 1) - 1);
            noninteractiveDrawables.add(new LineDrawable(
                    LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT,
                    tableLineWidth, lineColor,
                    new Rectangle(0, y, widthOfHorizontalLines, y), 2));
        }
        float bottomLineY = -heightOfHeader + headerTextSize
                + heightOfVerticalLines;
        noninteractiveDrawables.add(new LineDrawable(
                LineDrawable.Direction.TOPRIGHT_TO_BOTTOMLEFT,
                tableLineWidth, lineColor,
                new Rectangle(0, bottomLineY, widthOfHorizontalLines,
                        bottomLineY), 2));
        

        
        return new PatchViewDrawables(checkmarks, noninteractiveDrawables);
    }
    
    private int getNumCharsInLongestString(Iterable<String> strings) {
        int maxLength = 0;
        
        for(String string : strings) {
            int length = string.length();
            if(length > maxLength) {
                maxLength = length;
            }
        }
        
        return maxLength;
    }
        
    private Map<String, Color> createComponentColors(List<String>
            allComponents) {
        final int numComponents = allComponents.size();

        Map<String, Color> componentColors =
                new HashMap<>(numComponents);
        
        for(int i = 0; i < numComponents; i++) {
            String component = allComponents.get(i);
            
            char hue = (char) (((float) i / numComponents) * 255);
            Color color = Color.createHSBColor(hue, Character.MAX_VALUE,
                    Character.MAX_VALUE);
            
            componentColors.put(component, color);
        }
        
        return componentColors;
    }
    
    public static class PatchViewDrawables {
        private final List<CheckmarkDrawable> checkmarks;
        private final List<Drawable> noninteractiveDrawables;

        public PatchViewDrawables(List<CheckmarkDrawable> checkmarks,
                List<Drawable> noninteractiveDrawables) {
            this.checkmarks = checkmarks;
            this.noninteractiveDrawables = noninteractiveDrawables;
        }

        public List<CheckmarkDrawable> getCheckmarks() {
            return checkmarks;
        }

        public List<Drawable> getNoninteractiveDrawables() {
            return noninteractiveDrawables;
        }
    }
}