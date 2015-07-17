package edu.unl.cse.knorth.git_sonification.data_processing.sonification.audio;

import edu.unl.cse.knorth.git_sonification.data_processing.sonification.Measure;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The class responsible for producing the audio file that contains the
 * sonification.
 */
public abstract class AudioGenerator {
    /**
     * Produces the audio for the sonification.
     * @param measures The threadsafe queue of <code>Measure</code>s to sonifiy
     * @param filepath The file to save the audio to
     * @throws IOException If there was a problem saving the audio
     */
    public abstract void produceAudio(ConcurrentLinkedQueue<Measure> measures,
            String filepath) throws IOException;
}