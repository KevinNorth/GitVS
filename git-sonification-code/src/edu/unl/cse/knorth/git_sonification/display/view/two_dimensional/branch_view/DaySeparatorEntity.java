package edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view;

import javax.sound.sampled.Clip;

public class DaySeparatorEntity {
    private final int numTimesToPlayDaySeparator;
    private int numTimesDaySeparatorHasBeenPlayed;

    /**
     * Creates a <code>DaySeparatorEntity</code>.
     * @param numTimesToPlayDaySeparator The number of times to play a day
     * separator sound.
     */
    public DaySeparatorEntity(int numTimesToPlayDaySeparator) {
        this.numTimesToPlayDaySeparator = numTimesToPlayDaySeparator;
        numTimesDaySeparatorHasBeenPlayed = 0;
    }
    
    /**
     * Call once per frame to play a day separator multiple times.
     * @param currentDaySeparator The <code>Clip</code> currently being used to
     * play a day separator.
     * @param daySeparatorClone Another instance of a <code>Clip</code> that has
     * the same day separator sound loaded. The <code>DaySeparatorEntity</code>
     * switches between the two to avoid an intermittent bug where playing a
     * <code>Clip</code> again immediately after it has finished playing can
     * result in silence.
     * @return The <code>Clip</code> that should be used to play the next
     * DaySeparator (whether or not a <code>Clip</code> is currently playing).
     */
    public Clip updateDaySeparatorSounds(Clip currentDaySeparator, Clip daySeparatorClone) {
        if(!currentDaySeparator.isRunning()) {
            currentDaySeparator.setFramePosition(0);
            if(!isDaySeparatorFinishedPlaying()) {
                System.out.println("Playing for the "
                        + (numTimesDaySeparatorHasBeenPlayed + 1)
                        + "xd time");
                daySeparatorClone.start();
            }
            numTimesDaySeparatorHasBeenPlayed++;
            return daySeparatorClone;
        } else {
            return currentDaySeparator;
        }
    }
    
    /**
     * Indicates whether the <code>DaySeparatorEntity</code> will still be used
     * to play day separator sounds.
     * @return <code>true</code> if the <code>DaySeparatorEntity</code> has
     * played the day separator sound the number of times it was supposed to.
     * <code>false</code> otherwise.
     */
    public boolean isDaySeparatorFinishedPlaying() {
        return numTimesDaySeparatorHasBeenPlayed >= numTimesToPlayDaySeparator;
    }
}
