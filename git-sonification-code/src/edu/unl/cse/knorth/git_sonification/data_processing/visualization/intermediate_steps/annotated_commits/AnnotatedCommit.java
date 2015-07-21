package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class AnnotatedCommit {
    private DateTime timestamp;
    private String hash;
    private String author;
    private int branch;
    private List<Integer> incomingBranches;
    private List<Integer> outgoingBranches;
    private List<Integer> usedOutgoingBranches;
    private List<Component> components;

    public AnnotatedCommit() {
        incomingBranches = new ArrayList<>();
        outgoingBranches = new ArrayList<>();
        usedOutgoingBranches = new ArrayList<>();
        components = new ArrayList<>();
    }

    public AnnotatedCommit(DateTime timestamp, String hash, String author,
            int branch, List<Integer> incomingBranches,
            List<Integer> outgoingBranches, List<Component> components) {
        this.timestamp = timestamp;
        this.hash = hash;
        this.author = author;
        this.branch = branch;
        this.incomingBranches = incomingBranches;
        this.outgoingBranches = outgoingBranches;
        this.usedOutgoingBranches = new ArrayList<>();
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

    public List<Integer> getIncomingBranches() {
        return incomingBranches;
    }

    public void setIncomingBranches(List<Integer> incomingBranches) {
        this.incomingBranches = incomingBranches;
    }

    // Deliberately marked as package visibility because the CommitAnnotator
    // is the only other class that needs access to this method.
    List<Integer> getOutgoingBranches() {
        return outgoingBranches;
    }

    // Deliberately marked as package visibility because the CommitAnnotator
    // is the only other class that needs access to this method.
    void setOutgoingBranches(List<Integer> outgoingBranches) {
        this.outgoingBranches = outgoingBranches;
    }
    
    /**
     * Returns an arbitrary outgoing branch that hasn't been marked as used yet.
     * @return One of the elements of <code>outgoingBranches</code> that hasn't
     * been marked as used by <code>useOutgoingBranch()</code>, or
     * <code>-1</code> if no such branch exists.
     */
    // Deliberately marked as package visibility because the CommitAnnotator
    // is the only other class that needs access to this method.
    int getUnusedOutgoingBranch() {
        for(int outgoingBranch : outgoingBranches) {
            if(!usedOutgoingBranches.contains(outgoingBranch)) {
                return outgoingBranch;
            }
        }
        
        return -1;
    }
    
    /**
     * Marks an outgoing branch as used so that
     * <code>getUnusedOutgoingBranch()</code> won't return it anymore.
     * @param outgoingBranch The number of the branch to mark as used.
     */
    // Deliberately marked as package visibility because the CommitAnnotator
    // is the only other class that needs access to this method.
    void useOutgoingBranch(int outgoingBranch) {
        this.usedOutgoingBranches.add(outgoingBranch);
    }
    
    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "AnnotatedCommit{" + "timestamp=" + timestamp +
                ", hash=" + hash +
                ", author=" + author +
                ", branch=" + branch +
                ", incomingBranches=" + incomingBranches +
                ", outgoingBranches=" + outgoingBranches +
                ", usedOutgoingBranches=" + usedOutgoingBranches +
                ", components=" + components + '}';
    }
}