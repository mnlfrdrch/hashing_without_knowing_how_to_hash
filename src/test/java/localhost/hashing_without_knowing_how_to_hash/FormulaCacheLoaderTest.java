package test.java.localhost.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.constants.CharacterSets;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmFactory;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmType;
import mockdata.HashingAlgorithmMockData;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FormulaCacheLoaderTest {

    FormulaCacheLoader formulaCacheLoader;

    @BeforeEach
    public void setUp(){
        HashingAlgorithm hashingAlgorithm=HashingAlgorithmFactory.instantiateHashingAlgorithmWithRandomKey(HashingAlgorithmType.SHA256_SHORTENED_12BIT);
        formulaCacheLoader=new FormulaCacheLoader(hashingAlgorithm);
    }

    @AfterEach
    public void tearDown(){
        FormulaCache formulaCache=FormulaCache.getInstance();
        formulaCache.setFormulaContainerDto(null);
    }

    @Test
    @Timeout(5)
    public void testRunShouldGenerateFormulaAndLoadItInFormulaCache(){
        //given
        FormulaCache formulaCache=FormulaCache.getInstance();

        int maxSetSize=11;
        if(maxSetSize>ModelMetaData.CHARACTERS.size()){ // leads to a better runtime
            fail("For a improvement in runtime please choose a ModelMetaData.CHARACTERS set which is of size less or equals " + maxSetSize);
        }

        //when
        formulaCacheLoader.run();

        //then
        assertTrue(formulaCache.isFormulaContainerBuilt());
    }

}