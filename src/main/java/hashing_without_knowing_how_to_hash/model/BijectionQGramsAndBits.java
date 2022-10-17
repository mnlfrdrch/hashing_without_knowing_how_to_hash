package main.java.hashing_without_knowing_how_to_hash.model;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.Domain;
import lombok.Getter;

import java.util.*;

/**
 * Generates a direct mapping between each qgram in Domain and a unique representation in bits
 * This way a qgram and their BitSet representation can be used interchangeably
 * The BitSet representation is required for building and evaluation a garbled circuit
 * The assignment of the bits of a representation is used as assignment of the variables of a truth table
 * or the assignment of the initial wire of a garbled circuit
 */
public class BijectionQGramsAndBits {

    private Domain domain;
    @Getter
    private int bitSetLength;
    private Map<String, BitSet> nGramMapsToBitSetEncoding;
    private Map<BitSet, String> bitSetEncodingMapsToNGram;

    private final char SYMBOL_ZERO='0';
    private final char SYMBOL_ONE='1';
    private final boolean LOGIC_ZERO=false;
    private final boolean LOGIC_ONE=true;

    public BijectionQGramsAndBits(Domain domain){
        this.domain=domain;
        bitSetLength=computeRequiredBitSetLength();
        nGramMapsToBitSetEncoding=new HashMap<>();
        bitSetEncodingMapsToNGram=new HashMap<>();
        buildMappings();
    }

    /**
     * Find the BitSet representation for a qgram
     * @param x the qgram
     * @return the BitSet representation of qgram x
     */
    public BitSet encodeQGramAsBitSet(String x){
        return nGramMapsToBitSetEncoding.get(x);
    }

    /**
     * Find the qgram of a BitSet representation
     * @param bitSet the representation of a qgram
     * @return the corresponding qgram
     */
    public String encodeBitSetAsNGram(BitSet bitSet){
        return bitSetEncodingMapsToNGram.get(bitSet);
    }

    public int positionOfNGramInSortedOrder(String x){
        List<String> unorderedNGrams=new ArrayList<>(domain.getAllQGrams());
        Collections.sort(unorderedNGrams);
        List<String> nGramsInLexicographicOrder=new ArrayList<>(unorderedNGrams);

        int index=0;
        int maxIndex=nGramsInLexicographicOrder.size();
        for (int i=0; i<maxIndex; i++){
            String recentNGram=nGramsInLexicographicOrder.get(i);
            if(recentNGram.equals(x)){
                index=i;
                i=maxIndex;
            }
        }

        return index;
    }

    public String convertDecimalToBinaryRepresentation(int decimal){
        String binary=Integer.toBinaryString(decimal);
        return binary;
    }

    public BitSet convertBinarySymbolsToBitSetEncoding(String binary){
        int numOfBitsInArgument=binary.length();
        int numOfLeadingZeros=bitSetLength-numOfBitsInArgument;

        BitSet encoding=new FixedLengthBitSet(bitSetLength);

        for(int i=0; i<bitSetLength; i++){
            //leading zeros
            if(i<numOfLeadingZeros){
                encoding.set(i, LOGIC_ZERO);
            }
            //actual value
            else {
                encoding= setBitOfEncodingForNonLeadingZerosPart(encoding, binary, numOfLeadingZeros, i);
            }
        }
        return encoding;
    }

    public BitSet setBitOfEncodingForNonLeadingZerosPart(BitSet encoding, String binary, int numOfLeadingZeros, int absolutePosition){
        int relativePos=absolutePosition-numOfLeadingZeros;
        switch (binary.charAt(relativePos)) {
            case SYMBOL_ZERO -> encoding.set(absolutePosition, LOGIC_ZERO);
            case SYMBOL_ONE -> encoding.set(absolutePosition, LOGIC_ONE);
            default -> { /*throw new Exception("Illegal character " + String.valueOf(binary.charAt(i)));*/}
        }
        return encoding;
    }

    public int computeRequiredBitSetLength(){
        List<String> allPossibleNGrams=domain.getAllQGrams();
        int numberOfNGrams=allPossibleNGrams.size();
        double log2OfNumNGrams=Math.log(numberOfNGrams) / Math.log(2);
        int requiredBitCount=(int)Math.ceil(log2OfNumNGrams);
        return requiredBitCount;
    }

    private void buildMappings(){
        buildQGramMapsToBitSetEncoding();
        buildBitSetMapsToX();
    }

    private void buildQGramMapsToBitSetEncoding(){
        for(String qgram: domain.getAllQGrams()){
            int indexOfQGram=positionOfNGramInSortedOrder(qgram);
            String binaryRepresentationOfIndex=convertDecimalToBinaryRepresentation(indexOfQGram);
            BitSet encodedNGram=convertBinarySymbolsToBitSetEncoding(binaryRepresentationOfIndex);
            nGramMapsToBitSetEncoding.put(qgram, encodedNGram);
        }
    }

    private void buildBitSetMapsToX(){
        for(String plainQGram: nGramMapsToBitSetEncoding.keySet()){
            BitSet encodedNGram=nGramMapsToBitSetEncoding.get(plainQGram);
            bitSetEncodingMapsToNGram.put(encodedNGram, plainQGram);
        }
    }

}
