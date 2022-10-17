package localhost.hashing_without_knowing_how_to_hash.model.formula;

import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashFunctionShare;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.HashTable;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.TruthTable;
import mockdata.HashFunctionShareMockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FormulaOptimiserTest {

    private FormulaOptimiser formulaOptimiser;

    @BeforeEach
    public void setUp(){
        HashFunctionShare hashFunctionShare=HashFunctionShareMockData.mockHashFunctionShare();
        HashTable hashTable =new HashTable(hashFunctionShare);
        formulaOptimiser=new FormulaOptimiser(hashTable);
    }

    @Test
    public void testLiteralOfMoreThenOneCharacterIsAllowed(){
        FormulaFactory factory=new FormulaFactory();
        PropositionalParser parser=new PropositionalParser(factory);
        String expectedFormula = "AB & ~CD";

        Formula actualFormula=null;
        try {
            actualFormula = parser.parse(expectedFormula);
        }
        catch (ParserException parserException){
            fail(parserException.getMessage());
        }

        assertNotNull(actualFormula);
        assertEquals(expectedFormula, actualFormula.toString());
    }


    @Test
    public void testLoadFormulasDoesNotTrowException(){
        assertDoesNotThrow(()->{formulaOptimiser.loadFormulas();});
    }

    @Test
    public void testFormulaManipulatorThrowsExceptionForEmptyTruthTableGenerator(){
        //given
        HashTable hashTable =null;
        formulaOptimiser=new FormulaOptimiser(hashTable);

        //when + then
        assertThrows(NullPointerException.class,()->{formulaOptimiser.loadFormulas();});
    }

    @Test
    public void testGenFormulaFromEmptyString(){
        //given
        String formulaString="";

        //when
        Formula formula=null;
        try{
            formula=formulaOptimiser.genFormulaFromString(formulaString);
        }catch (ParserException parseException){
            String exceptionMessage=parseException.getMessage();
            fail(exceptionMessage);
        }

        //then
        assertNotNull(formula);
    }

    @Test
    public void testGenFormulaFromStringShouldContainExpectedMinTerms(){
        //given
        HashFunctionShare mockedHashFunctionShare=HashFunctionShareMockData.mockHashFunctionShare();
        HashTable hashTable =new HashTable(mockedHashFunctionShare);
        formulaOptimiser=new FormulaOptimiser(hashTable);
        List<TruthTable> truthTableList= hashTable.getTruthTables();
        String formulaString=truthTableList.get(0).getCNF().toString();

        String expectedEquivalentFormulaString=HashFunctionShareMockData.getEquivalentFormula();
        String expectedEquivalentFormulaStringWithoutBrackets=expectedEquivalentFormulaString.replaceAll("\\(", "").replaceAll("\\)", "");
        List<String> expectedFormulaMinTerms=List.of(expectedEquivalentFormulaStringWithoutBrackets.split(" \\| "));

        //when
        Formula actualFormula=null;
        try{
            actualFormula=formulaOptimiser.genFormulaFromString(formulaString);
        }catch (ParserException parseException){
            fail(parseException.getMessage());
        }

        //then
        List<String> actualFormulaMinTerms=List.of(actualFormula.toString().split(" \\| "));
        assertTrue(expectedFormulaMinTerms.containsAll(actualFormulaMinTerms) && actualFormulaMinTerms.containsAll(expectedFormulaMinTerms));
    }

    @Test
    public void testFormulaShouldBeImprovedInLength(){
        //given
        String expectedEquivalentFormulaString=HashFunctionShareMockData.getEquivalentFormula();
        Formula cnfFormula=null;
        try{
            cnfFormula=formulaOptimiser.genFormulaFromString(expectedEquivalentFormulaString);
        }catch (ParserException parseException){
            fail(parseException.getMessage());
        }

        //when
        FormulaContainerDto formulaContainer=formulaOptimiser.genImprovedFormulas();

        //then
        Formula improvedFormula=formulaContainer.getFormulaList().iterator().next();
        int cnfFormulaLength=cnfFormula.toString().length();
        int improvedFormulaLength=improvedFormula.toString().length();

        // '<=' instead of '<' if formula is already optimal
        assertTrue(improvedFormulaLength<=cnfFormulaLength);
    }
}