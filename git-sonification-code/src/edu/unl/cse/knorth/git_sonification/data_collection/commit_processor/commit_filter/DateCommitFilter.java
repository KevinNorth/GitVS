package edu.unl.cse.knorth.git_sonification.data_collection.commit_processor.commit_filter;

import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.PartialCommit;
import org.joda.time.DateTime;

/**
 * Filters commits based on when they were made, selecting the commits that fall
 * in-between two specified dates.
 * @author knorth
 */
public class DateCommitFilter extends CommitFilter {
    private final DateTime since;
    private final DateTime until;

    /**
     * Creates a DateCommitFilter that selects commits made in-between
     * <code>until</code> and <code>since</code>, inclusively.
     * @param since The earliest timestamp that a commit can have been made on
     * and still be selected.
     * @param until The latest timestamp that a commit can have been made on and
     * still be selected.
     */
    public DateCommitFilter(DateTime since, DateTime until) {
        this.since = since;
        this.until = until;
    }
    
    /**
     * Determines whether the given commit was made in-between the two specified
     * dates passed to this <code>DateCommitFilter</code>'s constructor.
     * @param commit The commit to check
     * @return <code>true</code> if the date the commit was made falls in-
     * between <code>since</code> and <code>until</code> (inclusively).
     * <code>false</code> otherwise.
     */
    @Override
    protected boolean shouldSelectCommit(PartialCommit commit) {
        DateTime commitDate = commit.getDatetime();
        
        if((commitDate.isAfter(since)) && (commitDate.isBefore(until))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return The earliest <code>Date</code> that a commit can have been made
     * on and still be selected by this <code>DateCommitFilter</code>.
     */
    public DateTime getSince() {
        return since;
    }

    /**
     * @return The latest <code>Date</code> that a commit can have been made
     * on and still be selected by this <code>DateCommitFilter</code>.
     */
    public DateTime getUntil() {
        return until;
    }
}