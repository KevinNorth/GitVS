package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import java.util.HashSet;
import java.util.Set;

public class AnnotatedCommitLine {
    private int fromBranch;
    private int toBranch;

    public AnnotatedCommitLine() {}
    
    public AnnotatedCommitLine(int fromBranch, int toBranch) {
        this.fromBranch = fromBranch;
        this.toBranch = toBranch;
    }

    public int getFromBranch() {
        return fromBranch;
    }

    public void setFromBranch(int fromBranch) {
        this.fromBranch = fromBranch;
    }

    public int getToBranch() {
        return toBranch;
    }

    public void setToBranch(int toBranch) {
        this.toBranch = toBranch;
    }

    @Override
    public String toString() {
        return "AnnotatedCommitLine{" + "fromBranch=" + fromBranch
                + ", toBranch=" + toBranch + '}';
    }
}