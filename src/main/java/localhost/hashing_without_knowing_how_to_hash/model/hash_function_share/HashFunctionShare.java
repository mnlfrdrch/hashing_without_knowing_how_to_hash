package localhost.hashing_without_knowing_how_to_hash.model.hash_function_share;

import localhost.hashing_without_knowing_how_to_hash.model.BijectionQGramsAndBits;
import localhost.hashing_without_knowing_how_to_hash.util.HexUtil;
import lombok.Getter;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements the individual plaintext hash function share of each host
 * Applies the selected hashing algorithm to each qgram in Domain
 * Generates a mapping of (qgram,h(qgram)) for all qgrams in Domain this ways
 * This mapping can be requested to generate a formula for the garbled circuit from it
 */
public class HashFunctionShare {

    @Getter
    private Domain domain;
    private HashingAlgorithm algorithm;
    private Map<BitSet, BitSet> functionMapping;

    public HashFunctionShare(Domain domain, HashingAlgorithm algorithm){
        this.domain=domain;
        this.algorithm=algorithm;
        functionMapping=new HashMap<>();
    }

    /**
     * Gets the calculated function mapping
     * If generateFunctionMapping() had not been called yet, this function calculates the function mapping and returns it
     * @return the calculated function mapping
     */
    public Map<BitSet, BitSet> getFunctionMapping(){
        if(functionMapping.size()==0){
            functionMapping=generateFunctionMapping();
        }
        return functionMapping;
    }

    /**
     * Applies the hashing algorithm for all qgrams of teh specified domain.
     * Saves all the pairs (qgram, h(qgram)) as a map.
     * @return the mapping (qgram, h(gram)) for all qgrams in Domain
     */
    public Map<BitSet, BitSet> generateFunctionMapping(){
        Map<BitSet, BitSet> functionMapping=new HashMap<>();
        List<String> xList=domain.getAllQGrams();

        for(String x:xList){
            String y=algorithm.h(x);

            BitSet bitSetOfX=stringToBitSet(x);
            BitSet bitSetOfy=HexUtil.convertHexStringIntoBits(y);

            functionMapping.put(bitSetOfX,bitSetOfy);
        }

        return functionMapping;
    }

    private BitSet stringToBitSet(String s){
        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);
        BitSet encodedNGram=bijection.encodeQGramAsBitSet(s);
        return encodedNGram;
    }
}
