package edu.unl.cse.knorth.git_sonification.data_processing.sonification;

import edu.unl.cse.knorth.git_sonification.display.model.sonification.SonificationData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Takes a list of Commits and uses it to produce a list of Measures that are
 * used in 
 */
public class SonificationProcessor {
    public SonificationData processSonificationData(
            Map<String, Integer> authorCommitCounts) {
        return new SonificationData(
                getAuthorsInOrderOfCommitCounts(authorCommitCounts));
    }
    
    private List<String> getAuthorsInOrderOfCommitCounts(
            final Map<String, Integer> authorCommitCounts) {
        List<String> authorsInOrderOfCommitCounts =
                new ArrayList<>(authorCommitCounts.keySet());

        Collections.sort(authorsInOrderOfCommitCounts,
                new Comparator<String>() {
            @Override
            public int compare(String author1, String author2) {
                return -(Integer.compare(authorCommitCounts.get(author1),
                            authorCommitCounts.get(author2)));
            }
        });
        
        return authorsInOrderOfCommitCounts;
    }
}