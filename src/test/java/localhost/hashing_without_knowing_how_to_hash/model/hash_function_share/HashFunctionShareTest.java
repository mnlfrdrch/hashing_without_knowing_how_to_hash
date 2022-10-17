package localhost.hashing_without_knowing_how_to_hash.model.hash_function_share;

import localhost.hashing_without_knowing_how_to_hash.constants.CharacterSets;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.model.BijectionQGramsAndBits;
import localhost.hashing_without_knowing_how_to_hash.util.HexUtil;
import mockdata.SecretKeyTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HashFunctionShareTest {

    private HashFunctionShare hashFunctionShare;
    private HashingAlgorithm hashingAlgorithm;

    @BeforeEach
    public void setUp(){
        Domain domain=new Domain(2, CharacterSets.NUMERICAL);
        HashingAlgorithmType hashingAlgorithmType=HashingAlgorithmType.SHA256_SHORTENED_12BIT;
        HashFunctionSecretKeyDto secretKeyDto=new HashFunctionSecretKeyDto(SecretKeyTestData.getKeyHex(), hashingAlgorithmType);
        hashingAlgorithm=HashingAlgorithmFactory.instantiateHashingAlgorithmWithKey(hashingAlgorithmType, secretKeyDto);
        hashFunctionShare=new HashFunctionShare(domain, hashingAlgorithm);
    }

    @Test
    public void testGetFunctionMappingShouldBeOfSameSizeAsDomain(){
        //given
        int sizeDomain=hashFunctionShare.getDomain().getAllQGrams().size();

        //when
        Map<BitSet, BitSet> functionMapping=hashFunctionShare.getFunctionMapping();

        //then
        assertEquals(sizeDomain, functionMapping.size());
    }

    @Test
    public void testGetFunctionMappingValuesShouldBeSameSizeAsHashValueLength(){
        //given
        int numBitsOfOneHexChar=4;
        int bitLengthOfHashValues=hashingAlgorithm.getHashValueLength()*numBitsOfOneHexChar;

        //when
        Map<BitSet, BitSet> functionMapping=hashFunctionShare.getFunctionMapping();
        int bitLengthOfFunctionMappingValues=functionMapping.values().iterator().next().length();

        //then
        assertEquals(bitLengthOfHashValues, bitLengthOfFunctionMappingValues);
    }

    @Test
    public void testGetFunctionMappingShouldContainExpectedHashValues(){
        //given
        //h(00)=2fe
        String qGram="00";
        String hashValue="2fe";
        BitSet encodedQGram=new BijectionQGramsAndBits(hashFunctionShare.getDomain()).encodeQGramAsBitSet(qGram);
        BitSet expectedHashValueEncoded= HexUtil.convertHexStringIntoBits(hashValue);

        //when
        Map<BitSet, BitSet> hashFunctionMap=hashFunctionShare.getFunctionMapping();
        BitSet actualHashValueEncoded=hashFunctionMap.get(encodedQGram);

        //then
        assertEquals(expectedHashValueEncoded, actualHashValueEncoded);
    }



}