package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraph;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraphLine;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraphRow;
import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Processes commits to determine where they should appear along the time
 * and branch axes in the final visualization. This class produces
 * "annotated commits" containing this positioning information. These
 * annotated commits can then be processed to determine which commits and
 * branch lines should be shown in each z-dimensional layer, but in a
 * future step.
 * @author ThePondermatic
 */
public class CommitAnnotator {
    /**
     * Processes raw commits to determine and attach the following information
     * for them:
     * <ul>
     * <li>Where they should appear vertically in the final visualization,
     * determined by timestamp and order in the commit list</li>
     * <li>Where they should appear horizontally in the final visualization,
     * determined by assigning a branch number</li>
     * <li>Which visual lines should appear to show branches merging and
     * splitting off, determined by giving each commit a list of incoming
     * branches to draw lines for</li>
     * </ul>
     * 
     * All of this information will be further processed, but in a future step
     * (i.e. not this class).
     * @param commitsToAnnotate The raw commits to process
     * @return A list of AnnotatedCommits containing the additional information
     * described above.
     */
    public List<AnnotatedCommit> annotateCommits(List<Commit> commitsToAnnotate,
            GitGraph gitGraph)
    {
        List<AnnotatedCommit> annotatedCommits =
                new ArrayList<>(commitsToAnnotate.size());
                
        for(Commit commit : commitsToAnnotate) {
            AnnotatedCommit result = annotateCommit(commit, gitGraph);
            annotatedCommits.add(result);
        }
        
        return annotatedCommits;
    }

    private AnnotatedCommit annotateCommit(Commit commit, GitGraph gitGraph) {
        GitGraphRow row = gitGraph.getRowForCommit(commit.getHash());
        
        DateTime timestamp = commit.getTimestamp();
        String hash = commit.getHash();
        String author = commit.getAuthor();
        List<Component> components = commit.getComponentsModified();
        
        int branch = row.getCommitBranchPosition();

        List<GitGraphLine> rowLines = row.getIncomingLines();
        List<AnnotatedCommitLine> lines = new ArrayList<>(rowLines.size());
        for(GitGraphLine rowLine : rowLines) {
            lines.add(new AnnotatedCommitLine(rowLine.getFromBranch(), 
                    rowLine.getToBranch()));
        }
        
        return new AnnotatedCommit(timestamp, hash, author, branch, lines,
                components);
    }
}