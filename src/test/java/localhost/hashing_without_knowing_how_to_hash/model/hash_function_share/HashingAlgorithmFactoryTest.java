package test.java.localhost.hashing_without_knowing_how_to_hash.model.hash_function_share;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.security.HashValueShortener;
import localhost.hashing_without_knowing_how_to_hash.security.Sha256;
import mockdata.SecretKeyTestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashingAlgorithmFactoryTest {

    @Test
    public void testInstantiateHashingAlgorithmWithRandomKeyGeneratesTwoInstancesWithDifferentKeys(){
        //given
        HashingAlgorithmType type=HashingAlgorithmType.SHA256;

        //when
        HashingAlgorithm firstHashingAlgo=HashingAlgorithmFactory.instantiateHashingAlgorithmWithRandomKey(type);
        HashingAlgorithm secondHashingAlgo=HashingAlgorithmFactory.instantiateHashingAlgorithmWithRandomKey(type);

        //then
        assertNotEquals(firstHashingAlgo.getSecretKey().getKey(), secondHashingAlgo.getSecretKey().getKey());
    }

    @Test
    public void testInstantiateHashingAlgorithmWithRandomKeyInstantiatesChosenTypeSha256(){
        //given
        HashingAlgorithmType type=HashingAlgorithmType.SHA256;

        //when
        HashingAlgorithm shouldBeSha256=HashingAlgorithmFactory.instantiateHashingAlgorithmWithRandomKey(type);

        //then
        assertInstanceOf(Sha256.class, shouldBeSha256);
    }

    @Test
    public void testInstantiateHashingAlgorithmWithRandomKeyInstantiatesChosenTypeSha256Shortened12Bit(){
        //given
        HashingAlgorithmType type=HashingAlgorithmType.SHA256_SHORTENED_12BIT;

        //when
        HashingAlgorithm shouldBeHashValueShortener=HashingAlgorithmFactory.instantiateHashingAlgorithmWithRandomKey(type);

        //then
        assertInstanceOf(HashValueShortener.class, shouldBeHashValueShortener);
    }

    @Test
    public void testInstantiateHashingAlgorithmWithKeyShouldGivenSecretKeyToHashingAlgorithm(){
        //given
        HashingAlgorithmType type=HashingAlgorithmType.SHA256;
        HashFunctionSecretKeyDto expectedSecretKey=new HashFunctionSecretKeyDto(SecretKeyTestData.getKeyHex(), type);

        //when
        HashingAlgorithm hashingAlgorithm=HashingAlgorithmFactory.instantiateHashingAlgorithmWithKey(type, expectedSecretKey);
        HashFunctionSecretKeyDto actualSecretKey=hashingAlgorithm.getSecretKey();

        //then
        assertEquals(expectedSecretKey.getKey(), actualSecretKey.getKey());
    }
}