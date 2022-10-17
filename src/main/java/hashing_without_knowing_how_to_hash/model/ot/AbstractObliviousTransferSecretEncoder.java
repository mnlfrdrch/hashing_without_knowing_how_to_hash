package main.java.hashing_without_knowing_how_to_hash.model.ot;

import lombok.Getter;

import java.math.BigInteger;

/**
 * The RSA oblivious works using integer numbers.
 * AbstractObliviousTransferSecretEncoder is a template
 * to encode a received secret of type T into integer representation.
 * One of these representations can then be transferred obliviously
 */
public abstract class AbstractObliviousTransferSecretEncoder<T> {

    @Getter
    private T secretToEncode;

    public AbstractObliviousTransferSecretEncoder(T secretToEncode){
        this.secretToEncode=secretToEncode;
    }

    abstract public BigInteger encode();
}
