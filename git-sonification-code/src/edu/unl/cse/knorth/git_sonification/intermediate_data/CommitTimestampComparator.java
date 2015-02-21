package edu.unl.cse.knorth.git_sonification.intermediate_data;

import java.util.Comparator;
import org.joda.time.DateTime;

/**
 * Compares <code>Commit</code>s by their timestamps. Earlier commits are
 * treated as having lower values than later timestamps.
 * @see Commit
 */
public class CommitTimestampComparator implements Comparator<Commit> {
    @Override
    public int compare(Commit c1, Commit c2) {
        DateTime c1Timestamp = c1.getTimestamp();
        DateTime c2Timestamp = c2.getTimestamp();
        
        if(c1Timestamp.isBefore(c2Timestamp)) {
            return -1;
        } else if(c1Timestamp.isAfter(c2Timestamp)) {
            return 1;
        } else {
            return 0;
        }
    }
}