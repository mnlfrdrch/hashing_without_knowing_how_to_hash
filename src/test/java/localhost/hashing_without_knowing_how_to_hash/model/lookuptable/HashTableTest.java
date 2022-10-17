package test.java.localhost.hashing_without_knowing_how_to_hash.model.lookuptable;

import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashFunctionShare;
import mockdata.HashFunctionShareMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashTableTest {

    private HashTable hashTable;

    @BeforeEach
    public void setUp(){
        HashFunctionShare mockedHashFunctionShare= HashFunctionShareMockData.mockHashFunctionShare();
        hashTable =new HashTable(mockedHashFunctionShare);
    }

    @Test
    public void testGetHashValueLengthAverageCase(){
        //given

        //when
        int actualHashValueLength= hashTable.getHashValueLength();

        //then
        assertEquals(3, actualHashValueLength);
    }


    @Test
    public void testIfTruthTableGeneratorProducesCorrectCNFString(){
        //given
        List<TruthTable> truthTableList= hashTable.getTruthTables();

        TruthTable firstTruthTable=truthTableList.get(0);
        String expectedFullFormula = HashFunctionShareMockData.getEquivalentFormula();
        List<String> expectedClauseList= Arrays.asList(expectedFullFormula.split(" \\| "));

        //when
        String actualFullFormula=firstTruthTable.getCNF().toString();
        List<String> actualClauseList=Arrays.asList(actualFullFormula.split(" \\| "));

        //then
        assertTrue(expectedClauseList.containsAll(actualClauseList) && actualClauseList.containsAll(expectedClauseList));
    }

}