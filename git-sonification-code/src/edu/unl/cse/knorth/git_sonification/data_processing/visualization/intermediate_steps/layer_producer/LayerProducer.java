package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.layer_producer;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_collection.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.AnnotatedCommit;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.AnnotatedCommitLine;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.SonificationData;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Commit;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

public class LayerProducer {
    public VisualizationData produceLayers(List<AnnotatedCommit> commits,
            SonificationData sonificationData, List<Component> components,
            List<Conflict> conflicts) {
        final int numStartBranches = commits.get(0).getMaxFromBranchNumber();
        final int numEndBranches =
                commits.get(commits.size() - 1).getMaxToBranchNumber();

        List<Conflict> currentConflicts = new LinkedList<>();
        
        Layer combinedLayer = new Layer();

        AnnotatedCommit previousCommit = null;
        for(AnnotatedCommit commit : commits) {
            if((previousCommit != null) && (areTimestampsOnDifferentDays(
                    previousCommit.getTimestamp(), commit.getTimestamp()))) {
                addDaySeparatorRow(combinedLayer, commit,
                        currentConflicts.size());
            }
            
            List<Conflict> newConflicts =
                    findStartedConflicts(commit, conflicts);
            currentConflicts.addAll(newConflicts);
            
            List<Conflict> resolvedConflicts = findResolvedConflicts(commit,
                    conflicts);
            currentConflicts.removeAll(resolvedConflicts);
            
            addRow(combinedLayer, commit, currentConflicts.size());
            
            previousCommit = commit;
        }
        
        return new VisualizationData(combinedLayer, components,
                numStartBranches, numEndBranches);
    }
        
    private void addRow(Layer combinedLayer, AnnotatedCommit commit,
            int numConflicts) {
        List<Line> lines = new ArrayList<>(
                commit.getIncomingBranches().size());

        edu.unl.cse.knorth.git_sonification.
            display.model.visualization.Commit modelCommit =
                new Commit(commit.getHash(), commit.getAuthor(),
                    commit.getTimestamp(), commit.getComponents());
        
        for(AnnotatedCommitLine oldLine : commit.getIncomingBranches()) {
            lines.add(new Line(oldLine.getFromBranch(), oldLine.getToBranch(),
                oldLine.getColor()));
        }
                
        Row row = new Row(commit.getAuthor(), commit.getTimestamp(),
            commit.getBranch(), numConflicts, modelCommit, lines);
        combinedLayer.getRows().add(row);
    }

    private void addDaySeparatorRow(Layer combinedLayer, AnnotatedCommit commit,
            int numConflicts) {
        List<Line> lines = new ArrayList<>(
            commit.getIncomingBranches().size());
        for(AnnotatedCommitLine oldLine : commit.getIncomingBranches()) {
            lines.add(new Line(oldLine.getFromBranch(),
                    oldLine.getFromBranch(), oldLine.getColor()));
        }

        Row row = new Row(commit.getTimestamp().withTimeAtStartOfDay(),
                numConflicts, lines);
        combinedLayer.getRows().add(row);
    }

    private boolean areTimestampsOnDifferentDays(DateTime timestamp1,
            DateTime timestamp2) {
        return DateTimeComparator.getDateOnlyInstance().compare(timestamp1,
                timestamp2) != 0;
    }

    private List<Conflict> findStartedConflicts(AnnotatedCommit commit,
            List<Conflict> conflicts) {
        List<Conflict> startedConflicts = new LinkedList<>();
        
        for(Conflict conflict : conflicts) {
            if(commit.getHash().equals(conflict.getCommitIntroducingConflict()))
            {
                startedConflicts.add(conflict);
            }
        }
        
        return startedConflicts;
    }

    private List<Conflict> findResolvedConflicts(AnnotatedCommit commit,
            List<Conflict> conflicts) {
        List<Conflict> resolvedConflicts = new LinkedList<>();
        
        for(Conflict conflict : conflicts) {
            if(commit.getHash().equals(conflict.getCommitResolvingConflict())) {
                resolvedConflicts.add(conflict);
            }
        }
        
        return resolvedConflicts;
    }
}