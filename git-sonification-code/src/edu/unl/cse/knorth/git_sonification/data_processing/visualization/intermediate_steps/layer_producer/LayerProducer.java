package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.layer_producer;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.AnnotatedCommit;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.AnnotatedCommitLine;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.Measure;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.SonificationData;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Commit;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LayerProducer {
    public VisualizationData produceLayers(List<AnnotatedCommit> commits,
            SonificationData sonificationData, List<Component> components) {
        final int numStartBranches = commits.get(0).getMaxFromBranchNumber();
        final int numEndBranches =
                commits.get(commits.size() - 1).getMaxToBranchNumber();
        
        Layer combinedLayer = new Layer();
        
        Iterator<Measure> measureIter = sonificationData.getMeasures()
                .iterator();
        for(AnnotatedCommit commit : commits) {
            Measure currentMeasure = measureIter.next();

            if(currentMeasure.isDaySeparator()) {
                Measure lastDaySeparator;
                do {
                    lastDaySeparator = currentMeasure;
                    currentMeasure = measureIter.next();
                } while(currentMeasure.isDaySeparator());
                
                addDaySeparatorRow(combinedLayer, commit,
                        lastDaySeparator);
            }

            
            addRow(combinedLayer, commit, currentMeasure);
        }
        
        return new VisualizationData(combinedLayer, components,
                numStartBranches, numEndBranches);
    }
        
    private void addRow(Layer combinedLayer, AnnotatedCommit commit,
            Measure currentMeasure) {
        List<Line> lines = new ArrayList<>(
                commit.getIncomingBranches().size());

        edu.unl.cse.knorth.git_sonification.
            display.model.visualization.Commit modelCommit =
                new Commit(commit.getHash(), commit.getAuthor(),
                    commit.getTimestamp(), commit.getComponents());
        
        for(AnnotatedCommitLine oldLine : commit.getIncomingBranches()) {
            lines.add(new Line(oldLine.getFromBranch(), oldLine.getToBranch()));
        }
                
        Row row = new Row(commit.getAuthor(), commit.getTimestamp(),
            commit.getBranch(), currentMeasure.getNumConflicts(),
                modelCommit, lines);
        combinedLayer.getRows().add(row);
    }

    private void addDaySeparatorRow(Layer combinedLayer, AnnotatedCommit commit,
            Measure currentMeasure) {
        List<Line> lines = new ArrayList<>(
            commit.getIncomingBranches().size());
        for(AnnotatedCommitLine oldLine : commit.getIncomingBranches()) {
            lines.add(new Line(oldLine.getFromBranch(),
                    oldLine.getFromBranch()));
        }

        Row row = new Row(currentMeasure.getTimestamp(),
            currentMeasure.getNumConflicts(), lines);
        combinedLayer.getRows().add(row);
    }
}