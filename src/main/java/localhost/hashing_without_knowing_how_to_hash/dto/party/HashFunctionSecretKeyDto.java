package localhost.hashing_without_knowing_how_to_hash.dto.party;

import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmFactory;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmType;
import localhost.hashing_without_knowing_how_to_hash.util.HexUtil;
import lombok.Getter;

/**
 * Wraps the secret key used in the hashing algorithms to be calculated
 * Implements validity check functionalities as well
 */
public class HashFunctionSecretKeyDto {
    @Getter
    private String key;

    /**
     * @param hexKey key of hashing algoritm as hexadecimal character key
     * @param algoType expected kind of hashing algorithm
     * @throws IllegalArgumentException is thrown if key is invalid
     */
    public HashFunctionSecretKeyDto(String hexKey, HashingAlgorithmType algoType) throws IllegalArgumentException{
        int requiredKeyLength=HashingAlgorithmFactory.instantiateHashingAlgorithmWithRandomKey(algoType).getSecretKeyLength();
        if(!isHashFunctionKey(hexKey, requiredKeyLength)){
            throw new IllegalArgumentException(hexKey + " is no valid key for hash function");
        }
        key=hexKey;
    }

    private boolean isHashFunctionKey(String hexKey, int requiredKeyCharLength){
        boolean isCorrectLength = hexKey.length() == requiredKeyCharLength;
        boolean isHex=HexUtil.isHexaDecimal(hexKey);
        return isCorrectLength && isHex;
    }
}
