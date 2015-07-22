package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import java.util.ArrayList;
import java.util.Collections;
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
                
        AnnotatedCommit firstCommit =
                annotateFirstCommit(commitsToAnnotate.remove(0));
        annotatedCommits.add(firstCommit);
                
        AnnotatedCommit previousCommit = firstCommit;
        for(Commit commit : commitsToAnnotate) {
            AnnotatedCommit result = annotateCommit(commit, previousCommit,
                    annotatedCommits);
            annotatedCommits.add(result);
            previousCommit = result;
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
            annotatedCommit.getIncomingBranches().add(
                new AnnotatedCommitLine(i, 1, commit.getHash()));
        }
        
        // It's possible that the first commit is the first commit in the
        // entire repository, in which case it doesn't have any parents.
        // In this case, be aware that annotatedCommit will have an empty
        // incomingBranches list.
        return annotatedCommit;
    }
    
    private AnnotatedCommit annotateCommit(Commit commit,
            AnnotatedCommit previousCommit,
            List<AnnotatedCommit> annotatedCommits) {

        AnnotatedCommit annotatedCommit = new AnnotatedCommit();
        annotatedCommit.setAuthor(commit.getAuthor());
        annotatedCommit.setHash(commit.getHash());
        annotatedCommit.setComponents(commit.getComponentsModified());
        annotatedCommit.setTimestamp(commit.getTimestamp());
        
        if(commit.getParentHashes().isEmpty()) {
            int newBranch = addNewStartingBranch(annotatedCommits);
            annotatedCommit.setBranch(newBranch);
        } else {
            // Get the lowest-numbered branch that comes from one of this
            // commit's parents. If none of the commit's parents can be reached
            // without branching, we'll end up creating a new branch for the
            // commit anyway, so put it in a new branch number.
            int newBranch = previousCommit.getMaxToBranchNumber() + 1;
            
            for(AnnotatedCommitLine line : previousCommit.getIncomingBranches())
            {
                if(commit.getParentHashes().contains(line.getParentHash())) {
                    if(line.getToBranch() < newBranch) {
                        newBranch = line.getToBranch();
                    }
                }
            }
            
            annotatedCommit.setBranch(newBranch);
        }
        
        int numBranchesMerged = 0;
        int commitBranch = annotatedCommit.getBranch();
        List<String> checkedParents = new ArrayList<>();
        List<AnnotatedCommitLine> lines = previousCommit.getIncomingBranches();
        Collections.sort(lines, new AnnotatedCommitLineToBranchComparator());
        
        for(AnnotatedCommitLine line : lines) {
            if(commit.getParentHashes().contains(line.getParentHash())) {
                annotatedCommit.getIncomingBranches().add(
                    new AnnotatedCommitLine(line.getToBranch(), commitBranch,
                        commit.getHash()));
                checkedParents.add(line.getParentHash());
            } else {
                // If a line doesn't pass through 
                annotatedCommit.getIncomingBranches().add(
                    new AnnotatedCommitLine(line.getToBranch(),
                            line.getToBranch() - numBranchesMerged,
                            line.getParentHash()));
            }
        }
        
        for(String parent : commit.getParentHashes()) {
            if(!checkedParents.contains(parent)) {
                int newFromBranch = addNewBranchFromParent(parent,
                        annotatedCommits);
                annotatedCommit.getIncomingBranches().add(
                        new AnnotatedCommitLine(newFromBranch, commitBranch,
                                commit.getHash()));
            }
        }
        
        return annotatedCommit;
    }
    
    /**
     * Adds a new branch off the bottom of the graph that can be used to place
     * a commit that has no parents, or that has parents outside of the portion
     * of the history being visualized.
     * @param annotatedCommits The list of annotated commits so far. They will
     * be modified to add a new branch.
     * @return The branch number of the new branch that should be used with the
     * new parentless commit the starting branch was added for.
     */
    private int addNewStartingBranch(List<AnnotatedCommit> annotatedCommits) {
        for(AnnotatedCommit annotatedCommit : annotatedCommits) {
            annotatedCommit.getIncomingBranches().add(new AnnotatedCommitLine(
                    annotatedCommit.getMaxFromBranchNumber() + 1,
                    annotatedCommit.getMaxToBranchNumber() + 1, null));
        }
        
        return annotatedCommits.get(annotatedCommits.size() - 1)
                .getMaxToBranchNumber();
    }
    
    private int addNewBranchFromParent(String parentHash,
            List<AnnotatedCommit> annotatedCommits) {
        boolean addingNewBranch = false;
        int previousBranch = 0;
        
        for(AnnotatedCommit annotatedCommit : annotatedCommits) {
            if(!addingNewBranch) {
                if(annotatedCommit.getHash().equals(parentHash)) {
                    addingNewBranch = true;
                    previousBranch = annotatedCommit.getBranch();
                }
            } else {
                int newBranch = annotatedCommit.getMaxToBranchNumber() + 1;
                annotatedCommit.getIncomingBranches().add(
                        new AnnotatedCommitLine(previousBranch, newBranch,
                                parentHash));
                previousBranch = newBranch;
            }
        }
        
        return previousBranch;
    }
}