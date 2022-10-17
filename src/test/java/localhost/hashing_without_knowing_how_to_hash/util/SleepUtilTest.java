package test.java.localhost.hashing_without_knowing_how_to_hash.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

class SleepUtilTest {

    @Test
    public void test(){
        //given
        long toleranceMs=10;
        long expectedMs=(long)SleepUtil.TIMEOUT_MS;
        long startMs=System.currentTimeMillis();

        //when
        SleepUtil.sleep();
        long stopMs=System.currentTimeMillis();

        //then
        long differenceAbsoluteMs=stopMs-startMs;
        long deviationFromExpectedTime=differenceAbsoluteMs-expectedMs;
        assertTrue((-1)*toleranceMs < deviationFromExpectedTime && deviationFromExpectedTime < toleranceMs);
    }

}