package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

import edu.unl.cse.knorth.git_sonification.utility.StreamGobbler;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class GitGraphProducer {
    public GitGraph produceGitGraph(String repositoryLocation)
            throws IOException {
        List<String> rawGraphData = callGitLog(repositoryLocation);
        List<Row> rows = processRows(rawGraphData);
        return new GitGraph(rows);
    }
    
    private List<String> callGitLog(String repositoryLocation)
            throws IOException {
        File workingDirectory = new File(repositoryLocation);
        
        String commandLineString = "git log --format=%H --graph"
                + " --author-date-order --no-color";
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(commandLineString.trim().split("\\s+"), null,
                workingDirectory);

        System.out.println("Running `" + commandLineString + "` in order to "
                + "find git commits...");
        
        // Output the process's output to STDOUT so the end user can see it
        StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream());
        errorGobbler.start();
        
        try {            
            List<String> rawGraphData = new LinkedList<String>();
            InputStreamReader isr = new InputStreamReader(pr.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null)
                rawGraphData.add(line);

            return rawGraphData;
        } finally {
            try {
                errorGobbler.join(100);
            } catch(InterruptedException err) { /* Swallow on purpose */ }
        }
    }
    
    private List<Row> processRows(List<String> rawGraphData) {
        LinkedList<Row> rows = new LinkedList<>();
        
        for(String rawRow : rawGraphData) {
            String[] split = rawRow.split("\\s+");
            String hash = split[split.length - 1];
            if(hash.matches("[a-f0-9]+")) {
                rows.add(new Row(hash));
            }
        }
        
        return rows;
    }
}