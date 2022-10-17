package test.java.localhost.hashing_without_knowing_how_to_hash.util;

import org.junit.jupiter.api.Test;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.PropositionalParser;

import static org.junit.jupiter.api.Assertions.*;


class GateTypeUtilTest {

    FormulaFactory factory=new FormulaFactory();
    PropositionalParser parser=new PropositionalParser(factory);

    @Test
    public void testIsAnd() throws Exception{
        //given
        Formula literal=parser.parse("A");
        Formula literalNegated=parser.parse("~A");
        Formula and=parser.parse("A & B");
        Formula nand=parser.parse("~(A & B)");
        Formula or=parser.parse("A | B");
        Formula nor=parser.parse("~(A | B)");

        //when
        boolean shouldBeFalseForLiteral= GateTypeUtil.isAnd(literal);
        boolean shouldBeFalseForNegatedLiteral= GateTypeUtil.isAnd(literalNegated);
        boolean shouldBeTrueForAnd= GateTypeUtil.isAnd(and);
        boolean shouldBeFalseForNand= GateTypeUtil.isAnd(nand);
        boolean shouldBeFalseForOr= GateTypeUtil.isAnd(or);
        boolean shouldBeFalseForNor= GateTypeUtil.isAnd(nor);

        //then
        assertFalse(shouldBeFalseForLiteral);
        assertFalse(shouldBeFalseForNegatedLiteral);
        assertTrue(shouldBeTrueForAnd);
        assertFalse(shouldBeFalseForNand);
        assertFalse(shouldBeFalseForOr);
        assertFalse(shouldBeFalseForNor);
    }

    @Test
    public void testIsNand() throws Exception{
        //given

        Formula literal=parser.parse("A");
        Formula literalNegated=parser.parse("~A");
        Formula and=parser.parse("A & B");
        Formula nand=parser.parse("~(A & B)");
        Formula or=parser.parse("A | B");
        Formula nor=parser.parse("~(A | B)");

        //when
        boolean shouldBeFalseForLiteral= GateTypeUtil.isNand(literal);
        boolean shouldBeFalseForNegatedLiteral= GateTypeUtil.isNand(literalNegated);
        boolean shouldBeFalseForAnd= GateTypeUtil.isNand(and);
        boolean shouldBeTrueForNand= GateTypeUtil.isNand(nand);
        boolean shouldBeFalseForOr= GateTypeUtil.isNand(or);
        boolean shouldBeFalseForNor= GateTypeUtil.isNand(nor);

        //then
        assertFalse(shouldBeFalseForLiteral);
        assertFalse(shouldBeFalseForNegatedLiteral);
        assertFalse(shouldBeFalseForAnd);
        assertTrue(shouldBeTrueForNand);
        assertFalse(shouldBeFalseForOr);
        assertFalse(shouldBeFalseForNor);
    }

    @Test
    public void testIsOr() throws Exception{
        //given
        Formula literal=parser.parse("A");
        Formula literalNegated=parser.parse("~A");
        Formula and=parser.parse("A & B");
        Formula nand=parser.parse("~(A & B)");
        Formula or=parser.parse("A | B");
        Formula nor=parser.parse("~(A | B)");

        //when
        boolean shouldBeFalseForLiteral= GateTypeUtil.isOr(literal);
        boolean shouldBeFalseForNegatedLiteral= GateTypeUtil.isOr(literalNegated);
        boolean shouldBeFalseForAnd= GateTypeUtil.isOr(and);
        boolean shouldBeFalseForNand= GateTypeUtil.isOr(nand);
        boolean shouldBeTrueForOr= GateTypeUtil.isOr(or);
        boolean shouldBeFalseForNor= GateTypeUtil.isOr(nor);

        //then
        assertFalse(shouldBeFalseForLiteral);
        assertFalse(shouldBeFalseForNegatedLiteral);
        assertFalse(shouldBeFalseForAnd);
        assertFalse(shouldBeFalseForNand);
        assertTrue(shouldBeTrueForOr);
        assertFalse(shouldBeFalseForNor);
    }

    @Test
    public void testIsNor() throws Exception{
        //given
        Formula literal=parser.parse("A");
        Formula literalNegated=parser.parse("~A");
        Formula and=parser.parse("A & B");
        Formula nand=parser.parse("~(A & B)");
        Formula or=parser.parse("A | B");
        Formula nor=parser.parse("~(A | B)");

        //when
        boolean shouldBeFalseForLiteral= GateTypeUtil.isNor(literal);
        boolean shouldBeFalseForNegatedLiteral= GateTypeUtil.isNor(literalNegated);
        boolean shouldBeFalseForAnd= GateTypeUtil.isNor(and);
        boolean shouldBeFalseForNand= GateTypeUtil.isNor(nand);
        boolean shouldBeFalseForOr= GateTypeUtil.isNor(or);
        boolean shouldBeTrueForNor= GateTypeUtil.isNor(nor);

        //then
        assertFalse(shouldBeFalseForLiteral);
        assertFalse(shouldBeFalseForNegatedLiteral);
        assertFalse(shouldBeFalseForAnd);
        assertFalse(shouldBeFalseForNand);
        assertFalse(shouldBeFalseForOr);
        assertTrue(shouldBeTrueForNor);
    }

    @Test
    public void getFormulaInsideNegation() throws Exception{
        Formula literal=parser.parse("A");
        Formula literalNegated=parser.parse("~A");
        Formula and=parser.parse("A & B");
        Formula nand=parser.parse("~(A & B)");
        Formula or=parser.parse("A | B");
        Formula nor=parser.parse("~(A | B)");

        //when
        Formula shouldBeLiteral= GateTypeUtil.getFormulaInsideNegation(literal);
        Formula shouldBeNegatedLiteral= GateTypeUtil.getFormulaInsideNegation(literalNegated);
        Formula shouldBeOriginalAnd= GateTypeUtil.getFormulaInsideNegation(and);
        Formula shouldBeExtractedAnd= GateTypeUtil.getFormulaInsideNegation(nand);
        Formula shouldBeOriginalOr= GateTypeUtil.getFormulaInsideNegation(or);
        Formula shouldBeExtractedOr= GateTypeUtil.getFormulaInsideNegation(nor);
        Formula shouldBeNull= GateTypeUtil.getFormulaInsideNegation(null);

        //then
        assertEquals(literal, shouldBeLiteral);
        assertEquals(literalNegated, shouldBeNegatedLiteral);
        assertEquals(and, shouldBeOriginalAnd);
        assertEquals(and, shouldBeExtractedAnd);
        assertEquals(or, shouldBeOriginalOr);
        assertEquals(or, shouldBeExtractedOr);
        assertNull(shouldBeNull);
    }

}