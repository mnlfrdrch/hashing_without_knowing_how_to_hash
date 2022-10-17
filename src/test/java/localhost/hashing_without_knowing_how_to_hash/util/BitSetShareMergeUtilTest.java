package localhost.hashing_without_knowing_how_to_hash.util;

import mockdata.BitSetTestData;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BitSetShareMergeUtilTest {

    @Test
    public void testMergeUsingXOR(){
        //given
        BitSet a= BitSetTestData.bitSetA();
        BitSet b= BitSetTestData.bitSetB();
        BitSet c= BitSetTestData.bitSetC();
        List<BitSet> setOfBitSets=List.of(a,b,c);
        BitSet expectedXORed= BitSetTestData.bitSetAxorBxorC();

        //when
        BitSet actualXORedBitSet=BitSetShareMergeUtil.mergeUsingXOR(setOfBitSets);

        //then
        assertEquals(expectedXORed, actualXORedBitSet);
    }

}