package main.java.hashing_without_knowing_how_to_hash.dto.ot;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * Hold the encrypted secret pairs which sender transfers to receiver
 * Is final step of RSA oblivious transfer variant
 * Receiver is able to encrypt exactly the selected one of the secrets
 */
@NoArgsConstructor
public class EncryptedSecretPairDto {

    @Getter
    private BigInteger encM0;
    @Getter
    private BigInteger encM1;

    public EncryptedSecretPairDto(BigInteger encM0, BigInteger encM1){
        this.encM0 = encM0;
        this.encM1 = encM1;
    }
    public EncryptedSecretPairDto(PlaintextSecretPairDto plaintextSecretPairDto, KValuePairDto kValuePairDto){
        encM0 =plaintextSecretPairDto.getM0().add(kValuePairDto.getK0());
        encM1 =plaintextSecretPairDto.getM1().add(kValuePairDto.getK1());
    }

}
