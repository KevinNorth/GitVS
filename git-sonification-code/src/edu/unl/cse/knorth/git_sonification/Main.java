package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.GitCaller;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.VisualizationProcessor;
import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.io.IOException;
import org.joda.time.DateTime;

public class Main {
    public static void main(String[] args) throws IOException {
//        GregorianCalendar cal = new GregorianCalendar();
//        cal.set(2009, 11, 23);
        DateTime since = new DateTime(2009, 11, 4, 0, 0);
//        cal.set(2010, 0, 6);
        DateTime until = new DateTime(2009, 11, 9, 0, 0);
                
        ViewModel viewModel =
                new GitDataProcessor().processGitData("../../voldemort/.git",
                        since, until);
        
        VisualizationData visualizationData = viewModel.getVisualizationData();
                
        for(Layer layer : visualizationData.getLayers()) {
            System.out.println(layer);
        }
    }
}
