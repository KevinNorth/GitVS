package edu.unl.cse.knorth.git_sonification.data_collection.commit_processor;

import edu.unl.cse.knorth.git_sonification.data_collection.commit_processor.commit_filter.BetweenHashesCommitFilter;
import edu.unl.cse.knorth.git_sonification.data_collection.commit_processor.commit_filter.CommitFilter;
import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_collection.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraph;
import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
     * @param firstHash The hash of the first commit in the
     * <code>gitGraph</code> that will also appear in the list of fully
     * processed <code>Commit</code>s that this method returns.
     * @param lastHash The hash of the first commit in the
     * <code>gitGraph</code> that will also appear in the list of fully
     * processed <code>Commit</code>s that this method returns.
     * @param gitGraph A <code>GitGraph</code> object representing the data from
     * <code>git log --graph</code>.
     * @return A list of fully processed <code>Commit</code>s that are ready to
     * be sonified, filtered according to <code>since</code> and
     * <code>until</code> and sorted in ascending order of commit timestamp.
     */
    public List<Commit> processCommits(List<PartialCommit> partialCommits,
            List<Conflict> conflicts, List<Component> components,
            String firstHash, String lastHash, GitGraph gitGraph) {
        return processCommits(partialCommits, conflicts, components,
                new BetweenHashesCommitFilter(firstHash, lastHash, gitGraph),
                gitGraph);
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
        return processCommits(partialCommits, conflicts, components,
                commitFilter, gitGraph.getCommitComparator(), gitGraph);
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
     * @param gitGraph A <code>GitGraph</code> object representing the data from
     * <code>git log --graph</code>.
     * @return A list of fully processed <code>Commit</code>s that are ready to
     * be sonified, filtered according to <code>commitFilter</code> and sorted
     * according to <code>commitComparator</code>.
     */
    public List<Commit> processCommits(List<PartialCommit> partialCommits,
            List<Conflict> conflicts, List<Component> components,
            CommitFilter commitFilter, Comparator<Commit> commitComparator,
            GitGraph gitGraph) {
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
        
        List<Commit> commitsCaughtInGraph =
                findCommitsCaughtInGraph(partialCommits, processedCommits,
                        conflicts, components, gitGraph);
        
        processedCommits.addAll(commitsCaughtInGraph);
        
        Collections.sort(processedCommits, commitComparator);
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

    /**
     * Since our visualization shows the branching and merging graph of a Git
     * history, it needs to include all of the commits within those branching
     * and merging patterns. However, the filter we used may have omitted some
     * of the commits that are in the graph. Without these commits, there is no
     * way to correctly represent the graph, because some commits will be
     * missing their parents.
     * <p/>
     * This function rectifies the situation by adding all of the commits that
     * are in the graph, but were filtered out previously. These commits can
     * then be added to the visualization, or existing commits can be edited to
     * cover up the holes that the omitted commits included.
     * <p/>
     * The main reason we need this function is because the default commit
     * filter gets the commits in a date range, which means we implicitly assume
     * that all commits are in chronological order. This is not true when we
     * look at real-world data. For some reason, many repositories will have
     * commits that are timestamped as earlier than their parents, meaning that
     * we risk filtering some commits' parents out of the visualization.
     * @param partialCommits A list of all of the commits in the Git history.
     * This function will select some of them and process them from
     * <code>PartialCommit</code>s into <code>Commit</code>s.
     * @param processedCommits A list of all of the commits that have been
     * processed to be included in the history so far.
     * @param conflicts A list of all of the conflicts in the history. Used to
     * convert <code>PartialCommit</code>s into <code>Commit</code>s.
     * @param components A list of all of the <code>Component</code>s in the
     * history. Used to convert <code>PartialCommit</code>s into
     * <code>Commit</code>s.
     * @param gitGraph The <code>GitGraph</code> representing the full graph
     * output by <code>git log --graph</code>.
     * @return A list of commits that need to be added to
     * <code>processedCommits</code> or otherwise processed further in order to
     * have enough information to correctly represent the branching and merging
     * patterns in the final visualization.
     */
    private List<Commit>
        findCommitsCaughtInGraph(List<PartialCommit> partialCommits,
                List<Commit> processedCommits, List<Conflict> conflicts,
                List<Component> components, GitGraph gitGraph) {
            /*
             * This function runs in O(n) time with respect to the number of
             * commits. (If the number of conflicts or components is large, it's
             * O(commits * conflicts * components).) To keep the algorithmic
             * complexity low, we have to pick our data structures carefully.
             * That's why we copy several of the lists into HashMaps and
             * HashSets.
             */
            List<Commit> commitsCaughtInGraph = new LinkedList<>();
            
            // Creating a map of hashes to partial commits increases the amount
            // of memory we need by a constant factor, but allows us to look up
            // commits later in O(1) time. If we had to scan a list each time
            // we looked a commit, this would take O(n) time per commit and make
            // the entire function O(n^2). Copying the list into a map takes
            // O(n) time, but this isn't any worse than the primary loop below.
            HashMap<String, PartialCommit> partialCommitsByHashes =
                    new HashMap<>(partialCommits.size());
            for(PartialCommit partialCommit : partialCommits) {
                String hash = partialCommit.getHash();
                partialCommitsByHashes.put(hash, partialCommit);
            }
            
            // Likewise, creating a hashset of the commits we've already
            // processed increases the amout of memeory we use by a constant
            // factor but lets us look them up in O(1) time.
            HashSet<String> existingHashes = new HashSet<>();
            for(Commit processedCommit : processedCommits) {
                existingHashes.add(processedCommit.getHash());
            }

            // We need to find the commits that appear first and last in the
            // graph so we know which portion of the graph to look over. Running
            // through the list of processed commits one at a time may seem
            // naive, but it's O(n). This is better than i.e. sorting the list
            // and taking the first and last elements, which is O(n log n).
            String startingHash = null;
            String endingHash = null;
            int startingIndex = Integer.MIN_VALUE;
            int endingIndex = Integer.MAX_VALUE;
            for(Commit commit : processedCommits) {
                String commitHash = commit.getHash();
                int commitIndex = gitGraph.getPositionOfCommit(commitHash);
                if(commitIndex > startingIndex) {
                    startingHash = commitHash;
                    startingIndex = commitIndex;
                }
                if(commitIndex < endingIndex) {
                    endingHash = commitHash;
                    endingIndex = commitIndex;
                }
            }
            
            // Now we scan through the portion of th egraph that we've
            // identified as being relevant. Because we moved everything into
            // hashes in previous steps, each iteration of this loop is O(1)!
            // Overall, it is O(n).
            for(int i = startingIndex; i >= endingIndex; i--) {
                String hashFromGraph =
                        gitGraph.getRows().get(i).getCommitHash();
                
                if(!existingHashes.contains(hashFromGraph)) {
                    PartialCommit partialCommit =
                            partialCommitsByHashes.get(hashFromGraph);
                    Commit commit = processPartialCommit(partialCommit,
                            conflicts, components);
                    commitsCaughtInGraph.add(commit);
                }
            }

            return commitsCaughtInGraph;
    }
}