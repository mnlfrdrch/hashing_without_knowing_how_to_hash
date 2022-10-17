package test.java.localhost.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

class BijectionHexAndBitsTest {

    BijectionHexAndBits bijection;

    @BeforeEach
    public void setUp(){
        bijection=new BijectionHexAndBits(2);
    }

    @Test
    public void testEncode(){
        //given
        String hexString="00";
        BitSet expectedEncodedHexString=new FixedLengthBitSet(8);

        //when
        BitSet actualEncodedHexString=bijection.encode(hexString);

        //then
        assertEquals(expectedEncodedHexString, actualEncodedHexString);
    }

    @Test
    public void testDecode(){
        //given
        String originalHexString="e3";
        BitSet expectedEncodedHexString=bijection.encode(originalHexString);

        //when
        String actualHexString=bijection.decode(expectedEncodedHexString);

        //then
        assertEquals(originalHexString, actualHexString);
    }

}