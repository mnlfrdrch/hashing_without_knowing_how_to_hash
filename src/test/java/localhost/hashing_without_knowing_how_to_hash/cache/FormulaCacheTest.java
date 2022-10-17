package test.java.localhost.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.HashTable;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.TruthTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FormulaCacheTest {

    private FormulaCache formulaCache;

    @BeforeEach
    public void setUp(){
        formulaCache=FormulaCache.getInstance();
    }

    @AfterEach
    public void tearDown(){
        formulaCache.setFormulaContainerDto(null);
    }

    @Test
    public void testGetInstanceCreatesActualInstance(){
        assertNotNull(formulaCache);
    }

    @Test
    public void testGetMultipleCallsOfGetInstanceShouldReturnTheSameInstance(){
        //when
        FormulaCache shouldBeSameInstance=FormulaCache.getInstance();

        //then
        assertSame(formulaCache, shouldBeSameInstance);
    }

    HashTable mockHashTableForOneSimpleFormula(String formula){
        TruthTable mockedNegationTruthTable=mock(TruthTable.class);
        when(mockedNegationTruthTable.getCNF()).thenReturn(new StringBuilder(formula));

        HashTable mockedHashTable=mock(HashTable.class);
        when(mockedHashTable.getTruthTables()).thenReturn(List.of(mockedNegationTruthTable));
        return mockedHashTable;
    }

    @Test
    public void testSetFormulasShouldGenerateEquivalentFormulaContainer(){
        //given
        String expectedFormula="~A";
        HashTable mockedHashTable=mockHashTableForOneSimpleFormula(expectedFormula);
        FormulaCache formulaCache=FormulaCache.getInstance();

        //when
        formulaCache.generateFormulasFromHashTable(mockedHashTable);
        FormulaContainerDto formulaContainer=formulaCache.getFormulaContainerDto();
        String actualFormula=formulaContainer.getFormulaList().get(0).toString();

        //then
        assertEquals(expectedFormula, actualFormula);
    }
}