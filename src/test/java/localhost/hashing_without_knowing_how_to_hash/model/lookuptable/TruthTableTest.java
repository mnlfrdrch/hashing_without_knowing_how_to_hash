package localhost.hashing_without_knowing_how_to_hash.model.lookuptable;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

class TruthTableTest {

    private TruthTable truthTable;

    @Test
    public void testGetMinTermAllLiteralsFalse(){
        int literalCount=3;
        truthTable=new TruthTable(literalCount);
        BitSet allLiteralsFalse=new FixedLengthBitSet(literalCount);

        StringBuilder minTerm=truthTable.getMinTerm(allLiteralsFalse);

        assertEquals("(~A & ~B & ~C)", minTerm.toString());
    }

    @Test
    public void testGetMinTermAllLiteralsTrue(){
        int literalCount=3;
        truthTable=new TruthTable(literalCount);
        BitSet allLiteralsTrue=new FixedLengthBitSet(literalCount);
        allLiteralsTrue.set(0,literalCount,true);

        StringBuilder minTerm=truthTable.getMinTerm(allLiteralsTrue);

        assertEquals("(A & B & C)", minTerm.toString());
    }

    @Test
    public void testGetMaxTermAllLiteralsFalse(){
        int literalCount=3;
        truthTable=new TruthTable(literalCount);
        BitSet allLiteralsFalse=new FixedLengthBitSet(literalCount);

        StringBuilder minTerm=truthTable.getMaxTerm(allLiteralsFalse);

        assertEquals("(A | B | C)", minTerm.toString());
    }

    @Test
    public void testGetMaxTermAllLiteralsTrue(){
        int literalCount=3;
        truthTable=new TruthTable(literalCount);
        BitSet allLiteralsTrue=new FixedLengthBitSet(literalCount);
        allLiteralsTrue.set(0, literalCount,true);

        StringBuilder minTerm=truthTable.getMaxTerm(allLiteralsTrue);

        assertEquals("(~A | ~B | ~C)", minTerm.toString());
    }

    @Test
    public void testCNF(){
        //given
        int literalCount=3;
        truthTable=new TruthTable(literalCount);
        BitSet onlyAtrue=new FixedLengthBitSet(literalCount);
        onlyAtrue.set(0,true);
        BitSet literalsBandCtrue=new FixedLengthBitSet(literalCount);
        literalsBandCtrue.set(1, true);
        literalsBandCtrue.set(2, true);
        truthTable.addRow(onlyAtrue, true);
        truthTable.addRow(literalsBandCtrue, true);

        //when
        StringBuilder cnf=truthTable.getCNF();

        //then
        assertEquals("(A & ~B & ~C) | (~A & B & C)", cnf.toString());
    }

}