package localhost.hashing_without_knowing_how_to_hash.security;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import mockdata.HashingAlgorithmMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashValueShortenerTest {

    HashValueShortener hashValueShortener;

    @BeforeEach
    public void setUp() {
        hashValueShortener = new HashValueShortener(3, HashingAlgorithmMockData.mockConstantFunction());
    }

    @Test
    public void testHShouldOnlyKeepFirst3DigitsOfOriginalHashValue() {
        //given
        String x = "1";
        String expectedHashValue = "123";

        //when
        String actualHashValue = hashValueShortener.h(x);

        //then
        assertEquals(expectedHashValue, actualHashValue);
    }

    @Test
    public void testGetHashValueLengthShouldBeAsSetInConstructor() {
        //given
        int expectedHashValueLength = 3;

        //when
        int actualHashValueLength = hashValueShortener.getHashValueLength();

        //then
        assertEquals(expectedHashValueLength, actualHashValueLength);
    }

    @Test
    public void testGetSecretKeyShouldPassSecretKeyOfUnderlyingHashFunction() {
        //given
        HashFunctionSecretKeyDto expectedSecretKey = HashingAlgorithmMockData.mockConstantFunction().getSecretKey();

        //when
        HashFunctionSecretKeyDto actualSecretKey = hashValueShortener.getSecretKey();

        //then
        assertEquals(expectedSecretKey.getKey(), actualSecretKey.getKey());
    }

    @Test
    public void testGetSecretKeyLengthShouldBeSameLengthAsFromUnderlyingHashFunction() {
        //given
        int expectedSecretKeyLength = HashingAlgorithmMockData.mockConstantFunction().getSecretKeyLength();

        //when
        int actualSecretKeyLength = hashValueShortener.getSecretKeyLength();

        //then
        assertEquals(expectedSecretKeyLength, actualSecretKeyLength);
    }
}