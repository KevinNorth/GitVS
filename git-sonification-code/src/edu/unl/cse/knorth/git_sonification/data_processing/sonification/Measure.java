package edu.unl.cse.knorth.git_sonification.data_processing.sonification;

/**
 * A POCO that represents a measure of music that will be sonified.
 */
public final class Measure {
    private final String author;
    private final int numConflicts;
    private final boolean isDaySeparator;

    /**
     * @param author The author of the commit to be sonified. If this measure
     * represents a day separator, this parameter will be ignored and can be
     * <code>null</code>.
     * @param numConflicts The number of conflicts that are present in the
     * codebase when this measure is sonified. Pass <code>0</code> if there are
     * no conflicts.
     * <code>false</code> otherwise.
     * @param isDaySeparator <code>true</code> if this measure represents a
     * day separator. <code>false</code> otherwise.
     */
    public Measure(String author, int numConflicts, boolean isDaySeparator) {
        this.author = author;
        this.numConflicts = numConflicts;
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
        return numConflicts != 0;
    }

    /**
     * The number of conflicts that are present in the codebase when this
     * measure is sonified.
     * @return The number of conflicts that are present in the codebase when
     * this measure is sonified. 0 if there are no conflicts.
     */
    public int getNumConflicts() {
        return numConflicts;
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
        return "Measure{" + "author=" + author + ", numConflicts="
                + numConflicts + ", isDaySeparator=" + isDaySeparator + '}';
    }
}