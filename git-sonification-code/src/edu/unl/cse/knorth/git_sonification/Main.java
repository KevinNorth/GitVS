package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.git_processor.git_caller.GitCaller;
import edu.unl.cse.knorth.git_sonification.git_processor.git_caller.PartialCommit;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2010, 0, 1);
        Date since = cal.getTime();
        cal.set(2011, 0, 1);
        Date until = cal.getTime();
                
        List<PartialCommit> commits;
        try(GitCaller gitCaller =
                new GitCaller("../../voldemort/.git")) {
            commits = gitCaller.getPartialCommits(since, until);
        }
        
        for(PartialCommit commit : commits) {
            System.out.println(commit);
        }
    }
}
