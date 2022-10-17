package main.java.hashing_without_knowing_how_to_hash.dto.ot;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Random;

/**
 * In the second step of RSA oblivious transfer protocol, the sender transfers this object to the receiver
 * It contains two random messages
 */
@NoArgsConstructor
public class RandomMessagePairDto {

    @Getter
    private BigInteger x0;
    @Getter
    private BigInteger x1;

    public RandomMessagePairDto(int numOfBits){
        Random random=new Random();
        x0 =new BigInteger(numOfBits, random);
        x1 =new BigInteger(numOfBits, random);
    }

}
