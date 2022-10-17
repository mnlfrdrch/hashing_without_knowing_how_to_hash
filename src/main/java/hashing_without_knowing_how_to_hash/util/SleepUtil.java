package main.java.hashing_without_knowing_how_to_hash.util;

import lombok.experimental.UtilityClass;

/**
 * Provides a simple-to-use function for pausing 1s
 */
@UtilityClass
public class SleepUtil {

    public final int TIMEOUT_MS=1000;
    private final int NUM_OF_RETRY=5;

    /**
     * Waits about 1s
     */
    public void sleep(){
        tryToSleep(NUM_OF_RETRY);
    }

    private void tryToSleep(int attemptsLeft){
        try {
            Thread.sleep(TIMEOUT_MS);
        }
        catch (InterruptedException interruptedException){
            retryIfInterruptedException(interruptedException, attemptsLeft-1);
        }
    }

    private void retryIfInterruptedException(InterruptedException interruptedException, int nTimes){
        if(nTimes>0) {
            tryToSleep(nTimes - 1);
        }
        else {
            interruptedException.printStackTrace();
        }
    }
}
