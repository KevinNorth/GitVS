package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

public class GitGraphLine {
    private int fromBranch;
    private int toBranch;

    public GitGraphLine(int fromBranch, int toBranch) {
        this.fromBranch = fromBranch;
        this.toBranch = toBranch;
    }

    public int getFromBranch() {
        return fromBranch;
    }

    public int getToBranch() {
        return toBranch;
    }

    public void setFromBranch(int fromBranch) {
        this.fromBranch = fromBranch;
    }

    public void setToBranch(int toBranch) {
        this.toBranch = toBranch;
    }
}