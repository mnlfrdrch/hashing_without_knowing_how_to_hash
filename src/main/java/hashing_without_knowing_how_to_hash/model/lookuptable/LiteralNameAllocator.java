package main.java.hashing_without_knowing_how_to_hash.model.lookuptable;

import localhost.hashing_without_knowing_how_to_hash.constants.CharacterSets;
import localhost.hashing_without_knowing_how_to_hash.util.LiteralNameMathUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Assings each element of a numerical set {0,...,n} in natural numbers an alphabetic and definite uppercase letter combination
 */
public class LiteralNameAllocator {

    private int literalCount;
    private int basis;
    private List<Character> listOfLetters;

    public LiteralNameAllocator(int literalCount){
        this.literalCount=literalCount;
        listOfLetters=new ArrayList<>(CharacterSets.UPPERCASE_LETTERS);
        Collections.sort(listOfLetters);
        basis=listOfLetters.size();
    }

    /**
     * Assigns every literal an individual alphabetic letter combination
     * @param index of the literal in BitSet data structure
     * @return StringBuilder of literal name
     */
    public StringBuilder getLiteralName(int index){
        int absIndex=index;

        StringBuilder literalName=new StringBuilder("");

        int lengthOfString= LiteralNameMathUtil.positionalCount(Math.max(0,literalCount-1),basis);

        for(int i=lengthOfString; i>0; i--){
            int approxFactor= LiteralNameMathUtil.calculateFittingFactor(absIndex, basis, i-1);
            literalName.append(digitToLetter(approxFactor));
            int potTerm= LiteralNameMathUtil.potencyTerm(approxFactor, basis, i);
            if(potTerm<absIndex){
                absIndex-=potTerm;
            }
        }

        return literalName;
    }

    public StringBuilder digitToLetter(int digit){
        int saveDigit=Math.abs(digit % basis);
        Character c=listOfLetters.get(saveDigit);
        StringBuilder ret=new StringBuilder();
        ret.append(c);
        return ret;
    }
}
