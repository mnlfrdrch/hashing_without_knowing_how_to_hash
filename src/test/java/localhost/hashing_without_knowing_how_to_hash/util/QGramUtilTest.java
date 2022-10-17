package test.java.localhost.hashing_without_knowing_how_to_hash.util;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QGramUtilTest {

    @Test
    public void testGenerateAllPossibleQGramsShouldCreateEqualSetAsReference(){
        //given
        int q=2;
        Set<Character> characterSet=Set.of('A','B');
        QGramUtil.generateAllPossibleQGrams(q, characterSet);
        Set<String> expectedQGrams=Set.of("AA","AB","BA","BB");

        //when
        Set<String> actualQGrams=QGramUtil.generateAllPossibleQGrams(q, characterSet);

        //then
        assertTrue(expectedQGrams.containsAll(actualQGrams) && actualQGrams.containsAll(expectedQGrams));
    }

}