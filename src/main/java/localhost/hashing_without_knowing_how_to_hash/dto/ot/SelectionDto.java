package localhost.hashing_without_knowing_how_to_hash.dto.ot;

import lombok.Getter;

import java.math.BigInteger;
import java.util.Random;

/**
 * The receiver needs to make a selection which of the two secrets to choose
 * Then the receiver also needs to prepare a random message by its own,
 * which takes part in the calculation of the VValueDto, that is created later
 */
public class SelectionDto {

    @Getter
    private boolean b;
    @Getter
    private BigInteger k;


    public SelectionDto(boolean b){
        this.b=b;
        k=generateRandomK(1024);
    }

    public BigInteger generateRandomK(int len){
        Random random=new Random();
        return new BigInteger(len, random);
    }

}
