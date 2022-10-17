package test.java.localhost.hashing_without_knowing_how_to_hash.model;

import localhost.hashing_without_knowing_how_to_hash.constants.CharacterSets;
import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.Domain;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BijectionQGramsAndBitsTest {

    @Test
    public void testPositionOfNGramsInSortedListForUnigram(){
        //given
        String UNIGRAM_NUM_SIGN="#";
        int expectedNumSignPosition=0;
        String UNIGRAM_ONE="1";
        int expectedOnePosition=1;
        String UNIGRAM_SEVEN="7";
        int expectedSevenPosition=2;
        String UNIGRAM_A="A";
        int expectedAPosition=3;
        String UNIGRAM_B="B";
        int expectedBPosition=4;
        String UNIGRAM_Z="Z";
        int expectedZPosition=5;

        List<String> unorderedUniGrams=List.of(UNIGRAM_SEVEN, UNIGRAM_A, UNIGRAM_NUM_SIGN, UNIGRAM_ONE, UNIGRAM_Z, UNIGRAM_B);
        Domain domain=mock(Domain.class);
        when(domain.getAllQGrams()).thenReturn(unorderedUniGrams);

        //when
        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);
        int actualNumSignPosition=bijection.positionOfNGramInSortedOrder(UNIGRAM_NUM_SIGN);
        int actualOnePosition=bijection.positionOfNGramInSortedOrder(UNIGRAM_ONE);
        int actualSevenPosition=bijection.positionOfNGramInSortedOrder(UNIGRAM_SEVEN);
        int actualAPosition=bijection.positionOfNGramInSortedOrder(UNIGRAM_A);
        int actualBPosition=bijection.positionOfNGramInSortedOrder(UNIGRAM_B);
        int actualZPosition=bijection.positionOfNGramInSortedOrder(UNIGRAM_Z);

        //then
        assertEquals(expectedNumSignPosition, actualNumSignPosition);
        assertEquals(expectedOnePosition, actualOnePosition);
        assertEquals(expectedSevenPosition, actualSevenPosition);
        assertEquals(expectedAPosition, actualAPosition);
        assertEquals(expectedBPosition, actualBPosition);
        assertEquals(expectedZPosition, actualZPosition);
    }

    @Test
    public void testPositionOfNGramsInSortedListForBigrams(){
        //given
        String BIGRAM_NUM_SIGN_Z="#Z";
        int expectedNumSignZPosition=0;
        String BIGRAM_ONE_U="1U";
        int expected1UPosition=1;
        String BIGRAM_A_NINE="A9";
        int expectedA9Position=2;

        List<String> unorderedUniGrams=List.of(BIGRAM_ONE_U,BIGRAM_A_NINE, BIGRAM_NUM_SIGN_Z);
        Domain domain=mock(Domain.class);
        when(domain.getAllQGrams()).thenReturn(unorderedUniGrams);

        //when
        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);
        int actualNumSignZPosition=bijection.positionOfNGramInSortedOrder(BIGRAM_NUM_SIGN_Z);
        int actual1UPosition=bijection.positionOfNGramInSortedOrder(BIGRAM_ONE_U);
        int actualA9Position=bijection.positionOfNGramInSortedOrder(BIGRAM_A_NINE);

        //then
        assertEquals(expectedNumSignZPosition, actualNumSignZPosition);
        assertEquals(expected1UPosition, actual1UPosition);
        assertEquals(expectedA9Position, actualA9Position);
    }

    @Test
    public void testConvertDecimalToBinaryRepresentation(){
        //given
        int ZERO=0;
        int TWO=2;
        String expected0Representation="0";
        String expected2Representation="10";
        Domain domain=new Domain(1, CharacterSets.EMPTY_ELEMENT);
        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);

        //when
        String actual0Representation=bijection.convertDecimalToBinaryRepresentation(ZERO);
        String actual2Representation=bijection.convertDecimalToBinaryRepresentation(TWO);

        //then
        assertEquals(expected0Representation, actual0Representation);
        assertEquals(expected2Representation, actual2Representation);
    }

    @Test
    public void testRequiredNumOfBitsFor100GramsShouldBe7(){
        //given
        Domain domain=new Domain(2, CharacterSets.NUMERICAL);
        int expectedBitSetLength=7; // ceil(log_2(#ngrams)) = ceil(log_2(10^2)) = ceil(log_2(100)) = ceil(6.64...) = 7

        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);

        //when
        int actualBitSetLength=bijection.computeRequiredBitSetLength();

        //then
        assertEquals(expectedBitSetLength, actualBitSetLength);
    }

    @Test
    public void testConversionToBitSetWithLeadingZeros(){
        //given
        Domain domain=new Domain(2, CharacterSets.NUMERICAL);
        String fifthNGram="101";
        int expectedBitSetLength=7;

        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);

        //when
        BitSet encodedNGram=bijection.convertBinarySymbolsToBitSetEncoding(fifthNGram);
        int actualBitSetLength=encodedNGram.length();

        //then
        assertEquals(expectedBitSetLength, actualBitSetLength);

            //four leading zeros
        assertFalse(encodedNGram.get(0));
        assertFalse(encodedNGram.get(1));
        assertFalse(encodedNGram.get(2));
        assertFalse(encodedNGram.get(3));
            //101
        assertTrue(encodedNGram.get(4));
        assertFalse(encodedNGram.get(5));
        assertTrue(encodedNGram.get(6));
    }

    @Test
    public void testEncodeNGramsAsBitSet(){
        //given
        Domain domain=new Domain(2, CharacterSets.NUMERICAL);
        String NGRAM="01";

        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);

        //when
        BitSet encodedNGram=bijection.encodeQGramAsBitSet(NGRAM);

        //then
            //encodedNGram should be 0000001
        assertFalse(encodedNGram.get(0));
        assertFalse(encodedNGram.get(1));
        assertFalse(encodedNGram.get(2));
        assertFalse(encodedNGram.get(3));
        assertFalse(encodedNGram.get(4));
        assertFalse(encodedNGram.get(5));
        assertTrue(encodedNGram.get(6));
    }

    @Test
    public void testEncodeBitSetAsNGram(){
        //given
        Domain domain=new Domain(2, CharacterSets.NUMERICAL);
        boolean ZERO=false;
        boolean ONE=true;

        BitSet encodedNGram=new FixedLengthBitSet(7);
        encodedNGram.set(0,ZERO);
        encodedNGram.set(1,ZERO);
        encodedNGram.set(2,ZERO);
        encodedNGram.set(3,ZERO);
        encodedNGram.set(4,ZERO);
        encodedNGram.set(5,ZERO);
        encodedNGram.set(6,ONE);

        String expectedNGram="01";

        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);

        //when
        String actualNGram=bijection.encodeBitSetAsNGram(encodedNGram);

        //then
        assertEquals(expectedNGram, actualNGram);
    }

}