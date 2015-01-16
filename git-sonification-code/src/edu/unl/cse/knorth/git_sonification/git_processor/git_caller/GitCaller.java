package edu.unl.cse.knorth.git_sonification.git_processor.git_caller;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Interacts with the jGit library to git information from the Voldemort git
 * repository.
 */
public class GitCaller implements AutoCloseable, Closeable {
    private final Repository repo;
    private boolean closed;
    
    /**
     * Constructs a GitCaller that interacts with a particular git repository.
     * <p/>
     * Be aware that this constructor involves working with the file system and
     * is therefore expensive to run. It is highly recommended that you only
     * construct one GitCaller per repository the program interacts with every
     * time you run a program relying on this class. In addition, you should
     * call <code>close()</code> on a GitCaller when you are done using it.
     * @param repositoryPath The path to the repository that the GitCaller will
     * interact with. This path should lead to a directory that contains a .git
     * directory, but not to the .git directory itself.
     * @throws IOException If there was a problem opening up the repository.
     */
    public GitCaller(String repositoryPath) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        builder.setGitDir(new File(repositoryPath));
        repo = builder.build();
        
        if(!repo.getObjectDatabase().exists()) {
            throw new IOException(repositoryPath + " is not a path to a git "
                    + "repository.");
        }
        
        closed = false;
    }
    
    /**
     * Gets a list of commits that will be processed by the rest of the Git
     * Parser component.
     * @param sinceDate This method will get information on commits made on or
     * after this date. (This corresponds to the date that would be used in the
     * <code>--since</code> flag in the <code>git log</code> command.)
     * @param untilDate This method will get information on commits made before
     * (but not on) this date. (This corresponds to the date that would be used
     * in the <code>--until</code> flag in the <code>git log</code> command.)
     * @return A list of PartialCommits for all of the commits made on or after
     * <code>sinceDate</code> but before <code>untilDate</code>.
     * @throws IllegalStateException If you try to call this method after
     * previously calling <code>close()</code> on this GitCaller.
     */
    public List<PartialCommit> getPartialCommits(Date sinceDate, Date untilDate)
    {
        if(closed) {
            throw new IllegalStateException("Can't call getCommitHashes() on a "
                    + "GitCaller that has been closed.");
        }
        
        RevFilter filter = CommitTimeRevFilter.between(sinceDate, untilDate);
        
        RevWalk walk = new RevWalk(repo);
        walk.setRevFilter(filter);
        
        List<PartialCommit> partialCommits  = new LinkedList<>();
        
        for(RevCommit commit : walk) {
            PartialCommit partialCommit = getCommitData(commit);
            partialCommits.add(partialCommit);
        }

        return partialCommits;
    }
    
    /**
     * Gets the author, timestamp, and list of parents for the commit associated
     * with the specified hash.
     * @param RevCommit The commit that we're interested in.
     * @return A <code>PartialCommit</code> containing the commit's hash,
     * author, timestamp, and list of parents' hashes.
     */
    private PartialCommit getCommitData(RevCommit commit) {
        String hash = commit.getName();
        String author = commit.getAuthorIdent().getName();

        int secondsSinceEpoch = commit.getCommitTime();
        Date date = new Date(((long) secondsSinceEpoch) * 1000L);
        
        List<String> parentHashes = new ArrayList<>(commit.getParentCount());
        for(RevCommit parent : commit.getParents()) {
            parentHashes.add(parent.getName());
        }
        
        return new PartialCommit(hash, author, date, parentHashes);
    }

    /**
     * Allows the resources the GitCaller is using for its reference to a
     * repository to be closed. Once you've called this method, any subsequent
     * calls to <code>getPartialCommits()</code> will throw
     * <code>IllegalStateException</code>s.
     * <p/>
     * You can safely call this method multiple times. It will only have a side
     * effect the first time.
     */
    @Override
    public void close() {
        if(!closed) {
            repo.close();
            closed = true;
        }
    }
}