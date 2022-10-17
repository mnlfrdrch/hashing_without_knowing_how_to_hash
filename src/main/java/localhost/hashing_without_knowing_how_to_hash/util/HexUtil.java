package localhost.hashing_without_knowing_how_to_hash.util;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import lombok.experimental.UtilityClass;

import java.util.BitSet;

/**
 * Converts hexadecimal strings into their bit representation
 */
@UtilityClass
public class HexUtil {

    private final int NUM_BITS_IN_HEX_DIGIT =4;

    /**
     * Creates the BitSet bit representation of any given hexadecimal string
     * @param hexadecimalSource the hexadecimal string, which must only consist of hexadecimal characters
     * @return the equivalent representation as BitSet
     */
    public BitSet convertHexStringIntoBits(String hexadecimalSource){
        int numOfHexCharacters=hexadecimalSource.length();
        int numOfRequiredBits=numOfHexCharacters*NUM_BITS_IN_HEX_DIGIT;
        BitSet convertedBits=new FixedLengthBitSet(numOfRequiredBits);

        for (int i=0; i<numOfHexCharacters; i++){
            Character recentChar=hexadecimalSource.charAt(i);
            BitSet charBitSet= convertHexCharacterIntoBits(recentChar);
            for (int j = 0; j< NUM_BITS_IN_HEX_DIGIT; j++){
                if(charBitSet.get(j)){
                    int absoluteIndex=(NUM_BITS_IN_HEX_DIGIT *i)+j;
                    convertedBits.set(absoluteIndex);
                }
            }
        }

        return convertedBits;
    }

    /**
     * Converts a single hexadecimal character into the 4 bit BitSet representation
     * e.g 0 -> 0000, 1 -> 0001, ... , f -> 1111
     * @param hexadecimalSource a single hexadecimal character
     * @return the equivalent representation as 4 bit BitSet
     */
    public BitSet convertHexCharacterIntoBits(Character hexadecimalSource){
        BitSet bitSet=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);

        switch (hexadecimalSource){
            case '0':
                //0000
                bitSet= instantiateBitSet0();
                break;
            case '1':
                //0001
                bitSet= instantiateBitSet1();
                break;
            case '2':
                //0010
                bitSet= instantiateBitSet2();
                break;
            case '3':
                //0011
                bitSet= instantiateBitSet3();
                break;
            case '4':
                //0100
                bitSet= instantiateBitSet4();
                break;
            case '5':
                //0101
                bitSet= instantiateBitSet5();
                break;
            case '6':
                //0110
                bitSet= instantiateBitSet6();
                break;
            case '7':
                //0111
                bitSet= instantiateBitSet7();
                break;
            case '8':
                //1000
                bitSet= instantiateBitSet8();
                break;
            case '9':
                //1001
                bitSet= instantiateBitSet9();
                break;
            case 'a':
                //1010
                bitSet= instantiateBitSetA();
                break;
            case 'b':
                //1011
                bitSet= instantiateBitSetB();
                break;
            case 'c':
                //1100
                bitSet= instantiateBitSetC();
                break;
            case 'd':
                //1101
                bitSet= instantiateBitSetD();
                break;
            case 'e':
                //1110
                bitSet= instantiateBitSetE();
                break;
            case 'f':
                //1111
                bitSet= instantiateBitSetF();
                break;
            default:
                try{
                    throw new Exception("Character not allowed");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

        return bitSet;
    }

    private BitSet instantiateBitSet0(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        /*b.set(0);*//*b.set(1);*//*b.set(2);*//*b.set(3);*/
        return b;
    }
    private BitSet instantiateBitSet1(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        /*b.set(0);*//*b.set(1);*//*b.set(2);*/b.set(3);
        return b;
    }

    private BitSet instantiateBitSet2(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        /*b.set(0);*//*b.set(1);*/b.set(2);/*b.set(3);*/
        return b;
    }

    private BitSet instantiateBitSet3(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        /*b.set(0);*//*b.set(1);*/b.set(2);b.set(3);
        return b;
    }

    private BitSet instantiateBitSet4(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        /*b.set(0);*/b.set(1);/*b.set(2);*//*b.set(3);*/
        return b;
    }

    private BitSet instantiateBitSet5(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        /*b.set(0);*/b.set(1);/*b.set(2);*/b.set(3);
        return b;
    }

    private BitSet instantiateBitSet6(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        /*b.set(0);*/b.set(1);b.set(2);/*b.set(3);*/
        return b;
    }

    private BitSet instantiateBitSet7(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        /*b.set(0);*/b.set(1);b.set(2);b.set(3);
        return b;
    }

    private BitSet instantiateBitSet8(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        b.set(0);/*b.set(1);*//*b.set(2);*//*b.set(3);*/
        return b;
    }

    private BitSet instantiateBitSet9(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        b.set(0);/*b.set(1);*//*b.set(2);*/b.set(3);
        return b;
    }

    private BitSet instantiateBitSetA(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        b.set(0);/*b.set(1);*/b.set(2);/*b.set(3);*/
        return b;
    }
    private BitSet instantiateBitSetB(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        b.set(0);/*b.set(1);*/b.set(2);b.set(3);
        return b;
    }

    private BitSet instantiateBitSetC(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        b.set(0);b.set(1);/*b.set(2);*//*b.set(3);*/
        return b;
    }

    private BitSet instantiateBitSetD(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        b.set(0);b.set(1);/*b.set(2);*/b.set(3);
        return b;
    }

    private BitSet instantiateBitSetE(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        b.set(0);b.set(1);b.set(2);/*b.set(3);*/
        return b;
    }

    private BitSet instantiateBitSetF(){
        BitSet b=new FixedLengthBitSet(NUM_BITS_IN_HEX_DIGIT);
        b.set(0);b.set(1);b.set(2);b.set(3);
        return b;
    }

    /**
     * Detects, if a given string only consists of hexadecimal digits.
     * Hexadecimal digits are the decimal digits 0 to 9 and the lowercase letters a to f
     * @param source the string that should be tested for hexadecimal
     * @return *true* if source only consists of the given hexadecimal digits, else *false*
     */
    public boolean isHexaDecimal(String source){
        return source.matches("[0-9a-f]+");
    }

}
