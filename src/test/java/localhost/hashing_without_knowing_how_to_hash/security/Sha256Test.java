package test.java.localhost.hashing_without_knowing_how_to_hash.security;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmFactory;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmType;
import mockdata.SecretKeyTestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Sha256Test {

    @Test
    public void testConstructorDoesNotThrowException(){
        assertDoesNotThrow(()->{new Sha256();});
    }

    @Test
    public void testFunctionDoesNotThrowException(){
        HashingAlgorithm algorithm=new Sha256();
        String x="ABC";

        assertDoesNotThrow(()->{String y=algorithm.h(x);
            System.out.println("h("+x+")="+y);});
    }

    @Test
    public void testFunctionGenerates256BitHasValue(){
        HashingAlgorithm algorithm=new Sha256();
        String x="ABC";

        //when
        String y=algorithm.h(x);
        int actualBits=y.length()*4;

        //then
        assertEquals(256, actualBits);
    }

    @Test
    public void test(){
        //given
        HashFunctionSecretKeyDto secretKey= SecretKeyTestData.getKeyWrapped();
        HashingAlgorithm hashingAlgorithm= HashingAlgorithmFactory.instantiateHashingAlgorithmWithKey(HashingAlgorithmType.SHA256, secretKey);
        String x="ABC";
        String expectedY="a7b5aa889b4f44098ff2dbeab27492fbab7ac2181d9d332001d4ea8132ce6478";

        //when
        String actualY=hashingAlgorithm.h(x);

        //then
        assertEquals(expectedY, actualY);
    }
}