package edu.unl.cse.knorth.git_sonification.data_collection.git_caller;

import java.util.List;
import java.util.Map;

public class PartialCommitsAndAuthorCommitCounts {
    private final List<PartialCommit> partialCommits;
    private final Map<String, Integer> authorCommitCounts;

    public PartialCommitsAndAuthorCommitCounts(List<PartialCommit> partialCommits, Map<String, Integer> authorCommitCounts) {
        this.partialCommits = partialCommits;
        this.authorCommitCounts = authorCommitCounts;
    }

    public List<PartialCommit> getPartialCommits() {
        return partialCommits;
    }

    public Map<String, Integer> getAuthorCommitCounts() {
        return authorCommitCounts;
    }
}