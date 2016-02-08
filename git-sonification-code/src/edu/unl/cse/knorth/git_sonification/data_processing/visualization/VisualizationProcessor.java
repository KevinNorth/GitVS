package edu.unl.cse.knorth.git_sonification.data_processing.visualization;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_collection.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraph;
import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.AnnotatedCommit;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits.CommitAnnotator;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.layer_producer.LayerProducer;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.SonificationData;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.util.List;

public class VisualizationProcessor {
    public VisualizationData produceVisualizationData(List<Commit> commits,
            List<Component> components, SonificationData sonificationData,
            List<Conflict> conflicts, GitGraph gitGraph) {
        List<AnnotatedCommit> annotatedCommits =
                new CommitAnnotator().annotateCommits(commits, gitGraph);
        return new LayerProducer().produceLayers(annotatedCommits,
                sonificationData, components, conflicts);
    }
}