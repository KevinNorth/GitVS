package edu.unl.cse.knorth.git_sonification.git_processor.git_caller;

import java.util.Calendar;
import java.util.List;

/**
 * Interacts with the jGit library to git information from the Voldemort git
 * repository.
 */
public class GitCaller {
    /**
     * Gets a list of hashes of commits that will be processed by the rest of
     * the Git Parser component.
     * @param sinceDate This method will get information on commits made on or
     * after this date. (This corresponds to the date that would be used in the
     * <code>--since</code> flag in the <code>git log</code> command.)
     * @param untilDate This method will get information on commits made before
     * (but not on) this date. (This corresponds to the date that would be used
     * in the <code>--until</code> flag in the <code>git log</code> command.)
     * @return A list of hashes for all of the commits made on or after
     * <code>sinceDate</code> but before <code>untilDate</code>.
     */
    public List<String> getCommitHashes(Calendar sinceDate, Calendar untilDate) {
       throw new UnsupportedOperationException("Not implemented yet"); 
    }
    
    /**
     * Gets the author, timestamp, and list of parents for the commit associated
     * with the specified hash.
     * @param commitHash The hash for the commit that we're interested in.
     * @return A <code>LogCommitData</code> containing the commit's author,
     * timestamp, and list of parents' hashes.
     */
    public LogCommitData getLogCommitData(String commitHash) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}