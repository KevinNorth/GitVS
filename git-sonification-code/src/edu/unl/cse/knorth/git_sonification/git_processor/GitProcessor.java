package edu.unl.cse.knorth.git_sonification.git_processor;

import edu.unl.cse.knorth.git_sonification.git_processor.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.git_processor.conflict_data.ConflictDataParser;
import edu.unl.cse.knorth.git_sonification.git_processor.git_caller.GitCaller;
import edu.unl.cse.knorth.git_sonification.git_processor.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.intermediate_data.CommitTimestampComparator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Processes git data and conflict data in order to prepare the list of commits
 * that will be sonified.
 */
public class GitProcessor {
    /**
     * Processes git data and conflict data in order to prepare the list of
     * commits that will be sonified.
     * @param repositoryPath The file path to the repository that has the
     * commits to be sonified.
     * @param conflictDataPath The file path to the conflict data file. See the
     * <code>ConflictDataParser</code> class's documentation for the format this
     * file must be in.
     * @param since Only commits made on or after this date will be returned.
     * @param until Only commits made before this date will be returned.
     * @return A <code>List</code> of <code>Commit</code>s that the rest of the
     * program can sonify. These commits will be in the order that they should
     * be sonified.
     * @throws java.io.IOException If there is a problem getting information
     * from either the target repository or the conflict data file.
     */
    public List<Commit> parseGitData(String repositoryPath,
            String conflictDataPath, Date since, Date until) throws IOException {
        List<Conflict> conflicts = new ConflictDataParser()
                .parseConflictData(conflictDataPath);
        List<PartialCommit> partialCommits;
        
        try(GitCaller gitCaller = new GitCaller(repositoryPath)) {
            partialCommits = gitCaller.getPartialCommits(since, until);
        }
        
        // We'll be sorting this list later, so let's keep it an ArrayList
        // so that it's performant.
        List<Commit> commits = new ArrayList<>();
        
        for(PartialCommit partialCommit : partialCommits) {
            commits.add(processPartialCommit(partialCommit, conflicts));
        }
        
        commits.sort(new CommitTimestampComparator());
        return commits;
    }
    
    /**
     * Converts a PartialCommit into a Commit by getting the relevant
     * information from the conflict metadata.
     * @param partialCommit The <code>PartialCommit</code> to process.
     * @param conflicts The list of <code>Conflict</code>s that contain metadata
     * to use for the generated commit.
     * @return A <code>Commit</code> with all of the information from
     * <code>partialCommit</code> in addition to information from the
     * <code>conflicts</code> list:
     * <ul>
     * <li> If the commit introduces a conflict, that information will 
     */
    private Commit processPartialCommit(PartialCommit partialCommit,
        List<Conflict> conflicts) {
                String hash = partialCommit.getHash();
        String resolvedCommit = null;
        boolean introducesConflict = false;

        for(Conflict conflict : conflicts) {
            if(conflict.getCommitIntroducingConflict().equals(hash)) {
                introducesConflict = true;
            } else if(conflict.getCommitResolvingConflict().equals(hash)) {
                resolvedCommit = conflict.getCommitIntroducingConflict();
            }
        }

        Commit commit = new Commit();
        commit.addParentHashes(partialCommit.getParentHashes());
        commit.setAuthor(partialCommit.getAuthor());
        commit.setHash(hash);
        commit.setIntroducesConflict(introducesConflict);
        commit.setResolvedConflictHash(resolvedCommit);
        commit.setTimestamp(partialCommit.getDatetime());
        return commit;
    }
}