package edu.unl.cse.knorth.git_sonification.display_preparation.sonification;

import edu.unl.cse.knorth.git_sonification.intermediate_data.Commit;
import edu.unl.cse.knorth.git_sonification.display_preparation.sonification.audio.AudioGenerator;
import edu.unl.cse.knorth.git_sonification.display_preparation.sonification.audio.per_commit.PerCommitAudioGenerator;
import edu.unl.cse.knorth.git_sonification.display_preparation.sonification.audio.per_commit.PerCommitMeasureProducer;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Sonifier {
    public void sonifyCommits(List<Commit> commits, String filepath) {
        try {
            MeasureProducer measureProducer = new PerCommitMeasureProducer();
            ConcurrentLinkedQueue<Measure> measures =
                    measureProducer.produceMeasures(commits);
            AudioGenerator audioGenerator = new PerCommitAudioGenerator();
                audioGenerator.produceAudio(measures, filepath);
        } catch (IOException ex) {
            System.err.println("Could not sonify git data:");
            ex.printStackTrace();
        }
    }
}