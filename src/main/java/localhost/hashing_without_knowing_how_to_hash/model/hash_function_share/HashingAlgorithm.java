package localhost.hashing_without_knowing_how_to_hash.model.hash_function_share;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;

public interface HashingAlgorithm {

    /**
     * The actual hash function of the hashing algorithm
     * @param x
     * @return h(x)
     */
    String h(String x);

    /**
     * Gets the secret key used by the keyed hash function
     * @return instance of HashFunctionSecretKeyDto, in which the actual key is wrapped in
     */
    HashFunctionSecretKeyDto getSecretKey();

    /**
     * Gets the length of each hash value in bits
     * @return length of hash value in bit
     */
    int getHashValueLength();

    /**
     * Gets the length of the expected or applied secret key in bit
     * @return lenght of key in bit
     */
    int getSecretKeyLength();
}
