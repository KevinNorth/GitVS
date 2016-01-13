package edu.unl.cse.knorth.git_sonification.data_collection.git_caller;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.joda.time.DateTime;

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
     * @return A list of PartialCommits for all of the commits made in this
     * GitCaller's repository, as well as a map indicating how many commits were
     * made by each developer in the repository.
     * @throws IllegalStateException If you try to call this method after
     * previously calling <code>close()</code> on this GitCaller.
     * @throws IOException If there was a problem reading the master branch of
     * the repository.
     */
    public PartialCommitsAndAuthorCommitCounts getPartialCommits()
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
        Map<String, Integer> authorCommitCounts = new HashMap<>();
        
        try {
            for(RevCommit commit : walk) {
                PartialCommit partialCommit = getCommitData(commit);
                partialCommits.add(partialCommit);
                
                String author = partialCommit.getAuthor();
                if(authorCommitCounts.containsKey(author)) {
                    int oldValue = authorCommitCounts.get(author);
                    authorCommitCounts.put(author, oldValue + 1);
                } else {
                    authorCommitCounts.put(author, 1);
                }
            }
        }
        catch(IOException err) {
            throw new IOException("Problem processing commits", err);
        }

        walk.dispose();
        
        return new PartialCommitsAndAuthorCommitCounts(partialCommits,
                authorCommitCounts);
    }
        
    /**
     * Gets the author, timestamp, and list of parents for the commit associated
     * with the specified hash.
     * @param RevCommit The commit that we're interested in.
     * @return A <code>PartialCommit</code> containing the commit's hash,
     * author, timestamp, and list of parents' hashes.
     */
    private PartialCommit getCommitData(RevCommit commit) throws IOException {
        String hash = commit.getName();
        String author = commit.getAuthorIdent().getName();

        DateTime date = new DateTime(commit.getAuthorIdent().getWhen());
        
        List<String> parentHashes = new ArrayList<>(commit.getParentCount());
        for(RevCommit parent : commit.getParents()) {
            parentHashes.add(parent.getName());
        }
        
        List<String> filesChanged = getFilesInCommit(commit);
        
        return new PartialCommit(hash, author, date, parentHashes,
                filesChanged);
    }
    
	/**
	 * Returns the list of files changed in a specified commit. If the
	 * repository does not exist or is empty, an empty list is returned.
	 *<p/>
         * This function is adapted from the gitblit project (specifically,
         * this version of the JGitUtils.java file:
         * https://github.com/gitblit/gitblit/blob/f76fee63ed9cb3a30d3c0c092d860b1cb93a481b/src/main/java/com/gitblit/utils/JGitUtils.java
         * ). gitblit is licensed under Apache Software Foundation license,
         * version 2.0 (https://www.apache.org/licenses/LICENSE-2.0).
         * 
	 * @param commit
	 *            if null, HEAD is assumed.
	 * @return list of files changed in a commit
	 */
	private List<String> getFilesInCommit(RevCommit commit) throws IOException {
		List<String> list = new ArrayList<>();
		RevWalk rw = new RevWalk(repo);
		try {
                    if (commit.getParentCount() == 0) {
                        // For the first commit, any files present after the
                        // first commit must have been added in that commit.
                        // So get the list of present files and return that.
                        TreeWalk tw = null;
                        try {
                            tw = new TreeWalk(repo);
                            tw.reset();
                            tw.setRecursive(true);
                            tw.addTree(commit.getTree());
                            while (tw.next()) {
                                    list.add(tw.getPathString());
                            }
                        } finally {
                            if(tw != null) {
                                tw.release();
                            }
                        }
                    } else if(commit.getParentCount() == 1) {
                        RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
                        DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
                        df.setRepository(repo);
                        df.setDiffComparator(RawTextComparator.DEFAULT);
                        // Moving a file can affect two different components:
                        // the component the file was moved from, and the
                        // component the file moved to. To find both components,
                        // treat file movement as a seperate addition and
                        // deletion.
                        df.setDetectRenames(false);
                        List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
                        for (DiffEntry diff : diffs) {
                            list.add(diff.getNewPath());
                        }
                    } else {
                        // For merge commits, we iterate over all of the merge's
                        // parents and include any files that changed between
                        // at least one of them.
                        Set<String> set = new HashSet<>(); // To ignore dups
                        DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
                        df.setRepository(repo);
                        df.setDiffComparator(RawTextComparator.DEFAULT);
                        df.setDetectRenames(false);

                        for(RevCommit parent : commit.getParents()) {
                            parent = rw.parseCommit(parent.getId());
                            List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
                            for (DiffEntry diff : diffs) {
                                set.add(diff.getNewPath());
                            }
                        }
                        
                        list.addAll(set);
                    }
		} finally {
			rw.dispose();
		}
		return list;
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