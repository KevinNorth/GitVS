package edu.unl.cse.knorth.git_sonification.git_processor.git_caller;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
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
     * @throws IOException If there was a problem reading the master branch of
     * the repository.
     */
    public List<PartialCommit> getPartialCommits(Date sinceDate, Date untilDate)
            throws IllegalStateException, IOException
    {
        if(closed) {
            throw new IllegalStateException("Can't call getCommitHashes() on a "
                    + "GitCaller that has been closed.");
        }
                
        RevWalk walk = new RevWalk(repo);
        
        try {
        Ref head = repo.getRef("refs/heads/master");
        RevCommit headCommit = walk.parseCommit(head.getObjectId());
        walk.markStart(headCommit);
        } catch(MissingObjectException err) {
            throw new IOException("Couldn't find the HEAD of the master branch",
                    err);
        } catch(IncorrectObjectTypeException err) {
            throw new IOException("Problem getting the HEAD of the master "
                    + "branch", err);
        } catch(IOException err) {
            throw new IOException("Problem getting the HEAD of the master "
                    + "branch", err);
        }
                        
        List<PartialCommit> partialCommits  = new LinkedList<>();
        
        for(RevCommit commit : walk) {
            if(isCommitInDateRange(commit, sinceDate, untilDate)) {
                PartialCommit partialCommit = getCommitData(commit);
                partialCommits.add(partialCommit);
            }
        }

        walk.dispose();
        
        return partialCommits;
    }
    
    /**
     * Determines whether the given commit was made in-between the two specified
     * dates.
     * @param commit The commit to check
     * @param since The earliest date the commit can have been made and still be
     * accepted
     * @param until The latest date the commit can have been made and still be
     * accepted
     * @return <code>true</code> if the date the commit was made on falls in-
     * between <code>since</code> (inclusively) and <code>until</code>
     * (exclusively). <code>false</code> otherwise.
     */
    private boolean isCommitInDateRange(RevCommit commit, Date since,
        Date until) {
        Date commitDate = new Date((long) commit.getCommitTime() * 1000L);
        
        if((commitDate.after(since)) && (commitDate.before(until))) {
            return true;
        } else if(commitDate.equals(since)) {
            return true;
        } else {
            return false;
        }
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
    
    /**
     * Indicates whether this GitCaller has been <code>close()</code>ed, as per
     * the <code>Closeable</code> and <code>AutoClosable</code> interfaces.
     * <p/>
     * If this function returns <code>false</code>, calling
     * <code>getPartialCommits()</code> will throw an exception. Otherwise, it
     * is safe to call <code>getPartialCommits()</code>.
     * @return <code>true</code> if <code>close()</code> has previously bee
     * called on this <code>GitCaller</code>. <code>false</code> otherwise.
     */
    public boolean isClosed() {
        return closed;
    }
}