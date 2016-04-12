package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class AnnotatedCommit {
    private DateTime timestamp;
    private String hash;
    private String author;
    private int branch;
    private List<AnnotatedCommitLine> incomingBranches;
    private List<String> components;

    public AnnotatedCommit() {
        incomingBranches = new ArrayList<>();
        components = new ArrayList<>();
    }

    public AnnotatedCommit(DateTime timestamp, String hash, String author,
            int branch, List<AnnotatedCommitLine> incomingBranches,
            List<String> components) {
        this.timestamp = timestamp;
        this.hash = hash;
        this.author = author;
        this.branch = branch;
        this.incomingBranches = incomingBranches;
        this.components = components;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public List<AnnotatedCommitLine> getIncomingBranches() {
        return incomingBranches;
    }

    public void setIncomingBranches(List<AnnotatedCommitLine> incomingBranches) {
        this.incomingBranches = incomingBranches;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public int getMaxFromBranchNumber() {
        if(incomingBranches.isEmpty()) {
            return 0;
        }
        
        int max = incomingBranches.get(0).getFromBranch();
        
        for(AnnotatedCommitLine line : incomingBranches) {
            if(line.getFromBranch() > max) {
                max = line.getFromBranch();
            }
        }
        
        return max;
    }
    
    public int getMaxToBranchNumber() {
        if(incomingBranches.isEmpty()) {
            return this.branch;
        }
        
        int max = incomingBranches.get(0).getToBranch();
        
        for(AnnotatedCommitLine line : incomingBranches) {
            if(line.getToBranch() > max) {
                max = line.getToBranch();
            }
        }
        
        return max;
    }
    
    @Override
    public String toString() {
        return "AnnotatedCommit{" + "timestamp=" + timestamp +
                ", hash=" + hash +
                ", author=" + author +
                ", branch=" + branch +
                ", incomingBranches=" + incomingBranches +
                ", components=" + components + '}';
    }
}