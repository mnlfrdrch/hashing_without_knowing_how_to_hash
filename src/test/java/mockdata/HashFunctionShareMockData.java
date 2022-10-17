package mockdata;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.Domain;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashFunctionShare;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class HashFunctionShareMockData {
    //example hash function
    public Map<BitSet, BitSet> getHashFunctionA() {
        int lenX = 2;
        int lenY = 3;
        Map<BitSet, BitSet> hashFunction = new HashMap<>();

        //x values
        BitSet firstX = new FixedLengthBitSet(lenX);
        BitSet secondX = new FixedLengthBitSet(lenX);
        BitSet thirdX = new FixedLengthBitSet(lenX);
        BitSet fourthX = new FixedLengthBitSet(lenX);
        secondX.set(1, true);
        thirdX.set(0, true);
        fourthX.set(0,true);
        fourthX.set(1,true);

        //y values
        BitSet valA=new FixedLengthBitSet(lenY);
        BitSet valB=new FixedLengthBitSet(lenY);
        BitSet valC=new FixedLengthBitSet(lenY);
        valA.set(0, true);
        valC.set(0,true);
        valC.set(1, true);

        //mapping
        hashFunction.put(firstX, valA);
        hashFunction.put(secondX, valC);
        hashFunction.put(thirdX, valB);
        hashFunction.put(fourthX, valC);

        return hashFunction;
    }

    public String getEquivalentFormula(){
        return "(~A & ~B) | (~A & B) | (A & B)";
    }

    public HashFunctionShare mockHashFunctionShare(){
        HashFunctionShare mockedHashFunctionShare=Mockito.mock(HashFunctionShare.class);

        Mockito.when(mockedHashFunctionShare.generateFunctionMapping()).thenReturn(getHashFunctionA());
        Mockito.when(mockedHashFunctionShare.getFunctionMapping()).thenReturn(getHashFunctionA());
        Mockito.when(mockedHashFunctionShare.getDomain()).thenReturn(new Domain(2, Set.of('A','B')));

        return mockedHashFunctionShare;
    }
}
