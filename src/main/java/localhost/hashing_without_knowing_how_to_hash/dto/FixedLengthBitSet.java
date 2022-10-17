package localhost.hashing_without_knowing_how_to_hash.dto;

import java.util.BitSet;

/**
 * A FixedLengthBitSet is an extension of the BitSet data type
 * The regular BitSet saves the length as index of highest value 1
 * To provide 'empty' or 'not full' BitSets with a fixed length, this extension is required
 * ImprovedBitSet supports a specified length at initialisation of instance
 * All methods are the same as in BitSet, apart from enforcing the introduced behaviour
 *
 * Example:
 * BitSet bitSet=new BitSet(10);
 * BitSet fixedLengthBitSet=new FixedLengthBitSet(10);
 * bitSet.set(4);
 * fixedLengthBitSet.set(4);
 * bitSet.length(); // this will return 5
 * fixedLengthBitSet.length(); // while this will return 10
 */
public class FixedLengthBitSet extends BitSet {
    private int length;

    public FixedLengthBitSet(int nbits){
        super(nbits+1);
        length=nbits;
        setLastBit();
    }

    public FixedLengthBitSet(){
        super();
        super.set(0);
        length=0;
    }
    @Override
    public void and(BitSet set){
        extendImprovedBitSetIfNecessary(set.length());
        super.and(set);
        setLastBit();
    }

    @Override
    public void andNot(BitSet set){
        extendImprovedBitSetIfNecessary(set.length());
        super.andNot(set);
        setLastBit();
    }

    @Override
    public int cardinality(){
        return super.cardinality()-1;
    }

    @Override
    public void clear(){
        super.clear();
        setLastBit();
    }
    @Override
    public void clear(int bitIndex){
        super.clear(bitIndex);
        setLastBit();
    }

    @Override
    public void clear(int fromIndex, int toIndex){
        super.clear(fromIndex, toIndex);
        setLastBit();
    }

    @Override
    public Object clone(){
        FixedLengthBitSet clonedFixedLengthBitSet =new FixedLengthBitSet(length);
        BitSet clonedUnderlyingBitSet=(BitSet) super.clone();
        for(int i=0; i<clonedUnderlyingBitSet.length(); i++){
            boolean shouldBeValue=clonedUnderlyingBitSet.get(i);
            clonedFixedLengthBitSet.set(i,shouldBeValue);
        }
        clonedFixedLengthBitSet.setLastBit();
        return clonedFixedLengthBitSet;
    }

    @Override
    public void flip(int bitIndex){
        super.flip(bitIndex);
        setLastBit();
    }

    @Override
    public void flip(int fromIndex, int toIndex){
        super.flip(fromIndex, toIndex);
        setLastBit();
    }

    @Override
    public boolean get(int bitIndex){
        return super.get(bitIndex);
    }

    @Override
    public BitSet get(int fromIndex, int toIndex){
        int length=toIndex-fromIndex;
        FixedLengthBitSet partialFixedLengthBitSet =new FixedLengthBitSet(length);
        BitSet classicalBitSet=super.get(fromIndex, toIndex);
        for(int i=0; i<classicalBitSet.length(); i++){
            boolean shouldBeValue=classicalBitSet.get(i);
            partialFixedLengthBitSet.set(i,shouldBeValue);
        }
        partialFixedLengthBitSet.setLastBit();
        return partialFixedLengthBitSet;
    }

    @Override
    public boolean intersects(BitSet set){
        boolean intersects=false;
        invalidateLastBit();
        intersects=super.intersects(set);
        setLastBit();
        return intersects;
    }

    @Override
    public boolean isEmpty(){
        boolean isEmpty=false;
        invalidateLastBit();
        isEmpty=super.isEmpty();
        setLastBit();
        return isEmpty;
    }

    @Override
    public int length(){
        return length;
    }

    @Override
    public int nextClearBit(int fromIndex){
        int nextClearBit=-1;
        invalidateLastBit();
        nextClearBit=super.nextClearBit(fromIndex);
        setLastBit();
        return nextClearBit;
    }

    @Override
    public int nextSetBit(int fromIndex){
        int nextSetBit=-1;
        invalidateLastBit();
        nextSetBit=super.nextSetBit(fromIndex);
        setLastBit();
        return nextSetBit;
    }

    @Override
    public void or(BitSet set){
        extendImprovedBitSetIfNecessary(set.length());
        super.or(set);
        setLastBit();
    }

    @Override
    public void set(int bitIndex){
        if(bitIndex>length){
            invalidateLastBit();
            length++;
            setLastBit();
            super.set(bitIndex);
        }
        else {
            super.set(bitIndex);
        }
    }

    @Override
    public void set(int bitIndex, boolean value){
        if (bitIndex>length){
            invalidateLastBit();
            set(length, false);
            length=bitIndex+1;
            set(bitIndex, value);
            setLastBit();
        }else{
            super.set(bitIndex, value);
        }
    }

    @Override
    public void set(int fromIndex, int toIndex, boolean value){
        for(int i=fromIndex; i<Math.min(length, toIndex); i++){
            set(i, value);
        }
    }

    @Override
    public int size(){
        int BITS_OF_LENGTH_VARIABLE=32;
        return super.size()+BITS_OF_LENGTH_VARIABLE;
    }
    @Override
    public void xor(BitSet set){
        extendImprovedBitSetIfNecessary(set.length());
        super.xor(set);
        setLastBit();
    }

    private void setLastBit(){
        super.set(length, true);
    }

    private void invalidateLastBit(){
        super.set(length, false);
    }

    private void extendImprovedBitSetTo(int length){
        invalidateLastBit();
        this.length=length;
        setLastBit();
    }

    private void extendImprovedBitSetIfNecessary(int length){
        if(length>this.length){
            extendImprovedBitSetTo(length);
        }
    }

}
