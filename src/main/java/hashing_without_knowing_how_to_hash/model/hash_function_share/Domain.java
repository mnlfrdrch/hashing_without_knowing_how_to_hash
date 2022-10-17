package main.java.hashing_without_knowing_how_to_hash.model.hash_function_share;

import localhost.hashing_without_knowing_how_to_hash.util.QGramUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Determines the 'Set' of all potential qgrams for which the hash function can be evaluated later
 */
public class Domain {

    /**
     * All possible qrams for which the hash function can be evaluated
     */
    @Getter
    private List<String> allQGrams;
    @Getter
    private List<Character> characterList;
    @Getter
    int qOfQGram;

    public Domain(int qOfQGram, List<Character> characterList){
        this.qOfQGram = qOfQGram;
        this.characterList=characterList;
        Set<String> setOfAllQGrams=QGramUtil.generateAllPossibleQGrams(qOfQGram, new HashSet<>(characterList));
        allQGrams =new ArrayList<>(setOfAllQGrams);
    }

    public Domain(int qOfQGram, Set<Character> characterSet){
        this.qOfQGram = qOfQGram;
        this.characterList=new ArrayList<>(characterSet);
        Set<String> setOfAllQGrams=QGramUtil.generateAllPossibleQGrams(qOfQGram, characterSet);
        allQGrams =new ArrayList<>(setOfAllQGrams);
    }

}
