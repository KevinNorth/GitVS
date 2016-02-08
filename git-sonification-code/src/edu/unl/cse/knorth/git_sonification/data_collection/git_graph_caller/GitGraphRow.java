package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class GitGraphRow {
    private final String commitHash;
    private final int commitBranchPosition;
    private final ArrayList<GitGraphLine> incomingLines;

    public GitGraphRow(String commitHash, int commitBranchPosition,
            Collection<GitGraphLine> incomingLines) {
        this.commitHash = commitHash;
        this.commitBranchPosition = commitBranchPosition;
        this.incomingLines = new ArrayList<>(incomingLines);
    }

    public String getCommitHash() {
        return commitHash;
    }

    public int getCommitBranchPosition() {
        return commitBranchPosition;
    }
    
    public ArrayList<GitGraphLine> getIncomingLines() {
        return incomingLines;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + commitHash.hashCode();
        hash = 67 * hash + commitBranchPosition;
        hash = 67 * hash + Objects.hashCode(this.incomingLines);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final GitGraphRow other = (GitGraphRow) obj;
        if (this.commitBranchPosition != other.commitBranchPosition) {
            return false;
        }
        if (!this.commitHash.equals(other.commitHash))
        {
            return false;
        }
        if (!Objects.equals(this.incomingLines, other.incomingLines))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GitGraphRow{"
                + "commitHash=" + commitHash
                + ", commitBranchPosition=" + commitBranchPosition
                + ", incomingLines=" + incomingLines + '}';
    }
}