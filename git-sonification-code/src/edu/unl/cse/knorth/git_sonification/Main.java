package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.commit_processor.CommitProcessor;
import edu.unl.cse.knorth.git_sonification.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.conflict_data.ConflictDataParser;
import edu.unl.cse.knorth.git_sonification.git_caller.GitCaller;
import edu.unl.cse.knorth.git_sonification.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.intermediate_data.Commit;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2009, 11, 23);
        Date since = cal.getTime();
        cal.set(2010, 0, 6);
        Date until = cal.getTime();
                
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
    }
}
