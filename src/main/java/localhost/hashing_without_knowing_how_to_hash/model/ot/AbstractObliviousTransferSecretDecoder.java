package localhost.hashing_without_knowing_how_to_hash.model.ot;

import lombok.Getter;

import java.math.BigInteger;

/**
 * The RSA oblivious works using integer numbers.
 * AbstractObliviousTransferSecretDecoder is a template
 * to decode a received secret as integer back into secret of required type T
 */
public abstract class AbstractObliviousTransferSecretDecoder<T> {

    @Getter
    private BigInteger secretToDecode;

    public AbstractObliviousTransferSecretDecoder(BigInteger secretToDecode){
        this.secretToDecode=secretToDecode;
    }

    public abstract T decode();
}
