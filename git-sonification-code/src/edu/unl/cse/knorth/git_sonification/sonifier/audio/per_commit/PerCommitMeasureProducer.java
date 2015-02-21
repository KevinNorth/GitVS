package edu.unl.cse.knorth.git_sonification.sonifier.audio.per_commit;

import edu.unl.cse.knorth.git_sonification.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.sonifier.Measure;
import edu.unl.cse.knorth.git_sonification.sonifier.MeasureProducer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * Takes a list of Commits and uses it to produce a list of Measures that are
 * used in 
 */
public class PerCommitMeasureProducer extends MeasureProducer {
    public ConcurrentLinkedQueue<Measure>
        produceMeasures(List<Commit> commits) {
        ConcurrentLinkedQueue<Measure> measures = new ConcurrentLinkedQueue<>();
        List<String> introducedConflictHashes = new LinkedList<>();
        
        Commit previousCommit = null;
        Measure previousMeasure = null;
        
        for(Commit commit : commits) {
            String author = commit.getAuthor();

            // Determine whether the measure is a conflict measure
            boolean introducesConflict = commit.isIntroducesConflict();
            boolean isInConflict;
            if(introducesConflict || !(introducedConflictHashes.isEmpty())) {
                isInConflict = true;
            } else {
                isInConflict = false;
            }

            // Add day separators
            if(previousCommit != null && previousMeasure != null) {
                int daysSincePreviousCommit =
                    calculateDaysSinceLastCommit(commit, previousCommit);
                
                if(daysSincePreviousCommit > 0) {
                    addDaySeparators(measures, daysSincePreviousCommit,
                            previousMeasure.isInConflict());
                }
            }
            
            // Update list of conflicts
            if(introducesConflict) {
                introducedConflictHashes.add(commit.getHash());  
            }
            if(commit.getResolvedConflictHash() != null) {
                introducedConflictHashes.remove(
                        commit.getResolvedConflictHash());
            }
            
            Measure measure = new Measure(author, isInConflict, false);
            measures.add(measure);
            
            previousCommit = commit;
            previousMeasure = measure;
        }
        
        return measures;
    }
    
    /**
     * Adds several day separator measures to the end of the measures queue.
     * @param measures The measures queue to add the day separators to.
     * @param numberOfSeparators The number of day separator measures to add.
     * @param conflict <code>true</code> if the added measures should count as
     * occurring in the middle of a conflict. <code>false</code> otherwise.
     */
    private void addDaySeparators(ConcurrentLinkedQueue<Measure> measures,
            int numberOfSeparators, boolean conflict) {
            
        for(int i = 0; i < numberOfSeparators; i++) {
            measures.add(new Measure(null, conflict, true));
        }            
    }
    
    /**
     * Calculates the number of days between two commits. This doesn't count two
     * days as being on different days if they are at least 24 hours apart, but
     * rather based on whether they are different calendar days. So a commit
     * made on December 2nd at 12:01 am would count as being made one day after
     * a commit made on December 1st at 11:59 pm.
     * @param commit The later commit.
     * @param previousCommit The earlier commit.
     * @return The number of days in-between the two commits.
     */
    private int calculateDaysSinceLastCommit(Commit commit, Commit previousCommit) {
        LocalDate currentDate = commit.getTimestamp().toLocalDate();
        LocalDate previousDate = previousCommit.getTimestamp().toLocalDate();
        return Days.daysBetween(previousDate, currentDate).getDays();
    }
}