package localhost.hashing_without_knowing_how_to_hash.dto.ot;

import lombok.Getter;

import java.math.BigInteger;

/**
 * The RSA private key of the RSA oblivious transfer is generated at the sender side
 * It must not be transferred to the receiver
 */
public class PrivateKeyRSADto {

    @Getter
    private BigInteger n;
    @Getter
    private BigInteger d;

    public PrivateKeyRSADto(BigInteger n, BigInteger d){
        this.d=d;
        this.n=n;
    }
}
