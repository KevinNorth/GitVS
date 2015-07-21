package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import java.util.ArrayList;
import java.util.List;

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
    public List<AnnotatedCommit> annotateCommits(List<Commit> commitsToAnnotate)
    {
        List<AnnotatedCommit> annotatedCommits =
                new ArrayList<>(commitsToAnnotate.size());
                
        annotatedCommits.add(annotateFirstCommit(commitsToAnnotate.remove(0)));
        
        // After the first commit, there will always be one branch - the branch
        // the current one is on
        int numCurrentBranches = 1;
        
        for(Commit commit : commitsToAnnotate) {
            AnnotatedCommitResult result = annotateCommit(commit,
                    annotatedCommits, commitsToAnnotate, numCurrentBranches);
            numCurrentBranches = result.getNewNumStartingBranches();
            annotatedCommits.add(result.getAnnotatedCommit());
        }
        
        return annotatedCommits;
    }
    
    /**
     * Processes the first commit to decide where to put it in the final
     * visualization.
     * @param commit The first commit that was selected from raw data.
     * @return An AnnotatedCommit representing <code>commit</code>'s location
     * and branch lines in the final visualization.
     */
    private AnnotatedCommit annotateFirstCommit(Commit commit) {
        AnnotatedCommit annotatedCommit = new AnnotatedCommit();
        annotatedCommit.setAuthor(commit.getAuthor());
        annotatedCommit.setHash(commit.getHash());
        annotatedCommit.setBranch(1);
        annotatedCommit.setComponents(commit.getComponentsModified());
        annotatedCommit.setTimestamp(commit.getTimestamp());
        
        // We'll show all of the first commit's parents being merged into it
        // at the very bottom of the commit graph.
        for(int i = 1; i <= commit.getParentHashes().size(); i++) {
            annotatedCommit.getIncomingBranches().add(i);
        }
        
        // It's possible that the first commit is the first commit in the
        // entire repository, in which case it doesn't have any parents.
        // In this case, be aware that annotatedCommit will have an empty
        // incomingBranches list.
        
        return annotatedCommit;
    }
    
    private AnnotatedCommitResult annotateCommit(Commit commit,
            List<AnnotatedCommit> annotatedCommits, List<Commit> commits,
            int numStartingBranches) {

        AnnotatedCommit annotatedCommit = new AnnotatedCommit();
        annotatedCommit.setAuthor(commit.getAuthor());
        annotatedCommit.setHash(commit.getHash());
        annotatedCommit.setComponents(commit.getComponentsModified());
        annotatedCommit.setTimestamp(commit.getTimestamp());
        
        int newNumCurrentBranches = numStartingBranches;
        
        if(commit.getParentHashes().isEmpty()) {
            newNumCurrentBranches++;
            annotatedCommit.setBranch(newNumCurrentBranches);
        } else {
            // Add lines 
            for(String parentHash : commit.getParentHashes()) {
                int parentBranch = lookupParentBranch(parentHash,
                        annotatedCommits);
                if(parentBranch == -1) {
                    // If a parent branch doesn't show up in the list of commits
                    // processed so far, it must be off the bottom of our graph.
                    // So we add a new starting branch at the bottom and connect
                    // our commit to it.
                    newNumCurrentBranches++;
                    annotatedCommit.getIncomingBranches()
                            .add(newNumCurrentBranches);
                } else {
                    annotatedCommit.getIncomingBranches().add(parentBranch);
                }
            }
            
            int minIncomingBranch = Integer.MAX_VALUE;
            for(int incomingBranch : annotatedCommit.getIncomingBranches()) {
                if(incomingBranch < minIncomingBranch) {
                    minIncomingBranch = incomingBranch;
                }
            }
            
            annotatedCommit.setBranch(minIncomingBranch);
        }
        
        DetermineOugoingLinesResults results = determineOutgoingLines(commit,
                commits, annotatedCommit.getBranch(), newNumCurrentBranches);
        newNumCurrentBranches = results.getNewNumCurrentBranches();
        annotatedCommit.setOutgoingBranches(results.getOutgoingLines());
        
        return new AnnotatedCommitResult(annotatedCommit,
                newNumCurrentBranches);
    }
    
    private int lookupParentBranch(String parentHash,
            List<AnnotatedCommit> annotatedCommits) {
        for(AnnotatedCommit annotatedCommit : annotatedCommits) {
            if(parentHash.equals(annotatedCommit.getHash())) {
                // Select one of the branches coming out of the parent that we
                // haven't used yet
                int branch = annotatedCommit.getUnusedOutgoingBranch();
                // And mark it as used so that we don't select it in the future
                annotatedCommit.useOutgoingBranch(branch);
            }
        }
        
        return -1;
    }
    
    private DetermineOugoingLinesResults determineOutgoingLines(
            Commit commitToCheck, List<Commit> commits,
            int commitToCheckBranch, int numCurrentBranches) {
        List<Integer> outgoingBranches = new ArrayList<>();
        int newNumCurrentBranches = numCurrentBranches;
        
        boolean usedCurrentBranch = false;
        for(Commit commit : commits) {
            if(commit.getParentHashes().contains(commitToCheck.getHash())) {
                if(usedCurrentBranch) {
                    newNumCurrentBranches++;
                    outgoingBranches.add(newNumCurrentBranches);
                } else {
                    usedCurrentBranch = true;
                    outgoingBranches.add(commitToCheckBranch);
                }
            }
        }
        
        return new DetermineOugoingLinesResults(outgoingBranches,
                newNumCurrentBranches);
    }
    
    private static final class AnnotatedCommitResult {
        private final AnnotatedCommit annotatedCommit;
        private final int newNumCurrentBranches;

        public AnnotatedCommitResult(AnnotatedCommit annotatedCommit, int newNumCurrentBranches) {
            this.annotatedCommit = annotatedCommit;
            this.newNumCurrentBranches = newNumCurrentBranches;
        }

        public AnnotatedCommit getAnnotatedCommit() {
            return annotatedCommit;
        }

        public int getNewNumStartingBranches() {
            return newNumCurrentBranches;
        }
    }
    
    private static final class DetermineOugoingLinesResults {
        private final List<Integer> outgoingLines;
        private final int newNumCurrentBranches;

        public DetermineOugoingLinesResults(List<Integer> outgoingLines, int nerNumCurrentBranches) {
            this.outgoingLines = outgoingLines;
            this.newNumCurrentBranches = nerNumCurrentBranches;
        }

        public List<Integer> getOutgoingLines() {
            return outgoingLines;
        }

        public int getNewNumCurrentBranches() {
            return newNumCurrentBranches;
        }
    }
}