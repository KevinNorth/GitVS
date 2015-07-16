package edu.unl.cse.knorth.git_sonification.display_preparation.sonification;

import edu.unl.cse.knorth.git_sonification.intermediate_data.Commit;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Takes a list of Commits and uses it to produce a list of Measures.
 */
public abstract class MeasureProducer {
    public abstract ConcurrentLinkedQueue<Measure> produceMeasures(List<Commit> commits);
}
