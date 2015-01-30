package edu.unl.cse.knorth.git_sonification.git_caller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A simple POCO that keeps track of the data that the GitCaller obtains for a
 * specific commit.
 */
public class PartialCommit {
    private String hash;
    private String author;
    private Date datetime;
    private final List<String> parentHashes;
    
    public PartialCommit() {
        hash = null;
        author = null;
        datetime = null;
        parentHashes = new ArrayList<>();
    }

    public PartialCommit(String hash, String author, Date datetime,
            Collection<String> parentHashes) {
        this.hash = hash;
        this.author = author;
        this.datetime = datetime;
        this.parentHashes = new ArrayList<>();
        this.parentHashes.addAll(parentHashes);
    }

    public PartialCommit(String hash, String author, Date datetime,
            String... parentHashes) {
        this.hash = hash;
        this.author = author;
        this.datetime = datetime;
        this.parentHashes = new ArrayList<>();
        this.parentHashes.addAll(Arrays.asList(parentHashes));
    }

    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public List<String> getParentHashes() {
        return parentHashes;
    }
    
    public void addParentHash(String parentHash) {
        parentHashes.add(parentHash);
    }
    
    public void addParentHashes(String... parentHashes) {
        this.parentHashes.addAll(Arrays.asList(parentHashes));
    }
    
    public void addParentHashes(Collection<String> parentHashses) {
        this.parentHashes.addAll(parentHashes);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        StringBuilder parentHashesString = new StringBuilder();
        
        for(String parentHash : parentHashes) {
            parentHashesString.append(parentHash);
            parentHashesString.append(' ');
        }
        
        return "PartialCommit{" + "hash=" + hash + ", author=" + author +
                ", datetime=" + datetime.toString() + ", parentHashes="
                + parentHashesString.toString() + '}';
    }
}