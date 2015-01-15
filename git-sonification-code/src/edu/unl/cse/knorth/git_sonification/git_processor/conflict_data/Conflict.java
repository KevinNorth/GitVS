package edu.unl.cse.knorth.git_sonification.git_processor.conflict_data;

/**
 * Keeps track of when a conflict was introduced and then resolved during a git
 * repository's history.
 */
public class Conflict {
    private final String commitIntroducingConflict;
    private final String commitResolvingConflict;

    /**
     * @param commitIntroducingConflict The hash of the commit that introduced
     * the conflict.
     * @param commitResolvingConflict The hash of the commit that resolved the
     * conflict.
     */
    public Conflict(String commitIntroducingConflict, String commitResolvingConflict) {
        this.commitIntroducingConflict = commitIntroducingConflict;
        this.commitResolvingConflict = commitResolvingConflict;
    }

    /**
     * Gets the hash of the commit that introduced the conflict.
     * @return The hash of the commit that introduced the conflict.
     */
    public String getCommitIntroducingConflict() {
        return commitIntroducingConflict;
    }
    
    /**
     * Gets the hash of the commit that resolved the conflict.
     * @return The hash of the commit that resolved the conflict.
     */
    public String getCommitResolvingConflict() {
        return commitResolvingConflict;
    }
    
    @Override
    public String toString() {
        return "[Conflict] Introduced: " + commitIntroducingConflict +
                " | Resolved: " + commitResolvingConflict;
    }
}