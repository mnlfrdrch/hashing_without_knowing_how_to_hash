package localhost.hashing_without_knowing_how_to_hash.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiteralNameMathUtilTest {


    @Test
    public void testPotencyTermOfExpIs0(){
        //Given
        int factor=1;
        int basis=2;
        int exp=0;

        //when
        int valShouldBe1= LiteralNameMathUtil.potencyTerm(factor,basis,exp);

        //then
        assertEquals(1,valShouldBe1);
    }

    @Test
    public void testPotencyTermOfExpIs1(){
        //Given
        int factor=1;
        int basis=2;
        int exp=1;

        //when
        int valShouldBe2= LiteralNameMathUtil.potencyTerm(factor,basis,exp);

        //then
        assertEquals(2,valShouldBe2);
    }

    @Test
    public void testPotencyTermOfExpIsMinus1(){
        //Given
        int factor=1;
        int basis=2;
        int exp=-1;

        //when
        int valShouldBe2= LiteralNameMathUtil.potencyTerm(factor,basis,exp);

        //then
        assertEquals(2,valShouldBe2);
    }

    @Test
    public void testPotencyTermOfFactor0(){
        //given
        int factor=0;
        int basis=2;
        int exp=0;

        //when
        int valShouldBe0= LiteralNameMathUtil.potencyTerm(factor, basis,exp);

        //then
        assertEquals(0, valShouldBe0);
    }

    @Test
    public void testPotencyTermOfFactorMinus1(){
        //given
        int factor=-1;
        int basis=2;
        int exp=0;

        //when
        int valShouldBe1= LiteralNameMathUtil.potencyTerm(factor, basis,exp);

        //then
        assertEquals(1, valShouldBe1);
    }

    @Test
    public void testPotencyTermOfBasis0(){
        //given
        int factor=1;
        int basis=0;
        int exp=1;

        //when
        int valShouldBe0= LiteralNameMathUtil.potencyTerm(factor, basis,exp);

        //then
        assertEquals(0, valShouldBe0);
    }


    @Test
    public void testPotencyTermUndefinedInput0ToPowerOf0(){
        //given
        int factor=1;
        int basis=0;
        int exp=0;

        //when
        int valShouldBe1= LiteralNameMathUtil.potencyTerm(factor, basis,exp);

        //then
        assertEquals(1, valShouldBe1);
    }

    @Test
    public void testPotencyTermOfBasis10(){
        //given
        int factor=1;
        int basis=10;
        int exp=1;

        //when
        int valShouldBe10= LiteralNameMathUtil.potencyTerm(factor, basis,exp);

        //then
        assertEquals(10, valShouldBe10);
    }

    @Test
    public void testPotencyTermOfBasisMinus10(){
        //given
        int factor=1;
        int basis=-10;
        int exp=1;

        //when
        int valShouldBe10= LiteralNameMathUtil.potencyTerm(factor, basis,exp);

        //then
        assertEquals(10, valShouldBe10);
    }

    @Test
    public void testSumOfPotencyTermsForMaxExponent0(){
        //given
        int exp=0;
        int basis=10;

        //when
        int valShouldBe9= LiteralNameMathUtil.sumOfPotencyTerms(basis,exp); //9*(10^0)

        //then
        assertEquals(9, valShouldBe9);
    }

    @Test
    public void testSumOfPotencyTermsForMaxExponent1(){
        //given
        int exp=1;
        int basis=10;

        //when
        int valShouldBe99= LiteralNameMathUtil.sumOfPotencyTerms(basis,exp); // 9*(10^1) + 9*(10^0)

        //then
        assertEquals(99, valShouldBe99);
    }

    @Test
    public void testSumOfPotencyTermsForNegativeMaxExponent(){
        //given
        int exp=-1;
        int basis=10;

        //when
        int valShouldBe0= LiteralNameMathUtil.sumOfPotencyTerms(basis,exp); // no for-loop call

        //then
        assertEquals(0, valShouldBe0);
    }

    @Test
    public void testSumOfPotencyTermsForBasis2(){
        //given
        int exp=1;
        int basis=2;

        //when
        int valShouldBe3= LiteralNameMathUtil.sumOfPotencyTerms(basis,exp); //1*(2^1) + 1*(2^0)

        //then
        assertEquals(3, valShouldBe3);
    }

    @Test
    public void testPositionalCountForNoLiterals(){
        //given
        int literalCount=0;
        int basis=10;

        //when
        int shouldBeOnePosition= LiteralNameMathUtil.positionalCount(literalCount,basis);

        //then
        assertEquals(1, shouldBeOnePosition);
    }

    @Test
    public void testPositionalCountForOneLiteral(){
        //given
        int literalCount=1;
        int basis=10;

        //when
        int shouldBeOnePosition= LiteralNameMathUtil.positionalCount(literalCount,basis);

        //then
        assertEquals(1, shouldBeOnePosition);
    }

    @Test
    public void testPositionalCountFor9LiteralsAndBasis10ShouldBe1(){
        //given
        int literalCount=9;
        int basis=10;

        //when
        int shouldBeOnePosition= LiteralNameMathUtil.positionalCount(literalCount,basis);

        //then
        assertEquals(1, shouldBeOnePosition);
    }

    @Test
    public void testPositionalCountFor10LiteralsAndBasis10ShouldBe2(){
        //given
        int literalCount=10;
        int basis=10;

        //when
        int shouldBeTwoPositions= LiteralNameMathUtil.positionalCount(literalCount,basis);

        //then
        assertEquals(2, shouldBeTwoPositions);
    }

    @Test
    public void testPositionalCountForNegative10LiteralsAndBasis10ShouldBe2(){
        //given
        int literalCount=-10;
        int basis=10;

        //when
        int shouldBeTwoPositions= LiteralNameMathUtil.positionalCount(literalCount,basis);

        //then
        assertEquals(2, shouldBeTwoPositions);
    }

    @Test
    public void testPositionalCountFor676LiteralsAndBasis26ShouldBe3(){
        //given
        int literalCount=676;
        int basis=26;

        //when
        int shouldBeThreePositions= LiteralNameMathUtil.positionalCount(literalCount,basis);

        //then
        assertEquals(3, shouldBeThreePositions);
    }

    @Test
    public void testPositionalCountFor10LiteralsAndBasisNegative10ShouldBe2(){
        //given
        int literalCount=10;
        int basis=-10;

        //when
        int shouldBeTwoPositions= LiteralNameMathUtil.positionalCount(literalCount,basis);

        //then
        assertEquals(2, shouldBeTwoPositions);
    }

    @Test
    public void testFittingFactorForAverageDecimalCase(){
        //given
        int numberToBeApproximated=23;
        int basis=10;
        int exp=1;

        //when
        int factorShouldBe2= LiteralNameMathUtil.calculateFittingFactor(numberToBeApproximated, basis, exp);

        //then
        assertEquals(2, factorShouldBe2);
    }

    @Test
    public void testFittingFactorForNegativeNumShouldBeEquivalentToAverageDecimalCase(){
        //given
        int numberToBeApproximated=-23;
        int basis=10;
        int exp=1;

        //when
        // no factor >= 0 that could fulfill the condition
        int factorShouldBe0= LiteralNameMathUtil.calculateFittingFactor(numberToBeApproximated, basis, exp);

        //then
        assertEquals(0, factorShouldBe0);
    }

    @Test
    public void testFittingFactorForPotencyTermEqualsNumberToBeApproximated(){
        //given
        int numberToBeApproximated=10;
        int basis=10;
        int exp=1;

        //when
        int factorShouldBe1= LiteralNameMathUtil.calculateFittingFactor(numberToBeApproximated, basis, exp);

        //then
        assertEquals(1, factorShouldBe1);
    }

    @Test
    public void testFittingFactorIfNumToHighForPotencyTerm(){
        //given
        int numberToBeApproximated=100;
        int basis=10;
        int exp=1;

        //when
        //100 = 1 * 10^2, but for 10^1 best case is 10 * 10^1
        int factorShouldBe10= LiteralNameMathUtil.calculateFittingFactor(numberToBeApproximated, basis, exp);

        //then
        assertEquals(10, factorShouldBe10);
    }

    @Test
    public void testFittingFactorIfNumToLowForPotencyTerm(){
        //given
        int numberToBeApproximated=2;
        int basis=10;
        int exp=1;

        //when
        //2 = 2 * 10^0, but for 10^1 best approximation is 2 > 0 * 10^1
        int factorShouldBe0= LiteralNameMathUtil.calculateFittingFactor(numberToBeApproximated, basis, exp);

        //then
        assertEquals(0, factorShouldBe0);
    }

}