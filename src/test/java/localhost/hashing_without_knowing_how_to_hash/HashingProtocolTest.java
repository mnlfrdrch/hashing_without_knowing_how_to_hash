package localhost.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.controller.CircuitController;
import localhost.hashing_without_knowing_how_to_hash.controller.ObliviousTransferController;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmFactory;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmType;
import mockdata.FormulaTestData;
import mockdata.SecretKeyTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HashingProtocolTest {

    @LocalServerPort
    int PORT;

    HashingProtocol hashingProtocol;

    @Autowired
    CircuitController circuitController;

    @Autowired
    ObliviousTransferController obliviousTransferController;

    private HostDto getHost(){
        return new HostDto("localhost", String.valueOf(PORT));
    }

    @BeforeEach
    public void setUp(){
        //own hash function secret
        HashFunctionSecretKeyDto secretKey= SecretKeyTestData.getKeyWrapped();
        HashingAlgorithm hashingAlgorithm=HashingAlgorithmFactory.instantiateHashingAlgorithmWithKey(HashingAlgorithmType.SHA256_SHORTENED_12BIT, secretKey);
        hashingProtocol=new HashingProtocol(hashingAlgorithm, List.of(getHost()));

        //other host's hash function secret
        FormulaContainerDto formulaContainer=new FormulaContainerDto(FormulaTestData.get12BitAAndBFunction());
        ReflectionTestUtils.setField(FormulaCache.getInstance(), "formulaContainerDto", formulaContainer);
    }

    @Test
    public void testH(){
        //given
        String x="00";
        String expectedHashValue="2fe"; //h_own(x) xor h_host(x) = 2ef xor 000 = 2ef

        //when
        String actualHashValue=hashingProtocol.h(x);

        //then
        assertEquals(expectedHashValue, actualHashValue);
    }

    @Test
    public void testIsHashableShouldBeTrueForHashableString(){
        //given
        String hashableString="00";

        //when
        boolean shouldBeHashable=hashingProtocol.isHashable(hashableString);

        //then
        assertTrue(shouldBeHashable);
    }

    @Test
    public void testIsHashableShouldBeFalseForNonHashableString(){
        //given
        String nonHashableString="ABDCEF";

        //when
        boolean shouldNotBeHashable=hashingProtocol.isHashable(nonHashableString);

        //then
        assertFalse(shouldNotBeHashable);
    }

}