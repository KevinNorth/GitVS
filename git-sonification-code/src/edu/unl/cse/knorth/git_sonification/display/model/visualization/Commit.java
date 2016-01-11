package edu.unl.cse.knorth.git_sonification.display.model.visualization;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import java.util.List;
import org.joda.time.DateTime;

public class Commit {
    private final String hash;
    private final String author;
    private final DateTime timestamp;
    private final List<Component> componentsModified;

    public Commit(String hash, String author, DateTime timestamp, List<Component> componentsModified) {
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

    public List<Component> getComponentsModified() {
        return componentsModified;
    }
}