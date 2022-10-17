package main.java.hashing_without_knowing_how_to_hash.security;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;

/**
 * Shortens the hash values of any cryptographic HashingAlgorithm to 12Bit
 * In hexadecimal 12 bit can be represented as a string of lenght 3
 */
public class HashValueShortener implements HashingAlgorithm {

    private int numOfHexDigits;
    private HashingAlgorithm hashingAlgorithm;

    public HashValueShortener(int numOfHexDigits, HashingAlgorithm hashingAlgorithm){
        this.numOfHexDigits=Math.max(numOfHexDigits, 1);
        this.hashingAlgorithm=hashingAlgorithm;
    }

    @Override
    public String h(String x){
        String fullHashValue=hashingAlgorithm.h(x);
        String shortenedHashValue=fullHashValue.substring(0, numOfHexDigits);
        return shortenedHashValue;
    }

    private String getSecretKeyHex(){
        return hashingAlgorithm.toString();
    }

    @Override
    public HashFunctionSecretKeyDto getSecretKey(){
        return hashingAlgorithm.getSecretKey();
    }

    @Override
    public int getHashValueLength(){
        return numOfHexDigits;
    }

    @Override
    public int getSecretKeyLength(){
        return hashingAlgorithm.getSecretKeyLength();
    }
    @Override
    public String toString(){
        return getSecretKeyHex();
    }
}
