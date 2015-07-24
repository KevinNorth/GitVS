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

        // Determine which branch the commit should be placed on
        if(commit.getParentHashes().isEmpty()) {
            int newBranch = previousCommit.getMaxToBranchNumber() + 1;
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
        
        // If the commit doesn't have any parents, add a new starting branch for
        // it
        if(commit.getParentHashes().isEmpty()) {
            addNewStartingBranch(annotatedCommits);
        }
        
        // Extend all of the previous commit's lines to continue through the
        // current commit. If we merge branches, move outside branches in to
        // fill the space the merged branches create
        int commitBranch = annotatedCommit.getBranch();
        List<String> checkedParents = new ArrayList<>();
        List<Integer> branchesExtended = new ArrayList<>();
        List<Integer> branchesMerged = new ArrayList<>();
        List<AnnotatedCommitLine> lines = previousCommit.getIncomingBranches();
        Collections.sort(lines, new AnnotatedCommitLineToBranchComparator());
        
        // First, connect all lines with the current commits' parents at the
        // front 
        for(AnnotatedCommitLine line : lines) {
            if(commit.getParentHashes().contains(line.getParentHash())
                    && !branchesExtended.contains(line.getToBranch())) {
                // If a branch merges into the current branch, we simply add a
                // line to the current branch.
                annotatedCommit.getIncomingBranches().add(
                    new AnnotatedCommitLine(line.getToBranch(), commitBranch,
                        commit.getHash()));
                checkedParents.add(line.getParentHash());
                branchesExtended.add(line.getToBranch());
                branchesMerged.add(line.getToBranch());
            }
        }
        
        for(AnnotatedCommitLine line : lines) {
            if(!branchesExtended.contains(line.getToBranch())) {
                // If a branch doesn't connect to the current commit, add a line
                // continuing the branch parallel to the current commit. If
                // we've merged two or more branches into the parent, outside
                // branches will move inside to fill in the created space.
                int numBranchesMerged = 0;
                int numBranchesToMoveInwards = 0;
                
                for(int branch : branchesMerged) {
                    if(line.getToBranch() > branch) {
                        numBranchesMerged++;
                    }
                }
                
                if(numBranchesMerged >= 2) {
                    numBranchesToMoveInwards = numBranchesMerged - 1;
                }
                
                // Note that, conviniently, if a commit has no parents, then
                // all of the previous commits' branches will simply be moved
                // straight up by this algorithm. Since we've already added an
                // additional starting branch to the graph, this will connect
                // the current commit for us automagically. The only thing we
                // have to do is change the new line's parent from null to the
                // current commit.
                String newLineParent;
                if(line.getToBranch() - numBranchesToMoveInwards ==
                        annotatedCommit.getBranch()) {
                    newLineParent = commit.getHash();
                } else {
                    newLineParent = line.getParentHash();
                }
                annotatedCommit.getIncomingBranches().add(
                    new AnnotatedCommitLine(line.getToBranch(),
                        line.getToBranch() - numBranchesToMoveInwards,
                        newLineParent));
                
                branchesExtended.add(line.getToBranch());
            }
        }
        
        // Finally, any parents that weren't merged into the current commit's
        // branch must be below the cutoff of the Git data we're processing.
        // Add a new starting branch for the commit accordingly.
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
        
        // If we never added a new branch, then the parent must have been
        // earlier than the first commit in the Git data we're analyzing. Create
        // a new starting branch accordingly.
        if(!addingNewBranch) {
            for(AnnotatedCommit annotatedCommit : annotatedCommits) {
                annotatedCommit.getIncomingBranches().add(new AnnotatedCommitLine(
                        annotatedCommit.getMaxFromBranchNumber() + 1,
                        annotatedCommit.getMaxToBranchNumber() + 1, null));
            }
            return annotatedCommits.get(annotatedCommits.size() - 1)
                .getMaxToBranchNumber();
        } else {
            return previousBranch;
        }
    }
}