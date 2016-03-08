package edu.unl.cse.knorth.git_sonification.data_collection.commit_processor.commit_filter;

import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraph;

/**
 * Filters commits based on when they were made, selecting the commits that fall
 * in-between two specified hashes.
 * @author knorth
 */
public class BetweenHashesCommitFilter extends CommitFilter {
    private final String firstHash;
    private final String lastHash;
    private final GitGraph gitGraph;

    public BetweenHashesCommitFilter(String firstHash, String lastHash,
            GitGraph gitGraph) {
        this.firstHash = firstHash;
        this.lastHash = lastHash;
        this.gitGraph = gitGraph;
    }
    
    /**
     * Determines whether the given commit was made in-between the two specified
     * commits passed to this <code>BetweenHashesCommitFilter</code>'s
     * constructor.
     * @param commit The commit to check
     * @return <code>true</code> if the date the commit was made falls in-
     * between <code>since</code> and <code>until</code> (inclusively).
     * <code>false</code> otherwise.
     */
    @Override
    protected boolean shouldSelectCommit(PartialCommit commit) {
        int positionOfCommit = gitGraph.getPositionOfCommit(commit.getHash());
        
        return (positionOfCommit <= gitGraph.getPositionOfCommit(firstHash))
            && (positionOfCommit >= gitGraph.getPositionOfCommit(lastHash));
    }

    public String getFirstHash() {
        return firstHash;
    }

    public String getLastHash() {
        return lastHash;
    }

    public GitGraph getGitGraph() {
        return gitGraph;
    }
}