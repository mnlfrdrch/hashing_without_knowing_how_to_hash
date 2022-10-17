package localhost.hashing_without_knowing_how_to_hash.util;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

class HexUtilTest {


    @Test
    public void testStringAsHexToBitSetForSingleCharacterSequence(){
        //given
        String hashValue="a"; //1010
        BitSet expectedEncodedValue=new FixedLengthBitSet(4);
        expectedEncodedValue.set(0, true);
        expectedEncodedValue.set(2, true);

        //when
        BitSet actualEncodedValue= HexUtil.convertHexStringIntoBits(hashValue);

        //then
        assertEquals(expectedEncodedValue.length(), actualEncodedValue.length());

        for(int i=0; i<actualEncodedValue.length(); i++){
            assertEquals(expectedEncodedValue.get(i), actualEncodedValue.get(i));
        }
    }

    @Test
    public void testStringAsHexToBitSetForTwoCharactersSequence(){
        //given
        String hashValue="7a"; //01111010
        BitSet expectedEncodedValue=new FixedLengthBitSet(8);
        expectedEncodedValue.set(1, true);
        expectedEncodedValue.set(2, true);
        expectedEncodedValue.set(3, true);
        expectedEncodedValue.set(4, true);
        expectedEncodedValue.set(6, true);

        //when
        BitSet actualEncodedValue= HexUtil.convertHexStringIntoBits(hashValue);

        //then
        assertEquals(expectedEncodedValue.length(), actualEncodedValue.length());

        for(int i=0; i<actualEncodedValue.length(); i++){
            assertEquals(expectedEncodedValue.get(i), actualEncodedValue.get(i));
        }
    }

    @Test
    public void testStringAsHexToBitSetForThreeCharactersSequence(){
        //given
        String hashValue="07a"; //000001111010
        BitSet expectedEncodedValue=new FixedLengthBitSet(12);
        expectedEncodedValue.set(5, true);
        expectedEncodedValue.set(6, true);
        expectedEncodedValue.set(7, true);
        expectedEncodedValue.set(8, true);
        expectedEncodedValue.set(10, true);

        //when
        BitSet actualEncodedValue= HexUtil.convertHexStringIntoBits(hashValue);

        //then
        assertEquals(expectedEncodedValue.length(), actualEncodedValue.length());

        for(int i=0; i<actualEncodedValue.length(); i++){
            assertEquals(expectedEncodedValue.get(i), actualEncodedValue.get(i));
        }
    }
}