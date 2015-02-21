package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.commit_processor.CommitProcessor;
import edu.unl.cse.knorth.git_sonification.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.conflict_data.ConflictDataParser;
import edu.unl.cse.knorth.git_sonification.git_caller.GitCaller;
import edu.unl.cse.knorth.git_sonification.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.sonifier.Measure;
import edu.unl.cse.knorth.git_sonification.sonifier.MeasureProducer;
import edu.unl.cse.knorth.git_sonification.sonifier.audio.per_commit.PerCommitMeasureProducer;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import org.joda.time.DateTime;

public class Main {
    public static void main(String[] args) throws IOException {
//        GregorianCalendar cal = new GregorianCalendar();
//        cal.set(2009, 11, 23);
        DateTime since = new DateTime(2009, 12, 23, 0, 0);
//        cal.set(2010, 0, 6);
        DateTime until = new DateTime(2010, 1, 6, 0, 0);
                
        List<PartialCommit> partialCommits;
        try(GitCaller gitCaller = new GitCaller("../../voldemort/.git")) {
            partialCommits = gitCaller.getPartialCommits();
        }
        
        List<Conflict> conflicts = new ConflictDataParser()
                .parseConflictData("data/conflict_data.dat");
        
        List<Commit> commits = new CommitProcessor()
                .processCommits(partialCommits, conflicts, since, until);
        
        for(Commit commit : commits) {
            System.out.println(commit);
        }
        
        System.out.println("\n----------------\n");
        
        MeasureProducer measureProducer = new PerCommitMeasureProducer();
        Queue<Measure> measures = measureProducer.produceMeasures(commits);
        
        for(Measure measure : measures) {
            System.out.println(measure);
        }
    }
}
