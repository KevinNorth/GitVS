package edu.unl.cse.knorth.git_sonification.data_collection.git_caller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.joda.time.DateTime;

/**
 * A simple POCO that keeps track of the data that the GitCaller obtains for a
 * specific commit.
 */
public class PartialCommit {
    private String hash;
    private String author;
    private DateTime datetime;
    private final List<String> parentHashes;
    private final List<String> filesModified;
    
    public PartialCommit() {
        hash = null;
        author = null;
        datetime = null;
        parentHashes = new ArrayList<>();
        filesModified = new ArrayList<>();
    }

    public PartialCommit(String hash, String author, DateTime datetime,
            Collection<String> parentHashes, Collection<String> filesModified) {
        this.hash = hash;
        this.author = author;
        this.datetime = datetime;
        this.parentHashes = new ArrayList<>(parentHashes);
        this.filesModified = new ArrayList<>(filesModified);
    }
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(DateTime datetime) {
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

    public List<String> getFilesModified() {
        return filesModified;
    }
    
    public void addFileModified(String fileModified) {
        filesModified.add(fileModified);
    }
    
    public void addFilesModified(String... filesModified) {
        this.filesModified.addAll(Arrays.asList(filesModified));
    }
    
    public void addFilesModified(Collection<String> filesModified) {
        this.filesModified.addAll(filesModified);
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
        StringBuilder filesModifiedString = new StringBuilder();
        
        for(String parentHash : parentHashes) {
            parentHashesString.append(parentHash);
            parentHashesString.append(' ');
        }
        
        for(String file : filesModified) {
            filesModifiedString.append(file);
            filesModifiedString.append(' ');
        }
        
        return "PartialCommit{" + "hash=" + hash + ", author=" + author +
                ", datetime=" + datetime.toString() + ", parentHashes="
                + parentHashesString.toString() + ", filesModified="
                + filesModifiedString.toString() + '}';
    }
}