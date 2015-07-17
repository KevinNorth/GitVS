package edu.unl.cse.knorth.git_sonification.data_processing.sonification.audio;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Function;

/**
 * A UGen used to control the speed of the main Clock in the AudioGenerator.
 * This UGen works by constantly returning the amount of time, in milliseconds,
 * that the Clock should wait between ticks. This amount of time can be
 * immediately changed at any time, allowing the clock to speed up or slow down
 * depending on how long we want each sound to be.
 * @author knorth
 */
public class ClockSpeedController extends Function {
    private float clockSpeed;
    
    /**
     * Creates a ClockSpeedController that starts at the specified speed.
     * @param ac The AudioContext for the class
     * @param clockSpeed The speed to start the clock at, in milliseconds
     * between ticks
     */
    public ClockSpeedController(AudioContext ac, float clockSpeed) {
        super(new Envelope(ac, 1)); //The envelope we pass in is a dummy - it's just needed to let the Function superclass work
        this.clockSpeed = clockSpeed; 
    }
    
    /**
     * Constantly returns the speed the clock should be running
     * @return The speed of the clock, in milliseconds between ticks.
     */
    @Override
    public float calculate() {
        return (float) clockSpeed;
    }
    
    /**
     * Gets the speed of the clock in milliseconds between ticks
     * @return The speed of the clock in milliseconds between ticks
     */
    public float getClockSpeed() {
        return clockSpeed;
    }

    /**
     * Immediately changes the speed of the clock.
     * @param clockSpeed The new speed of the clock, in milliseconds between
     * ticks
     */
    public void setClockSpeed(float clockSpeed) {
        this.clockSpeed = clockSpeed;
    }
}