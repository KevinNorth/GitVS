package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.layer_producer;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.AnnotatedCommit;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.AnnotatedCommitLine;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.Measure;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.SonificationData;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LayerProducer {
    public VisualizationData produceLayers(List<AnnotatedCommit> commits,
            SonificationData sonificationData, List<Component> components) {
        final int numStartBranches = commits.get(0).getMaxFromBranchNumber();
        final int numEndBranches =
                commits.get(commits.size() - 1).getMaxToBranchNumber();
        
        Map<Component, Layer> layerMap = prepareLayerMap(components);
        
        Iterator<Measure> measureIter = sonificationData.getMeasures()
                .iterator();
        for(AnnotatedCommit commit : commits) {
            Measure currentMeasure = measureIter.next();
            
            while(currentMeasure.isDaySeparator()) {
                addDaySeparatorRow(layerMap, commit, currentMeasure);
                currentMeasure = measureIter.next();
            }
            
            addRow(layerMap, commit, currentMeasure);
        }
        
        return new VisualizationData(layerMap.values(), numStartBranches,
                numEndBranches);
    }
    
    private Map<Component, Layer> prepareLayerMap(List<Component> components) {
        Map<Component, Layer> layerMap = new HashMap<>(components.size());
        
        for(Component component : components) {
            layerMap.put(component, new Layer());
        }
        
        return layerMap;
    }
    
    private void addRow(Map<Component, Layer> layerMap, AnnotatedCommit commit,
            Measure currentMeasure) {
        List<Line> lines = new ArrayList<>(
                commit.getIncomingBranches().size());
        for(AnnotatedCommitLine oldLine : commit.getIncomingBranches()) {
            lines.add(new Line(oldLine.getFromBranch(), oldLine.getToBranch(),
                    true));
        }
        
        for(Map.Entry<Component, Layer> componentAndLayer : layerMap.entrySet())
        {
            Component component = componentAndLayer.getKey();
            Layer layer = componentAndLayer.getValue();
            
            boolean isCommitVisible =
                    commit.getComponents().contains(component)
                    || commit.getComponents().isEmpty(); // is a merge commit
            boolean isMergeRow = commit.getComponents().isEmpty();
            Row row = new Row(commit.getAuthor(), commit.getTimestamp(),
                    commit.getBranch(), isCommitVisible, isMergeRow,
                    currentMeasure.getNumConflicts(), lines);
            layer.getRows().add(row);
        }
    }

    private void addDaySeparatorRow(Map<Component, Layer> layerMap,
            AnnotatedCommit commit, Measure currentMeasure) {
        List<Line> lines = new ArrayList<>(
            commit.getIncomingBranches().size());
        for(AnnotatedCommitLine oldLine : commit.getIncomingBranches()) {
            lines.add(new Line(oldLine.getFromBranch(), oldLine.getFromBranch(),
                true));
        }

        for(Layer layer : layerMap.values())
        {
            Row row = new Row(currentMeasure.getTimestamp(),
                    currentMeasure.getNumConflicts(), lines);
            layer.getRows().add(row);
        }
    }
}