package main.java.hashing_without_knowing_how_to_hash.util;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Is required by BijectionHexAndBits, to generate all possible QGrams and create a mapping
 */
@UtilityClass
public class QGramUtil {

    /**
     * Set of all qgrams that are possible to create with given q and characters
     * e.g. let q=2 and characters=Set.of('A','B')
     * the generated set is {"AA","AB","BA","BB"}
     * @param q if q=2, then bigrams are generated
     * @param characters if characters=Set.of('A','B') only the character 'A' and 'B' are used
     * @return Set of all possible qgrams as a string
     */
    public Set<String> generateAllPossibleQGrams(int q, Set<Character> characters){
        Set<String> allNGrams=new HashSet<>();

        for (Character c : characters) {
            String newQGram = new String();
            newQGram += c;
            allNGrams.addAll(generateQGramsRec(q - 1, characters, newQGram));
        }
        return allNGrams;
    }

    private Set<String> generateQGramsRec(int i, Set<Character> characters, String accu){
        Set<String> subSetOfQGrams=new HashSet<>();
        if(i<=0){
            subSetOfQGrams.add(accu);
        }
        else {
            for (Character c:characters){
                Set<String> alreadyProcessedQGrams=generateQGramsRec(i-1, characters, accu+c);
                subSetOfQGrams.addAll(alreadyProcessedQGrams);
            }
        }
        return subSetOfQGrams;
    }
}
