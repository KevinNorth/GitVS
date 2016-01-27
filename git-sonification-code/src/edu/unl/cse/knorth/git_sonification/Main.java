package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraph;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraphProducer;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.Row;
import java.io.IOException;
import org.joda.time.DateTime;

public class Main {
    public static void main(String[] args) throws IOException {
//        GregorianCalendar cal = new GregorianCalendar();
//        cal.set(2009, 11, 23);
        DateTime since = new DateTime(2009, 11, 4, 0, 0);
//        cal.set(2010, 0, 6);
        DateTime until = new DateTime(2009, 11, 9, 0, 0);
        
        GitGraph graph = new GitGraphProducer()
                .produceGitGraph("../../voldemort/.git");
        
        for(Row row : graph.getRows()) {
            System.out.println(row.getCommitHash());
        }
        
        
//        ViewModel viewModel =
//                new GitDataProcessor().processGitData("../../voldemort/.git",
//                        since, until,
//                        CreateComponentTechniques.EACH_INDIVIDUAL_FILE);
//        
//        
//        
//        VisualizationData visualizationData = viewModel.getVisualizationData();
//                
////        for(Layer layer : visualizationData.getLayers()) {
////            System.out.println(layer);
////        }
//        
//        System.out.println(
//                new GraphStringifier().stringifyVisualizationData(
//                        visualizationData, false));
    }
}
