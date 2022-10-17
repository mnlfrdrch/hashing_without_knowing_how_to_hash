package main.java.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.BijectionQGramsAndBits;
import localhost.hashing_without_knowing_how_to_hash.model.circuit.HashValueEvaluator;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.Domain;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmFactory;
import localhost.hashing_without_knowing_how_to_hash.util.BitSetShareMergeUtil;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Implements the actual hashing protocol, which is able to securely evaluate a single hash function secret
 */
public class HashingProtocol {
    private HashingAlgorithm ownHashingAlgorithm;
    private List<HostDto> hosts;

    private BijectionQGramsAndBits bijectionQGramsAndBits;
    private BijectionHexAndBits bijectionHexAndBits;

    public HashingProtocol(HashingAlgorithm ownHashingAlgorithm,List<HostDto> hosts){
        this.ownHashingAlgorithm=ownHashingAlgorithm;
        this.hosts=hosts;

        Domain domain=new Domain(ModelMetaData.Q, ModelMetaData.CHARACTERS);
        bijectionQGramsAndBits =new BijectionQGramsAndBits(domain);
        int hashValueLength=HashingAlgorithmFactory.instantiateHashingAlgorithmWithRandomKey(ModelMetaData.ALGORITHM_TYPE).getHashValueLength();
        bijectionHexAndBits=new BijectionHexAndBits(hashValueLength);
    }

    /**
     * Checks, whether secure evaluation can be applied for the given value
     * A value, that could not be computes would be a value, that is not part of the specified domain
     * @param x the value, to which h(x) should be computed
     * @return *true* if no problems are expected, else *false*
     */
    public boolean isHashable(String x){
        boolean isHashable=false;
        BitSet encodedHashFunctionArgument= bijectionQGramsAndBits.encodeQGramAsBitSet(x);
        isHashable=(encodedHashFunctionArgument!=null);
        return isHashable;
    }

    /**
     * Implements the protocol, which evaluates a hash value using SMC and Secret Sharing
     * @param qgram the qgram for which the hash value should be evaluated
     * @return h(gram)
     */
    public String h(String qgram){
        String secretHashValue=null;
        BitSet encodedQGram=convertQGramFromStringToBitSet(qgram);

        List<BitSet> encodedHashValueShares=new ArrayList<>();

        BitSet ownHashValueShare=computeEncodedHashValueShareOfOwnHashFunction(qgram);
        encodedHashValueShares.add(ownHashValueShare);

        for(HostDto host:hosts){
            BitSet encodedHashValueShare=evaluateHashValueShare(host, encodedQGram);
            encodedHashValueShares.add(encodedHashValueShare);
        }

        BitSet encodedSecretHashValue=xorHashValueShares(encodedHashValueShares);
        secretHashValue=convertSecretHashValueFromBitSetToString(encodedSecretHashValue);

        return secretHashValue;
    }

    private BitSet convertQGramFromStringToBitSet(String qgram){
        BitSet encodedQGram=null;
        encodedQGram= bijectionQGramsAndBits.encodeQGramAsBitSet(qgram);
        if(encodedQGram==null){
            encodedQGram=new FixedLengthBitSet(0);
        }
        return encodedQGram;
    }

    private String convertSecretHashValueFromBitSetToString(BitSet encodedSecretHashValue){
        String secretHashValue=bijectionHexAndBits.decode(encodedSecretHashValue);
        return secretHashValue;
    }

    private BitSet evaluateHashValueShare(HostDto host, BitSet encodedQGram){
        HashValueEvaluator evaluator = new HashValueEvaluator(host,encodedQGram);
        BitSet encodedHashValueShare = evaluator.evaluateEachBit();
        return encodedHashValueShare;
    }

    private BitSet xorHashValueShares(List<BitSet> previouslyEvaluatedEncodedHashValueShares){
        BitSet encodedHashValueSecret = BitSetShareMergeUtil.mergeUsingXOR(previouslyEvaluatedEncodedHashValueShares);
        return encodedHashValueSecret;
    }

    private BitSet computeEncodedHashValueShareOfOwnHashFunction(String qgram){
        String hashValue=ownHashingAlgorithm.h(qgram);
        BitSet encodedHashValue=bijectionHexAndBits.encode(hashValue);
        return encodedHashValue;
    }
}
