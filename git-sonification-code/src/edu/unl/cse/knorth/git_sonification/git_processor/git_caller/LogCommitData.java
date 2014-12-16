package edu.unl.cse.knorth.git_sonification.git_processor.git_caller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * A simple POCO that keeps track of the data that the GitCaller obtains for a
 * specific commit.
 */
public class LogCommitData {
    private String author;
    private Calendar datetime;
    private final List<String> parentHashes;
    
    public LogCommitData() {
        author = null;
        datetime = null;
        parentHashes = new ArrayList<>();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Calendar getDatetime() {
        return datetime;
    }

    public void setDatetime(Calendar datetime) {
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
}