package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.data_collection.commit_processor.CommitProcessor;
import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_collection.components.ComponentDataParser;
import edu.unl.cse.knorth.git_sonification.data_collection.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.data_collection.conflict_data.ConflictDataParser;
import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.GitCaller;
import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.data_processing.sonification.SonificationProcessor;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.AnnotatedCommit;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.CommitAnnotator;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.Measure;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.SonificationData;
import java.io.IOException;
import java.util.List;
import org.joda.time.DateTime;

public class Main {
    public static void main(String[] args) throws IOException {
//        GregorianCalendar cal = new GregorianCalendar();
//        cal.set(2009, 11, 23);
        DateTime since = new DateTime(2009, 11, 4, 0, 0);
//        cal.set(2010, 0, 6);
        DateTime until = new DateTime(2009, 11, 9, 0, 0);
                
        List<PartialCommit> partialCommits;
        try(GitCaller gitCaller = new GitCaller("../../voldemort/.git")) {
            partialCommits = gitCaller.getPartialCommits();
        }
        
        List<Conflict> conflicts = new ConflictDataParser()
                .parseConflictData("data/conflict_data.dat");
        
        List<Component> components = new ComponentDataParser()
                .parseComponents("data/components.txt");
        
        List<Commit> commits = new CommitProcessor()
                .processCommits(partialCommits, conflicts, components, since,
                        until);
        
        for(Commit commit : commits) {
            System.out.println(commit);
        }
        
        System.out.println("\n----------------\n");
        
        SonificationProcessor sonificationProcessor = new SonificationProcessor();
        SonificationData sonificationData = sonificationProcessor.processSonificationData(commits);
        
        for(Measure measure : sonificationData.getMeasures()) {
            System.out.println(measure);
        }
        
        List<AnnotatedCommit> annotatedCommits =
                new CommitAnnotator().annotateCommits(commits);
        
        for(AnnotatedCommit annotatedCommit : annotatedCommits) {
            System.out.println(annotatedCommit);
        }
    }
}
