package main.java.hashing_without_knowing_how_to_hash.dto.ot;

import lombok.Getter;

import java.math.BigInteger;

/**
 * In the beginning, the sender of the oblivious transfer holds both of ist unencrypted (plaintext) secrets
 * However, they are already converted to a corresponding integer representation to be later used with RSA encryption
 * The sender must not transfer this object to the receiver
 * However sender and receiver will perform an oblivious transfer on the secrets of this object
 */
public class PlaintextSecretPairDto {

    @Getter
    private BigInteger m0;
    @Getter
    private BigInteger m1;

    public PlaintextSecretPairDto(BigInteger m0, BigInteger m1){
        this.m0 = m0;
        this.m1 = m1;
    }
}
