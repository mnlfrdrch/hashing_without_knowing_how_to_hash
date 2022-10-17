package localhost.hashing_without_knowing_how_to_hash.model.lookuptable;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A truth table (one for each bit of the hash value) for all possible x of h(x)
 * One truth table for each bit of the hash value in hash table can be generated.
 * For a truth table there is an equivalent propositional formula.
 * This formula is required to build a garbled circuit for each bit of the hash value
 */
public class TruthTable {

    private Map<BitSet, Boolean> table;
    private int literalCount;
    private LiteralNameAllocator literalNameAllocator;

    public TruthTable(int literalCount){
        table=new HashMap<>();
        this.literalCount=literalCount;
        literalNameAllocator =new LiteralNameAllocator(literalCount);
    }
    
    public StringBuilder getMinTerm(BitSet bitSet){
        StringBuilder minTerm=new StringBuilder("(");

        for(int i=0;i<literalCount;i++){
            if(!bitSet.get(i)){
                minTerm.append("~");
            }
            minTerm.append(literalNameAllocator.getLiteralName(i));
            minTerm.append(" & ");
        }

        minTerm=removePatternAtEndOf(minTerm);
        minTerm.append(")");

        return minTerm;
    }

    public StringBuilder getMaxTerm(BitSet bitSet){
        StringBuilder maxTerm=new StringBuilder("(");

        for(int i=0;i<literalCount; i++){
            if(bitSet.get(i)){
                maxTerm.append("~");
            }
            maxTerm.append(literalNameAllocator.getLiteralName(i));
            maxTerm.append(" | ");
        }

        maxTerm=removePatternAtEndOf(maxTerm);
        maxTerm.append(")");
        return maxTerm;
    }

    /**
     * Generates a formula in CNF, which is equivalent to the given truth table
     * @return equivalent propositional formula
     */
    public StringBuilder getCNF(){
        StringBuilder cnf=new StringBuilder("");

        for(BitSet bitSet: table.keySet()){
            if(table.get(bitSet)){
                cnf.append(getMinTerm(bitSet));
                cnf.append(" | ");
            }
        }

        cnf=removePatternAtEndOf(cnf);

        return cnf;
    }


    public StringBuilder getDNF(){
        StringBuilder dnf=new StringBuilder("");

        for(BitSet bitSet:table.keySet()){
            if(table.get(bitSet)){
                dnf.append(getMaxTerm(bitSet));
                dnf.append(" & ");
            }
        }

        dnf=removePatternAtEndOf(dnf);
        return dnf;
    }


    public StringBuilder removePatternAtEndOf(StringBuilder stringBuilder){
        //gets rid of redundant " | " at the end of cnf
        if(stringBuilder.length()>=3){
            stringBuilder.delete(stringBuilder.length()-3,stringBuilder.length());
        }
        return stringBuilder;
    }

    /**
     * Appends a row to the truth table where
     * @param literals is the allocation of each variable and
     * @param value is the boolean truth value of this row
     */
    public void addRow(BitSet literals, Boolean value){
        table.put(literals,value);
    }

}
