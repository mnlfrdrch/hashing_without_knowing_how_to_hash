package main.java.hashing_without_knowing_how_to_hash.model.lookuptable;

import localhost.hashing_without_knowing_how_to_hash.model.BijectionQGramsAndBits;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.Domain;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashFunctionShare;
import lombok.Getter;

import java.util.*;

/**
 * Generates and holds all the truth tables to a party's individual hash function share
 * -> One truth table per hash value bit position
 */
public class HashTable {

    @Getter
    private List<TruthTable> truthTables;
    private HashFunctionShare hashFunctionShare;

    @Getter
    private Map<Integer, String> literalNameMapping;
    public HashTable(HashFunctionShare hashFunctionShare){
        this.hashFunctionShare = hashFunctionShare;
        truthTables=new ArrayList<>();
        literalNameMapping=new HashMap<>();
        genTruthTables();
    }

    private void genTruthTables(){
        int bitCountOfHashValue=getHashValueLength();
        int bitCountOfNGram=getHashKeyLength();
        for(int literalIndex=0; literalIndex<bitCountOfHashValue; literalIndex++){
            TruthTable truthTable=new TruthTable(bitCountOfNGram);
            for (BitSet x: hashFunctionShare.getFunctionMapping().keySet()){
                BitSet y= hashFunctionShare.getFunctionMapping().get(x); //y=h(x)
                truthTable.addRow(x, y.get(literalIndex));
            }
            truthTables.add(truthTable);
        }
    }

    public int getHashValueLength(){
        return getMaxBitSetLength(hashFunctionShare.getFunctionMapping().values());
    }

    public int getHashKeyLength(){
        Domain domain= hashFunctionShare.getDomain();
        BijectionQGramsAndBits bijection=new BijectionQGramsAndBits(domain);
        int keyLength=bijection.getBitSetLength();
        return keyLength;
    }

    public int getMaxBitSetLength(Collection<BitSet> bitSets){
        int maxLength=0;
        for(BitSet hashValue: bitSets){
            if(hashValue.length()>maxLength){
                maxLength=hashValue.length();
            }
        }
        return maxLength;
    }
}
