package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

public class AnnotatedCommitLine {
    private int fromBranch;
    private int toBranch;
    private int color;
    
    public AnnotatedCommitLine() {}

    public AnnotatedCommitLine(int fromBranch, int toBranch) {
        this.fromBranch = fromBranch;
        this.toBranch = toBranch;
    }
    
    public AnnotatedCommitLine(int fromBranch, int toBranch, int color) {
        this.fromBranch = fromBranch;
        this.toBranch = toBranch;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "AnnotatedCommitLine{" + "fromBranch=" + fromBranch
                + ", toBranch=" + toBranch + '}';
    }
}