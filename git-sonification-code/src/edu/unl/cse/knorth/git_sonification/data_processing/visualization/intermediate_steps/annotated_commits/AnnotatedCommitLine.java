package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import java.util.HashSet;
import java.util.Set;

public class AnnotatedCommitLine {
    private int fromBranch;
    private int toBranch;
    private String parentHash;
    private Set<Component> componentsModified;

    public AnnotatedCommitLine() {}
    
    public AnnotatedCommitLine(int fromBranch, int toBranch, String parentHash) {
        this.fromBranch = fromBranch;
        this.toBranch = toBranch;
        this.parentHash = parentHash;
        this.componentsModified = new HashSet<>();
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

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public Set<Component> getComponentsModified() {
        return componentsModified;
    }

    public void setComponentsModified(Set<Component> componentsModified) {
        this.componentsModified = componentsModified;
    }
    
    @Override
    public String toString() {
        return "AnnotatedCommitLine{" + "fromBranch=" + fromBranch +
                ", toBranch=" + toBranch +
                ", parentHash=" + parentHash + '}';
    }
}