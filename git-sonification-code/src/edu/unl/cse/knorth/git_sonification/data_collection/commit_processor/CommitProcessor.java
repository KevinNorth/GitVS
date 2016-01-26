package edu.unl.cse.knorth.git_sonification.data_collection.commit_processor;

import edu.unl.cse.knorth.git_sonification.data_collection.commit_processor.commit_filter.CommitFilter;
import edu.unl.cse.knorth.git_sonification.data_collection.commit_processor.commit_filter.DateCommitFilter;
import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_collection.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraph;
import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.CommitTimestampComparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import org.joda.time.DateTime;
import java.util.List;

public class CommitProcessor {
    /**
     * Produces a list of Commits, filtered so that only the commits made
     * between two dates, then sorted in ascending order of their
     * timestamps, that can be sonified.
     * @param partialCommits A list of every commit from the repository to be
     * sonified.
     * @param conflicts A list of <code>Conflict</code>s which will be used to
     * add conflict metadata to the <code>Commit</code>s this method returns.
     * @param components A list of all components that appear in the commits.
     * @param since The earliest a commit can have been made and still appear in
     * the list of fully processed <code>Commit</code>s that this method
     * returns.
     * @param until The latest a commit can have been made and still appear in
     * the list of fully processed <code>Commit</code>s that this method
     * returns.
     * @param gitGraph A <code>GitGraph</code> object representing the data from
     * <code>git log --graph</code>.
     * @return A list of fully processed <code>Commit</code>s that are ready to
     * be sonified, filtered according to <code>since</code> and
     * <code>until</code> and sorted in ascending order of commit timestamp.
     */
    public List<Commit> processCommits(List<PartialCommit> partialCommits,
            List<Conflict> conflicts, List<Component> components,
            DateTime since, DateTime until, GitGraph gitGraph) {
        return processCommits(partialCommits, conflicts, components,
                new DateCommitFilter(since, until), gitGraph);
    }
    
    /**
     * Produces a list of Commits, sorted in ascending order of their
     * timestamps, that can be sonified.
     * @param partialCommits A list of every commit from the repository to be
     * sonified.
     * @param conflicts A list of <code>Conflict</code>s which will be used to
     * add conflict metadata to the <code>Commit</code>s this method returns.
     * @param components A list of all components that appear in the commits.
     * @param commitFilter A <code>CommitFilter</code> that will determine which
     * particular commits will be sonified.
     * @param gitGraph A <code>GitGraph</code> object representing the data from
     * <code>git log --graph</code>.
     * @return A list of fully processed <code>Commit</code>s that are ready to
     * be sonified, filtered according to <code>commitFilter</code> and sorted
     * in ascending order of commit timestamp.
     */
    public List<Commit> processCommits(List<PartialCommit> partialCommits,
            List<Conflict> conflicts, List<Component> components,
            CommitFilter commitFilter, GitGraph gitGraph) {
        Comparator<Commit> commitComparator = new Comparator<Commit>() {
            @Override
            public int compare(Commit c1, Commit c2) {
                return gitGraph.getHashComparator().compare(c1.getHash(),
                        c2.getHash());
            }
        };
        
        return processCommits(partialCommits, conflicts, components,
                commitFilter, commitComparator);
    }
    
    /**
     * Produces a list of Commits that can be sonified.
     * @param partialCommits A list of every commit from the repository to be
     * sonified.
     * @param conflicts A list of <code>Conflict</code>s which will be used to
     * add conflict metadata to the <code>Commit</code>s this method returns.
     * @param components A list of all components that appear in the commits.
     * @param commitFilter A <code>CommitFilter</code> that will determine which
     * particular commits will be sonified.
     * @param commitComparator A <code>Comparator</code> that will be used to
     * sort the <code>Commit</code>s before they are sonified. Remember, the
     * <code>Sonifier</code> sonifies the commits in the order it receives them;
     * it does not order commits itself.
     * @return A list of fully processed <code>Commit</code>s that are ready to
     * be sonified, filtered according to <code>commitFilter</code> and sorted
     * according to <code>commitComparator</code>.
     */
    public List<Commit> processCommits(List<PartialCommit> partialCommits,
            List<Conflict> conflicts, List<Component> components,
            CommitFilter commitFilter, Comparator<Commit> commitComparator) {
        List<PartialCommit> filteredCommits =
                commitFilter.filterCommits(partialCommits);

        // We'll be sorting this list later, so let's keep it an ArrayList
        // so that it's performant.
        List<Commit> processedCommits = new ArrayList<>(filteredCommits.size());
        
        for(PartialCommit filteredCommit : filteredCommits) {
            processedCommits.add(
                    processPartialCommit(filteredCommit, conflicts,
                            components));
        }
        
        processedCommits.sort(commitComparator);
        return processedCommits;
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
     * <li> If the commit introduces a conflict, that information will be
     * included in the commit's <code>introducesCommit</code> attribute.</li>
     * <li>If the commit resolves a conflict, the hash of the conflict it
     * resolves will be included in the commit's
     * <code>resolvedConflictHash</code> attribute.</li>
     * </ul>
     */
    private Commit processPartialCommit(PartialCommit partialCommit,
        List<Conflict> conflicts, List<Component> components) {
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
        commit.addComponentsModified(
                matchFilesModifiedToComponents(partialCommit.getFilesModified(),
                        components));
        commit.setAuthor(partialCommit.getAuthor());
        commit.setHash(hash);
        commit.setIntroducesConflict(introducesConflict);
        commit.setResolvedConflictHash(resolvedCommit);
        commit.setTimestamp(partialCommit.getDatetime());
                
        return commit;
    }
    
    /**
     * Gets a list of all of the Components that include at least one of a list
     * of files.
     * @param filesModified The list of files to check.
     * @param components A List of Components where each Component includes at
     * least one of the files in <code>filesModified</code>.
     * @return 
     */
    private List<Component> matchFilesModifiedToComponents(
            List<String> filesModified, List<Component> components) {
        List<Component> matchedComponents = new LinkedList<>();
        
        for(Component component : components) {
            if(component.includesAnyFile(filesModified)) {
                matchedComponents.add(component);
            }
        }
        
        return matchedComponents;
    }
}