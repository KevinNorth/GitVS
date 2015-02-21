package edu.unl.cse.knorth.git_sonification.sonifier.audio.per_commit;

import edu.unl.cse.knorth.git_sonification.sonifier.Measure;
import edu.unl.cse.knorth.git_sonification.sonifier.audio.ClockSpeedController;
import net.beadsproject.beads.core.AudioContext;

/**
 * Contains the audio logic to sonify an individual measure.
 */
public class MeasureSonifier {
    /**
     * Sonifies an individual measure. The sonficiation isn't returned, but
     * rather it is sent directly to the <code>AudioContext</code> that is
     * passed in as an argument.
     * @param ac The <code>AudioContext</code> to which the sonified measure's
     * audio data should be sent.
     * @param measure The <code>Measure</code> to be sonified.
     * @param clockSpeedController A <code>ClockSpeedController</code> that can
     * be used to control when the next measure will be sonified. In this way,
     * the <code>MeasureSonifier</code> can make its measure particularly long
     * or short if needed.
     */
    public void sonifyMeasure(AudioContext ac, Measure measure,
            ClockSpeedController clockSpeedController) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}