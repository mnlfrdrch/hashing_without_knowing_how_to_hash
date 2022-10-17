package localhost.hashing_without_knowing_how_to_hash.util;

import localhost.hashing_without_knowing_how_to_hash.constants.CharacterSets;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SetUnionUtilTest {

    @Test
    public void testUnion(){
        //given
        Set<Character> numerical= CharacterSets.NUMERICAL;
        Set<Character> emptyElement=CharacterSets.EMPTY_ELEMENT;
        Set<Character> numericalAndEmptyElement=Set.of('0','1','2','3','4','5','6','7','8','9','#');
        SetUnionUtil<Character> unionUtil=new SetUnionUtil();

        //when
        Set<Character> actualUnionSet=unionUtil.union(numerical, emptyElement);

        //then
        assertTrue(numericalAndEmptyElement.containsAll(actualUnionSet) && actualUnionSet.containsAll(numericalAndEmptyElement));
    }

}