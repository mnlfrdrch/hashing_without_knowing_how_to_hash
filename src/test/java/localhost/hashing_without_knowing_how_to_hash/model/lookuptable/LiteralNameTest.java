package test.java.localhost.hashing_without_knowing_how_to_hash.model.lookuptable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiteralNameTest {

    private LiteralNameAllocator literalNameAllocator;

    @Test
    public void testDigitToLetterOf0ShouldBeA() {
        literalNameAllocator =new LiteralNameAllocator(10);

        StringBuilder stringBuilder= literalNameAllocator.digitToLetter(0);
        String shouldBeA=stringBuilder.toString();

        assertEquals("A",shouldBeA);
    }

    @Test
    public void testDigitToLetterOf25ShouldBeZ() {
        literalNameAllocator =new LiteralNameAllocator(10);

        StringBuilder stringBuilder= literalNameAllocator.digitToLetter(25);
        String shouldBeZ=stringBuilder.toString();

        assertEquals("Z",shouldBeZ);
    }

    @Test
    public void testDigitToLetterOf27ShouldBeB() {
        literalNameAllocator =new LiteralNameAllocator(10);

        StringBuilder stringBuilder= literalNameAllocator.digitToLetter(27);
        String shouldBeB=stringBuilder.toString();

        assertEquals("B",shouldBeB);
    }

    @Test
    public void testDigitToLetterOfMinus1ShouldBeZ() {
        literalNameAllocator =new LiteralNameAllocator(10);

        StringBuilder stringBuilder= literalNameAllocator.digitToLetter(-1);
        String shouldBeB=stringBuilder.toString();

        assertEquals("B",shouldBeB);
    }

    @Test
    public void testGetLiteralNameWhenOnlyOneCharacterIsNecessaryForFirstLiteral(){
        //given
        int indexA=0;
        int literalCount=26;
        literalNameAllocator =new LiteralNameAllocator(literalCount);

        //when
        StringBuilder stringBuilder= literalNameAllocator.getLiteralName(indexA);
        String shouldBeA=stringBuilder.toString();

        //then
        assertEquals("A", shouldBeA);
    }

    @Test
    public void testGetLiteralNameWhenOnlyOneCharacterIsNecessaryForLastLiteral(){
        //given
        int indexZ=25;
        int literalCount=26;
        literalNameAllocator =new LiteralNameAllocator(literalCount);

        //when
        StringBuilder stringBuilder= literalNameAllocator.getLiteralName(indexZ);
        String shouldBeZ=stringBuilder.toString();

        //then
        assertEquals("Z", shouldBeZ);
    }

    @Test
    public void testGetLiteralNameFirstTimeSecondCharacterIsNecessary(){
        //given
        int indexAA=0;
        int literalCount=27;
        literalNameAllocator =new LiteralNameAllocator(literalCount);

        //when
        StringBuilder stringBuilder= literalNameAllocator.getLiteralName(indexAA);
        String shouldBeAA=stringBuilder.toString();

        //then
        assertEquals("AA", shouldBeAA);
    }

    @Test
    public void testGetLiteralNameSecondTimeSecondCharacterIsNecessary(){
        //given
        int indexAB=1;
        int literalCount=27;
        literalNameAllocator =new LiteralNameAllocator(literalCount);

        //when
        StringBuilder stringBuilder= literalNameAllocator.getLiteralName(indexAB);
        String shouldBeAB=stringBuilder.toString();

        //then
        assertEquals("AB", shouldBeAB);
    }

    @Test
    public void testGetLiteralNameLastTimeSecondCharacterIsNecessary(){
        //given
        int indexZZ=675; // 25 * 26^1 + 25 * 26^0 = 676
        int literalCount=676;
        literalNameAllocator =new LiteralNameAllocator(literalCount);

        //when
        StringBuilder stringBuilder= literalNameAllocator.getLiteralName(indexZZ);
        String shouldBeZZ=stringBuilder.toString();

        //then
        assertEquals("ZZ", shouldBeZZ);
    }
}