package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraph;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraphLine;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraphRow;
import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * Processes commits to determine where they should appear along the time
 * and branch axes in the final visualization. This class produces
 * "annotated commits" containing this positioning information. These
 * annotated commits can then be processed to determine which commits and
 * branch lines should be shown in each z-dimensional layer, but in a
 * future step.
 * @author ThePondermatic
 */
public class CommitAnnotator {
    /**
     * Processes raw commits to determine and attach the following information
     * for them:
     * <ul>
     * <li>Where they should appear vertically in the final visualization,
     * determined by timestamp and order in the commit list</li>
     * <li>Where they should appear horizontally in the final visualization,
     * determined by assigning a branch number</li>
     * <li>Which visual lines should appear to show branches merging and
     * splitting off, determined by giving each commit a list of incoming
     * branches to draw lines for</li>
     * </ul>
     * 
     * All of this information will be further processed, but in a future step
     * (i.e. not this class).
     * @param commitsToAnnotate The raw commits to process
     * @return A list of AnnotatedCommits containing the additional information
     * described above.
     */
    public List<AnnotatedCommit> annotateCommits(List<Commit> commitsToAnnotate,
            GitGraph gitGraph)
    {
        List<AnnotatedCommit> annotatedCommits =
                new ArrayList<>(commitsToAnnotate.size());

        Map<Integer, Integer> branchColors =
                getInitialBranchColors(commitsToAnnotate.get(0), gitGraph);
        
        for(Commit commit : commitsToAnnotate) {
            AnnotatedCommitAndBranchColors result = annotateCommit(commit,
                    gitGraph, branchColors);
            annotatedCommits.add(result.getAnnotatedCommit());
            branchColors = result.getBranchColors();
        }
        
        return annotatedCommits;
    }

    private AnnotatedCommitAndBranchColors annotateCommit(Commit commit,
            GitGraph gitGraph, Map<Integer, Integer> branchColors) {
        GitGraphRow row = gitGraph.getRowForCommit(commit.getHash());
        
        Map<Integer, Integer> newBranchColors = new HashMap<>();
        newBranchColors.putAll(branchColors);
        
        DateTime timestamp = commit.getTimestamp();
        String hash = commit.getHash();
        String author = commit.getAuthor();
        List<Component> components = commit.getComponentsModified();
        
        int branch = row.getCommitBranchPosition();

        List<GitGraphLine> rowLines = row.getIncomingLines();
        List<AnnotatedCommitLine> lines = new ArrayList<>(rowLines.size());
        for(GitGraphLine rowLine : rowLines) {
            int color = 1;
            final int toBranch = rowLine.getToBranch();
            final int fromBranch = rowLine.getFromBranch();
            
            if(toBranch == fromBranch) {
                color = branchColors.get(toBranch);
                newBranchColors.put(toBranch, color);
            } else if(toBranch > fromBranch) /* going to the right */ {
               GitGraphLine closestLeftLine = getClosestLineGoingToLeft(toBranch,
                       row);
               
               if(closestLeftLine != null) {
                   // The line is merging to the right
                   // Match the color of the branch being pulled into
                   color = branchColors.get(toBranch);
                   newBranchColors.put(toBranch, color);
               } else {
                   // The line is going to the right, but it's not merging
                   // See whether to generate a new color
                   GitGraphLine leftmostLine = getLeftmostLine(fromBranch, row);
                   if(rowLine == leftmostLine) {
                       color = branchColors.get(fromBranch);
                       newBranchColors.put(toBranch, color);
                   } else {
                       color = newColor(branchColors, newBranchColors);
                       newBranchColors.put(toBranch, color);
                   }
               }
            } else /* going to the left */ {
               GitGraphLine closestLeftLine = getClosestLineGoingToLeft(toBranch,
                       row);
               
               if(closestLeftLine == rowLine) {
                   color = branchColors.get(fromBranch);
                   newBranchColors.put(toBranch, color);
               } else {
                   color = branchColors.get(fromBranch);
               }
            }            
            
            lines.add(new AnnotatedCommitLine(rowLine.getFromBranch(), 
                    rowLine.getToBranch(), color));
        }
        
        AnnotatedCommit annotatedCommit = new AnnotatedCommit(timestamp, hash,
                author, branch, lines, components);
        return new AnnotatedCommitAndBranchColors(annotatedCommit,
                newBranchColors);
    }

    private Map<Integer, Integer> getInitialBranchColors(Commit firstCommit,
            GitGraph gitGraph) {
        Map<Integer, Integer> branchColors = new HashMap<>();
        
        GitGraphRow firstRow = gitGraph.getRowForCommit(firstCommit.getHash());

        int maxFromBranch = 0;
        for(GitGraphLine line : firstRow.getIncomingLines()) {
            int fromBranch = line.getFromBranch();
            if(fromBranch > maxFromBranch) {
                maxFromBranch = fromBranch;
            }
        }
        
        for(int branch = 1; branch <= maxFromBranch; branch++) {
            branchColors.put(branch, branch);
        }
        
        if(branchColors.isEmpty()) {
            branchColors.put(1, 1);
        }
        
        return branchColors;
    }

    private GitGraphLine getClosestLineGoingToLeft(int toBranch,
            GitGraphRow row) {
        GitGraphLine closestLeftLine = null;
        for(GitGraphLine line : row.getIncomingLines()) {
            if(line.getToBranch() == toBranch) {
                if(line.getToBranch() > line.getFromBranch()) {
                    continue;
                }
                if(closestLeftLine != null) {
                    if(line.getFromBranch() < closestLeftLine.getFromBranch()) {
                        closestLeftLine = line;
                    }
                } else {
                    closestLeftLine = line;
                }
            }
        }
        
        return closestLeftLine;
    }

    private GitGraphLine getLeftmostLine(int fromBranch, GitGraphRow row) {
        GitGraphLine leftmostLine = null;
        for(GitGraphLine line : row.getIncomingLines()) {
            if(line.getFromBranch() == fromBranch) {
                if(leftmostLine != null) {
                    if(line.getToBranch() < leftmostLine.getToBranch()) {
                        leftmostLine = line;
                    }
                } else {
                    leftmostLine = line;
                }
            }
        }
        
        return leftmostLine;
    }

    private int newColor(Map<Integer, Integer> branchColors,
            Map<Integer, Integer> newBranchColors) {
        int maxColor = 0;
        
        for(int color : branchColors.values()) {
            if(color > maxColor) {
                maxColor = color;
            }
        }

        for(int color : newBranchColors.values()) {
            if(color > maxColor) {
                maxColor = color;
            }
        }
            
        return maxColor + 1;
    }
    
    private static class AnnotatedCommitAndBranchColors {
        private final AnnotatedCommit annotatedCommit;
        private final Map<Integer, Integer> branchColors;

        public AnnotatedCommitAndBranchColors(AnnotatedCommit annotatedCommit,
                Map<Integer, Integer> branchColors) {
            this.annotatedCommit = annotatedCommit;
            this.branchColors = branchColors;
        }

        public AnnotatedCommit getAnnotatedCommit() {
            return annotatedCommit;
        }

        public Map<Integer, Integer> getBranchColors() {
            return branchColors;
        }
    }
}