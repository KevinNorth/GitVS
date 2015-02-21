package edu.unl.cse.knorth.git_sonification.sonifier;

/**
 * A POCO that represents a measure of music that will be sonified.
 */
public final class Measure {
    private final String author;
    private final boolean inConflict;
    private final boolean isDaySeparator;

    /**
     * @param author The author of the commit to be sonified. If this measure
     * represents a day separator, this parameter will be ignored and can be
     * <code>null</code>.
     * @param inConflict <code>true</code> if this measure represents a commit
     * or day separator that is in the middle of at least one conflict.
     * <code>false</code> otherwise.
     * @param isDaySeparator <code>true</code> if this measure represents a
     * day separator. <code>false</code> otherwise.
     */
    public Measure(String author, boolean inConflict, boolean isDaySeparator) {
        this.author = author;
        this.inConflict = inConflict;
        this.isDaySeparator = isDaySeparator;
    }

    /**
     * Gets the author of the commit to be sonified. If this measure represents
     * a day separator, this parameter should be ignored and might be
     * <code>null</code>.
     * @return The author of the commit to be sonified. If this measure
     * represents a day separator, this parameter should be ignored and might be
     * <code>null</code>.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Indicates whether this measure represents a commit or day separator that
     * is in the middle of at least one conflict.
     * @return <code>true</code> if this measure represents a commit or day
     * separator that is in the middle of at least one conflict.
     * <code>false</code> otherwise.
     */
    public boolean isInConflict() {
        return inConflict;
    }

    /**
     * Indicates whether this measure represents a day separator.
     * @return <code>true</code> if this measure represents a day separator.
     * <code>false</code> otherwise.
     */
    public boolean isDaySeparator() {
        return isDaySeparator;
    }

    @Override
    public String toString() {
        return "Measure{" + "author=" + author + ", inConflict=" + inConflict
                + ", isDaySeparator=" + isDaySeparator + '}';
    }
}