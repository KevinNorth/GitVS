package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

public class GitGraphLine {
    private int fromBranch;
    private int toBranch;
    private GitGraphLine markerLine; // Used in the algorithm to parse the
                                     // output of git log --graph

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

    // Package visibility is deliberate - the markerLine field is only used by
    // an algorithm in the GitGraphProducer class.
    GitGraphLine getMarkerLine() {
        return markerLine;
    }

    // Package visibility is deliberate - the markerLine field is only used by
    // an algorithm in the GitGraphProducer class.
    void setMarkerLine(GitGraphLine markerLine) {
        this.markerLine = markerLine;
    }
    
    @Override
    public String toString() {
        return "[" + fromBranch + " -> " + toBranch + "]";
    }
}