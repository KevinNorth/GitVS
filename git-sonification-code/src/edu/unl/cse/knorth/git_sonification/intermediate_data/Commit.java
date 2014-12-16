package edu.unl.cse.knorth.git_sonification.intermediate_data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Keeps track of all of the information that is needed to correctly sonify a
 * commit.
 */
public class Commit {
    private String author;
    private boolean introducesConflict;
    private Calendar timestamp;
    private String hash;
    private final List<String> parentHashes;
    private String resolvedConflictHash;
    
    /**
     * Creates an empty <code>Commit</code>.
     */
    public Commit() {
        parentHashes = new ArrayList<>();
    };

    /**
     * Gets a string containing the name of the developer who made this commit.
     * @return A string containing the name of the developer who made this
     * commit
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the string containing the name of the developer who made this
     * commit.
     * @param author A String containing the name of the developer who made this
     * commit 
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Indicates whether this commit introduces a conflict.
     * @return <code>true</code> if this commit introduces a conflict.
     * <code>false</code> otherwise.
     */
    public boolean isIntroducesConflict() {
        return introducesConflict;
    }

    /**
     * Sets whether this commit introduces a conflict.
     * @param introducesConflict Pass <code>true</code> to indicate that this
     * commit introduces a conflict. Pass <code>false</code> to indicate that
     * this commit does <b>not</b> introduce a conflict.
     */
    public void setIntroducesConflict(boolean introducesConflict) {
        this.introducesConflict = introducesConflict;
    }

    /**
     * Gets the date and time that this commit was made on.
     * @return The date and time that this commit was made on.
     */
    public Calendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the date and time that this commit was made on.
     * @param timestamp The date and time that this commit was made on.
     */
    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets this commit's hash. In git, a hash can be used to uniquely identify
     * a commit, much like an ID on a record in a database.
     * @return This commit's hash.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets this commit's hash. In git, a hash can be used to uniquely identify
     * a commit, much like an ID on a record in a database.
     * @param hash This commit's hash.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Gets a list containing all of this commit's parents' hashes.
     * @return A <code>List</code> of <code>String</code>s containing all of
     * this commit's parents' hashes.
     */
    public List<String> getParentHashes() {
        return parentHashes;
    }

    /**
     * Adds a hash as one of this commit's parents' hashes.
     * @param parentHash One of the hashes belonging to one of this commit's
     * parents.
     */
    public void addParentHash(String parentHash) {
        this.parentHashes.add(parentHash);
    }
    
    /**
     * Adds multiple hashes as some of this commit's parents' hashes.
     * @param parentHashes Some of the hashes belonging to some of this commit's
     * parents.
     */
    public void addParentHashes(String... parentHashes) {
        this.parentHashes.addAll(Arrays.asList(parentHashes));
    }
    
    /**
     * Adds multiple hashes as some of this commit's parents' hashes.
     * @param parentHashes Some of the hashes belonging to some of this commit's
     * parents.
     */
    public void addParentHashes(Collection<String> parentHashes) {
        this.parentHashes.addAll(parentHashes);
    }

    /**
     * If this commit resolves a conflict, this method gets the hash of the
     * commit that introduced the conflict being resolved. Otherwise, if this
     * commit does not resolve a conflict, this method returns
     * <code>null</code>.
     * @return If this commit resolves a conflict, this method gets the hash of
     * the commit that introduced the conflict being resolved. Otherwise, if 
     * this commit does not resolve a conflict, this method returns
     * <code>null</code>.
     */
    public String getResolvedConflictHash() {
        return resolvedConflictHash;
    }

    /**
     * If this commit resolves a conflict, pass this method the hash of the
     * conflict that introduced the conflict being resolved. Otherwise, if this
     * commit does not resolve a conflict, pass in <code>null</code>.
     * @param resolvedConflictHash If this commit resolves a conflict, pass this
     * method the hash of the conflict that introduced the conflict being
     * resolved. Otherwise, if this commit does not resolve a conflict, pass in
     * <code>null</code>.
     */
    public void setResolvedConflictHash(String resolvedConflictHash) {
        this.resolvedConflictHash = resolvedConflictHash;
    }
    
    
}