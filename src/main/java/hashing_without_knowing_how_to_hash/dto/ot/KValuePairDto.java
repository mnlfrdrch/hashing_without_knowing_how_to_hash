package main.java.hashing_without_knowing_how_to_hash.dto.ot;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * From previously received data the receiver of oblivious transfer computes two keys
 * But only one of the keys is actually legitimate, the other one is useless
 * This way the receiver is able to encrypt just the one selected secret with the corresponding key
 */
@NoArgsConstructor
public class KValuePairDto {

    private PrivateKeyRSADto privateKeyRSADto;
    private RandomMessagePairDto randomMessagePairDto;
    private VValueDto v;

    @Getter
    private BigInteger k0;
    @Getter
    private BigInteger k1;

    public KValuePairDto(PrivateKeyRSADto privateKeyRSADto, RandomMessagePairDto randomMessagePairDto, VValueDto v){
        this.privateKeyRSADto = privateKeyRSADto;
        this.randomMessagePairDto = randomMessagePairDto;
        this.v=v;
        k0 = computeK0();
        k1 = computeK1();
    }

    public BigInteger computeKx(BigInteger randomMessage){
        BigInteger difference=v.getV().subtract(randomMessage);
        BigInteger powerAndModulo=difference.modPow(privateKeyRSADto.getD(), privateKeyRSADto.getN());
        return powerAndModulo;
    }

    public BigInteger computeK0(){
        return computeKx(randomMessagePairDto.getX0());
    }

    public BigInteger computeK1(){
        return computeKx(randomMessagePairDto.getX1());
    }

}
