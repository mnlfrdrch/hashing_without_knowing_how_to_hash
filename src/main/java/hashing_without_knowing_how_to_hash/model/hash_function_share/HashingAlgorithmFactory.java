package main.java.hashing_without_knowing_how_to_hash.model.hash_function_share;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.security.HashValueShortener;
import localhost.hashing_without_knowing_how_to_hash.security.Sha256;
import lombok.experimental.UtilityClass;

/**
 * Manages dynamic instantiation of hashing algorithms
 * Hashing algorithms can be selected by HashingAlgorithmType
 */
@UtilityClass
public class HashingAlgorithmFactory {


    /**
     * Only instantiates hashing algorithms, that randomly generate their secret keys by themselves
     * @param algoType e.g. SHA256
     * @return the selected HashingAlgorithm instance
     */
    public HashingAlgorithm instantiateHashingAlgorithmWithRandomKey(HashingAlgorithmType algoType){
        HashingAlgorithm instantiatedHashingAlgorithm=null;
        switch (algoType){
            case SHA256 -> {
                instantiatedHashingAlgorithm=new Sha256();
                break;
            }
            case SHA256_SHORTENED_12BIT -> {
                instantiatedHashingAlgorithm=new HashValueShortener(3, new Sha256());
                break;
            }
        }
        return instantiatedHashingAlgorithm;
    }

    /**
     * Only instantiates hashing algorithms, that require a fixed and known secret key
     * @param algoType e.g. SHA256
     * @return the selected HashingAlgorithm instance
     */
    public HashingAlgorithm instantiateHashingAlgorithmWithKey(HashingAlgorithmType algoType, HashFunctionSecretKeyDto secretKey){
        HashingAlgorithm instantiatedHashingAlgorithm=null;
        switch (algoType){
            case SHA256 -> {
                instantiatedHashingAlgorithm=new Sha256(secretKey);
                break;
            }
            case SHA256_SHORTENED_12BIT -> {
                instantiatedHashingAlgorithm=new HashValueShortener(3, new Sha256(secretKey));
                break;
            }
        }
        return instantiatedHashingAlgorithm;
    }
}
