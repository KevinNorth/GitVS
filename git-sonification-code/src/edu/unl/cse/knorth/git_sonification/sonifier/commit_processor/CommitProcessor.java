package edu.unl.cse.knorth.git_sonification.sonifier.commit_processor;

import edu.unl.cse.knorth.git_sonification.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.sonifier.Measure;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Takes a list of Commits and uses it to produce a list of Measures.
 */
public class CommitProcessor {
    public ConcurrentLinkedQueue<Measure> processCommits(List<Commit> commits) {
        ConcurrentLinkedQueue<Measure> measures = new ConcurrentLinkedQueue<>();
        throw new UnsupportedOperationException("Not yet implemented");
    }
}