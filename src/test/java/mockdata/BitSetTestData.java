package mockdata;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import lombok.experimental.UtilityClass;

import java.util.BitSet;

@UtilityClass
public class BitSetTestData {

    private final int len=5;

    public BitSet bitSetA(){
        BitSet a=new FixedLengthBitSet(len);
        a.set(2);
        a.set(3);

        return a;
    }

    public BitSet bitSetB(){
        BitSet b=new FixedLengthBitSet(len);
        b.set(2);
        b.set(0);
        b.set(1);

        return b;
    }

    public BitSet bitSetC(){
        BitSet c=new FixedLengthBitSet(len);
        c.set(3);
        c.set(4);

        return c;
    }

    public BitSet bitSetAxorBxorC(){
        BitSet xored=new FixedLengthBitSet(len);
        xored.set(0);
        xored.set(1);
        xored.set(4);

        return xored;
    }

    public BitSet getBitSet12Bit(){
        BitSet bitSet=new FixedLengthBitSet(12);
        bitSet.set(0);
        bitSet.set(1);
        bitSet.set(2);
        bitSet.set(4);
        bitSet.set(6);
        bitSet.set(7);
        return bitSet;
    }

}
