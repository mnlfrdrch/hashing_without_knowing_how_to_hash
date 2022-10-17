package test.java.localhost.hashing_without_knowing_how_to_hash.model.hash_function_share;

import localhost.hashing_without_knowing_how_to_hash.constants.CharacterSets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DomainTest {

    private Domain domain;

    @BeforeEach
    public void setUp(){
        domain=new Domain(2, CharacterSets.NUMERICAL);
    }

    @Test
    public void testGetAllNGramsShouldConsistOf100IndividualNGrams(){
        //given
        int expectedNumberOfQGrams=100;

        //when
        List<String> allQGrams=domain.getAllQGrams();

        //then
        assertEquals(expectedNumberOfQGrams, allQGrams.size());
    }

    @Test
    public void testAlternativeConstructorShouldWorkEqually(){
        //given
        List<String> previousConstructorQGrams=domain.getAllQGrams();

        //when
        domain=new Domain(2, new ArrayList<>(CharacterSets.NUMERICAL));
        List<String> alternativeConstructorQGrams=domain.getAllQGrams();

        //then
        assertTrue(previousConstructorQGrams.containsAll(alternativeConstructorQGrams) && alternativeConstructorQGrams.containsAll(previousConstructorQGrams));
    }

    @Test
    public void testQOfGRamsShouldBe2(){
        //given
        int expectedQ=2;

        //when
        int actualQ=domain.getQOfQGram();

        //then
        assertEquals(expectedQ, actualQ);
    }

    @Test
    public void testGetCharacterListShouldContainExactlyTheCharactersFormConstructorArgument(){
        //given
        List<Character> expectedCharacterList=new ArrayList<>(CharacterSets.NUMERICAL);

        //when
        List<Character> actualCharacterList=domain.getCharacterList();

        //then
        assertTrue(expectedCharacterList.containsAll(actualCharacterList) && actualCharacterList.containsAll(expectedCharacterList));
    }

}