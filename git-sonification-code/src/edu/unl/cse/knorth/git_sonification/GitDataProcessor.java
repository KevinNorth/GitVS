package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.data_collection.commit_processor.CommitProcessor;
import edu.unl.cse.knorth.git_sonification.data_collection.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.data_collection.conflict_data.ConflictDataParser;
import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.GitCaller;
import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.data_collection.git_caller.PartialCommitsAndAuthorCommitCounts;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraph;
import edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller.GitGraphProducer;
import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.data_processing.sonification.SonificationProcessor;
import edu.unl.cse.knorth.git_sonification.data_processing.visualization.VisualizationProcessor;
import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import edu.unl.cse.knorth.git_sonification.display.model.sonification.SonificationData;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GitDataProcessor {
    public ViewModel processGitData(String targetGitRepoLocation,
            String firstHash, String lastHash,
            String conflictsLocation, String componentsLocation)
            throws IOException {
        List<PartialCommit> partialCommits;
        Map<String, Integer> authorCommitCounts;
        try(GitCaller gitCaller = new GitCaller(targetGitRepoLocation)) {
            PartialCommitsAndAuthorCommitCounts results =
                    gitCaller.getPartialCommits();
            partialCommits = results.getPartialCommits();
            authorCommitCounts = results.getAuthorCommitCounts();
        }
        
        List<Conflict> conflicts = new ConflictDataParser()
                .parseConflictData(conflictsLocation);
        
        File gitRepoRoot = new File(targetGitRepoLocation).getParentFile();
        
        GitGraph gitGraph = new GitGraphProducer()
                .produceGitGraph(targetGitRepoLocation);
        
        List<Commit> commits = new CommitProcessor()
                .processCommits(partialCommits, conflicts, firstHash, lastHash,
                        gitGraph);
                
        SonificationProcessor sonificationProcessor = new SonificationProcessor();
        SonificationData sonificationData =
            sonificationProcessor.processSonificationData(authorCommitCounts);
                
        VisualizationData visualizationData =
                new VisualizationProcessor().produceVisualizationData(commits,
                        sonificationData, conflicts, gitGraph);
        
        return new ViewModel(visualizationData, sonificationData);
    }
}