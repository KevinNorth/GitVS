package edu.unl.cse.knorth.git_sonification.commit_processor.commit_filter;

import edu.unl.cse.knorth.git_sonification.git_caller.PartialCommit;
import java.util.LinkedList;
import java.util.List;

/**
 * Takes a list of <code>PartialCommit</code>s and filters it, returning a
 * subset of the commits that fulfill some criteria.
 */
public abstract class CommitFilter {
    /**
    * Takes a list of <code>PartialCommit</code>s and filters it, returning a
    * subset of the commits that fulfill the criteria implemented in the
    * particular subclass.
     * @param partialCommits The list of <code>PartialCommit</code>s to filter.
     * @return A subset of <code>partialCommit</code> that fulfills a criteria
     * specified by the particular subclass.
     */
    public final List<PartialCommit>
        filterCommits(List<PartialCommit> partialCommits) {
        List<PartialCommit> filteredCommits = new LinkedList<>();
        
        for(PartialCommit partialCommit : partialCommits) {
            if(shouldSelectCommit(partialCommit)) {
                filteredCommits.add(partialCommit);
            }
        }
        
        return filteredCommits;
    }
    
    /**
     * Determines whether a <code>PartialCommit</code> is selected or filtered
     * out.
     * @param commit The <code>PartialCommit</code> to consider.
     * @return <code>true</code> if this <code>CommitFilter</code> should select
     * the specified <code>commit</code>. <code>false</code> if the specified
     * <code>commit</code> should be filtered out.
     */
    protected abstract boolean shouldSelectCommit(PartialCommit commit);
}