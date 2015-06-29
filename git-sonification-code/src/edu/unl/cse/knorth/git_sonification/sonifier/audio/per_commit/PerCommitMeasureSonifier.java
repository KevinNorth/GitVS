package edu.unl.cse.knorth.git_sonification.sonifier.audio.per_commit;

import edu.unl.cse.knorth.git_sonification.sonifier.Measure;
import edu.unl.cse.knorth.git_sonification.sonifier.audio.ClockSpeedController;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

/**
 * Contains the audio logic to sonify an individual measure for the
 * <code>PerCommitAudioGenerator</code>.
 */
public class PerCommitMeasureSonifier {
    public static final String CONFLICT_DRUMS_1_FILEPATH = "audio/conflict_drums_1.wav";
    public static final String CONFLICT_DRUMS_2_FILEPATH = "audio/conflict_drums_2.wav";
    public static final String CONFLICT_DRUMS_3_FILEPATH = "audio/conflict_drums_3.wav";
    public static final String CONFLICT_DRUMS_4_FILEPATH = "audio/conflict_drums_4.wav";
    public static final String DAY_SEPARATOR_FILEPATH = "audio/day_separator.wav";
    public static final String DEV_1_FILEPATH = "audio/dev1.wav";
    public static final String DEV_2_FILEPATH = "audio/dev2.wav";
    public static final String DEV_3_FILEPATH = "audio/dev3.wav";
    public static final String DEV_4_FILEPATH = "audio/dev4.wav";
    public static final String DEV_5_FILEPATH = "audio/dev5.wav";
    public static final String DEV_6_FILEPATH = "audio/dev6.wav";
    public static final String DEV_7_FILEPATH = "audio/dev7.wav";
    public static final String DEV_8_FILEPATH = "audio/dev8.wav";
    public static final String DEV_9_FILEPATH = "audio/dev9.wav";
    public static final String DEV_10_FILEPATH = "audio/dev10.wav";
    public static final String DEV_11_FILEPATH = "audio/dev11.wav";
    public static final String DEV_12_FILEPATH = "audio/dev12.wav";
    public static final String DEV_13_FILEPATH = "audio/dev13.wav";
    public static final String OTHER_DEV_FILEPATH = "audio/dev14.wav";
    
    /**
     * Sonifies an individual measure. The sonficiation isn't returned, but
     * rather it is sent directly to the <code>AudioContext</code> that is
     * passed in as an argument.
     * @param ac The <code>AudioContext</code> to which the sonified measure's
     * audio data should be sent.
     * @param measure The <code>Measure</code> to be sonified.
     * @param clockSpeedController A <code>ClockSpeedController</code> that can
     * be used to control when the next measure will be sonified. In this way,
     * the <code>PerCommitMeasureSonifier</code> can make its measure particularly long
     * or short if needed.
    */
    public void sonifyMeasure(AudioContext ac, Measure measure,
            ClockSpeedController clockSpeedController) {
        SampleManager.setVerbose(false);
        
        if(measure.getAuthor() != null) {
            sonifyAuthor(ac, measure.getAuthor());
        }
        
        if(measure.isDaySeparator()) {
            sonifyDaySeparator(ac);
        }
        
        if(measure.isInConflict())
            sonifyConflictDrums(ac, measure.getNumConflicts());
    }
    
    /**
     * Plays the earcon corresponding to the measure's author.
     * @param ac The AudioContext to play the earcon to.
     * @param author The String used to identify the author of the measure being
     * sonified.
     */
    public void sonifyAuthor(AudioContext ac, String author) {
        Sample sample;
        
        switch (author) {
            case "Alex Feinberg":
                sample = SampleManager.sample(DEV_1_FILEPATH);
                break;
            case "Roshan Sumbaly":
                sample = SampleManager.sample(DEV_2_FILEPATH);
                break;
            case "Jay Kreps":
                sample = SampleManager.sample(DEV_3_FILEPATH);
                break;
            case "Bhupesh Bansal":
                sample = SampleManager.sample(DEV_4_FILEPATH);
                break;
            case "Jay J Wylie":
                sample = SampleManager.sample(DEV_5_FILEPATH);
                break;
            case "Chinmay Soman":
                sample = SampleManager.sample(DEV_6_FILEPATH);
                break;
            case "Ismael Juma":
                sample = SampleManager.sample(DEV_7_FILEPATH);
                break;
            case "kirktrue":
                sample = SampleManager.sample(DEV_8_FILEPATH);
                break;
            case "Vinoth Chandar":
                sample = SampleManager.sample(DEV_9_FILEPATH);
                break;
            case "Kirk True":
                sample = SampleManager.sample(DEV_10_FILEPATH);
                break;
            case "Siddharth Singh":
                sample = SampleManager.sample(DEV_11_FILEPATH);
                break;
            case "Zhongjie Wu":
                sample = SampleManager.sample(DEV_12_FILEPATH);
                break;
            case "Lei Gao":
                sample = SampleManager.sample(DEV_13_FILEPATH);
                break;
            default:
                sample = SampleManager.sample(OTHER_DEV_FILEPATH);
                break;
        }

        SamplePlayer player = new SamplePlayer(ac, sample);
        Gain gain = new Gain(ac, 2, 1.0f);
        player.setKillOnEnd(true);
        gain.setKillListener(player.getEndListener());
        gain.addInput(player);
        ac.out.addInput(gain);
    }
    
    /**
     * Plays the day separator earcon for a day separator measure.
     * @param ac THe AudioContext to play the audio to.
     */
    public void sonifyDaySeparator(AudioContext ac) {
        Sample sample = SampleManager.sample(DAY_SEPARATOR_FILEPATH);
        
        SamplePlayer player = new SamplePlayer(ac, sample);
        Gain gain = new Gain(ac, 2, 1.0f);
        player.setKillOnEnd(true);
        gain.setKillListener(player.getEndListener());
        gain.addInput(player);
        ac.out.addInput(gain);
    }
    
    /**
     * Plays the conflict drums earcon for a measure that is part of a conflict.
     * @param ac THe AudioContext to play the audio to.
     * @param numConflicts The number of simultaneous conflicts being sonified.
     * Used to select louder drums for more conflicts.
     */
    public void sonifyConflictDrums(AudioContext ac, int numConflicts) {
        Sample sample;
        
        switch(numConflicts) {
            case 1:
                sample = SampleManager.sample(CONFLICT_DRUMS_1_FILEPATH);
                break;
            case 2:
                sample = SampleManager.sample(CONFLICT_DRUMS_2_FILEPATH);
                break;
            case 3:
                sample = SampleManager.sample(CONFLICT_DRUMS_3_FILEPATH);
                break;
            default:
                sample = SampleManager.sample(CONFLICT_DRUMS_4_FILEPATH);
                break;
        }
        
        SamplePlayer player = new SamplePlayer(ac, sample);
        Gain gain = new Gain(ac, 2, 1.0f);
        player.setKillOnEnd(true);
        gain.setKillListener(player.getEndListener());
        gain.addInput(player);
        ac.out.addInput(gain);
    }
}