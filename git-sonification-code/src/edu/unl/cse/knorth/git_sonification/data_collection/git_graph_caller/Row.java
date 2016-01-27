package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

import java.util.Objects;

public class Row {
    private final String commitHash;

    public Row(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getCommitHash() {
        return commitHash;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.commitHash);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Row other = (Row) obj;
        if (!Objects.equals(this.commitHash, other.commitHash)) {
            return false;
        }
        return true;
    }
}