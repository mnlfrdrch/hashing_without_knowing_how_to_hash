package localhost.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.BitSet;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class HashFunctionCacheTest {

    private HashFunctionCache hashFunctionCache;

    @BeforeEach
    public void setUp(){
        hashFunctionCache=HashFunctionCache.getInstance();
    }

    @AfterEach
    public void tearDown(){
        ReflectionTestUtils.setField(hashFunctionCache, "qGramToHashValue", new HashMap<>());
    }

    @Test
    public void testInstanceIsGenerated() {
        //given
        HashFunctionCache hashFunctionCache=null;

        //when
        hashFunctionCache=HashFunctionCache.getInstance();

        //then
        assertNotNull(hashFunctionCache);
    }

    @Test
    public void testOnlySingleInstanceIsGenerated(){
        //given
        HashFunctionCache firstReference=null;
        HashFunctionCache secondReference=null;

        //when
        firstReference=HashFunctionCache.getInstance();
        secondReference=HashFunctionCache.getInstance();

        //then
        assertEquals(firstReference,secondReference);
    }


    @Test
    void testIsInCacheShouldBeFalse() {
        //given
        HashFunctionCache hashFunctionCache=HashFunctionCache.getInstance();
        String keyNotInCache="keyWhichIsNotInCache";

        //when
        boolean isKeyInCache=hashFunctionCache.isQGramHashValuePairInCache(keyNotInCache);

        //then
        assertFalse(isKeyInCache);
    }


    @Test
    void testIsQGramHashValuePairInCacheShouldBeTrueIfRequestedKVPairWasCachedPreviously() {
        //given
        HashFunctionCache hashFunctionCache=HashFunctionCache.getInstance();
        String keyInCache="a";
        String valueInCache="b";
        hashFunctionCache.cacheQGramHashValuePair(keyInCache, valueInCache);

        //when
        boolean isKeyInCache=hashFunctionCache.isQGramHashValuePairInCache(keyInCache);

        //then
        assertTrue(isKeyInCache);
    }

    @Test
    void testRequestSecretHashValueForQGramShouldGetPreviouslyCachedValueForExistingKey() {
        //given
        HashFunctionCache hashFunctionCache=HashFunctionCache.getInstance();
        String keyInCache="a";
        String valueInCache="b";
        hashFunctionCache.cacheQGramHashValuePair(keyInCache, valueInCache);

        //when
        String receivedValue=hashFunctionCache.requestSecretHashValueForQGram(keyInCache);

        //then
        assertEquals(valueInCache,receivedValue);
    }


    @Test
    void testRequestSecretHashValueForQGramShouldGetNullForNonExistingKey() {
        //given
        HashFunctionCache hashFunctionCache=HashFunctionCache.getInstance();
        String keyNotInCache="keyNotInCache";

        //when
        String receivedValue=hashFunctionCache.requestSecretHashValueForQGram(keyNotInCache);

        //then
        assertNull(receivedValue);
    }
}