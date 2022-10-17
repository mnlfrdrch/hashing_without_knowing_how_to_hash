package localhost.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.constants.CharacterSets;
import localhost.hashing_without_knowing_how_to_hash.util.HexUtil;
import localhost.hashing_without_knowing_how_to_hash.util.QGramUtil;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Creates a bijection between the hexadecimal hash value and a representation of this value in bits
 * Is required, that the hash value secret can be merged by the hash value shares.
 * The hash value shares are of type BitSet so evaluation of individual bits and merging the shares using XOR is possible.
 * The hash value printed to the screen and saved to the HashValueCache is a hexadecimal String tough.
 * BijectionHexAndBits is therefore used to bring the hash value secret into the right format
 */
public class BijectionHexAndBits {

    private Map<String, BitSet> hexToBitSet;
    private Map<BitSet, String> bitSetToHex;

    /**
     * The constructor takes the argument
     * @param numChars ,which determines the common length of the hexadecimal notation
     */
    public BijectionHexAndBits(int numChars){
        hexToBitSet=new HashMap<>();
        bitSetToHex=new HashMap<>();
        generateHexMapsToBitSet(numChars);
        generateBitSetToHexMaps();
    }

    private void generateHexMapsToBitSet(int numChars){
        Set<String> allHexadecimalQGrams=QGramUtil.generateAllPossibleQGrams(numChars, CharacterSets.HEXADECIMAL);

        for(String qGram:allHexadecimalQGrams){
            BitSet encodedQGrams= HexUtil.convertHexStringIntoBits(qGram);
            hexToBitSet.put(qGram, encodedQGrams);
        }
    }

    private void generateBitSetToHexMaps(){
        for(String hexQGram: hexToBitSet.keySet()){
            BitSet encodedQGram=hexToBitSet.get(hexQGram);
            bitSetToHex.put(encodedQGram, hexQGram);
        }
    }

    /**
     * Converts hexadecimal notation to bit notation
     * @param hex any hexadecimal string of previously defined length
     * @return the bit representation of the argument
     */
    public BitSet encode(String hex){
        return hexToBitSet.get(hex);
    }

    /**
     * Converts bit notation back to hexadecimal notation
     * @param encodedHex any BitSet stored in the maps of BijectionHexAndBits
     * @return the hexadecimal representation of argument
     */
    public String decode(BitSet encodedHex){
        return bitSetToHex.get(encodedHex);
    }
}
