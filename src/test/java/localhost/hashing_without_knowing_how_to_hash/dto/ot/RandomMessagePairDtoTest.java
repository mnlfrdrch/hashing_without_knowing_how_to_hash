package localhost.hashing_without_knowing_how_to_hash.dto.ot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomMessagePairDtoTest {

    private RandomMessagePairDto randomMessagePair;

    @AfterEach
    public void tearDown(){
        randomMessagePair=null;
    }

    @Test
    public void testRandomMessagesShouldBeOfDeterminedLength(){
        //given
        int expectedBitLength=1024;
        int tolerance=10;

        //when
        randomMessagePair=new RandomMessagePairDto(expectedBitLength);
        int actualBitLength=randomMessagePair.getX0().bitLength();

        //then
        assertTrue(expectedBitLength-tolerance < actualBitLength && actualBitLength < expectedBitLength+tolerance);
    }

}