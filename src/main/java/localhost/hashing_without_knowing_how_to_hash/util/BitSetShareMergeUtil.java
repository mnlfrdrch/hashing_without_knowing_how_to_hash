package localhost.hashing_without_knowing_how_to_hash.util;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import lombok.experimental.UtilityClass;

import java.util.BitSet;
import java.util.List;

/**
 * Is able to merge multiple hash function shares back into a single hash function secret
 * Provides functions for merging multiple partial hash function values (shares) into the secret hash function value
 */
@UtilityClass
public class BitSetShareMergeUtil {

    /**
     * Merges multiple BitSets into a single BitSet secret by applying the bitwise XOR Operation
     * @param shares e.g. a List of shares A,B,C
     * @return the secret (A XOR B XOR C)
     */
    public BitSet mergeUsingXOR(List<BitSet> shares){
        int len=determineMaxLength(shares);
        FixedLengthBitSet xoredBitSet=new FixedLengthBitSet(len);
        xoredBitSet.clear();

        for(BitSet bitSet:shares){
            xoredBitSet.xor(bitSet);
        }

        return xoredBitSet;
    }

    private int determineMaxLength(List<BitSet> shares){
        int maxLength=0;

        for(BitSet b: shares){
            if(b.length()>maxLength)
                maxLength=b.length();
        }

        return maxLength;
    }

}
