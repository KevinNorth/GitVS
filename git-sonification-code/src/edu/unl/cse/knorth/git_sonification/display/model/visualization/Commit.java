package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import java.util.List;
import org.joda.time.DateTime;

public class Commit {
    private final String hash;
    private final String author;
    private final DateTime timestamp;
    private final List<String> componentsModified;

    public Commit(String hash, String author, DateTime timestamp,
            List<String> componentsModified) {
        this.hash = hash;
        this.author = author;
        this.timestamp = timestamp;
        this.componentsModified = componentsModified;
    }

    public String getHash() {
        return hash;
    }

    public String getAuthor() {
        return author;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getComponentsModified() {
        return componentsModified;
    }
}