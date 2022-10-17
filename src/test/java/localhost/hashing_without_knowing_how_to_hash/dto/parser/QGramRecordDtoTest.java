package localhost.hashing_without_knowing_how_to_hash.dto.parser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QGramRecordDtoTest {

    @Test
    public void testToStringWithNonEmptyNGramsList(){
        //given
        String id="nGramRecord-1";
        String biGramA="AB";
        String biGramB="Z4";
        String biGramC="PZ";
        Set<String> nGramSet= Set.of(biGramA,biGramB,biGramC);
        QGramRecordDto qGramRecordDto =new QGramRecordDto(id, nGramSet);
        String potentialString1="id="+id+" ngrams={"+biGramA+", "+biGramB+", "+biGramC+"}";
        String potentialString2="id="+id+" ngrams={"+biGramA+", "+biGramC+", "+biGramB+"}";
        String potentialString3="id="+id+" ngrams={"+biGramB+", "+biGramA+", "+biGramC+"}";
        String potentialString4="id="+id+" ngrams={"+biGramB+", "+biGramC+", "+biGramA+"}";
        String potentialString5="id="+id+" ngrams={"+biGramC+", "+biGramA+", "+biGramB+"}";
        String potentialString6="id="+id+" ngrams={"+biGramC+", "+biGramB+", "+biGramA+"}";

        //when
        String actualString= qGramRecordDto.toString();

        //then
        assertTrue(actualString.equals(potentialString1)||
                actualString.equals(potentialString2)||
                actualString.equals(potentialString3)||
                actualString.equals(potentialString4)||
                actualString.equals(potentialString5)||
                actualString.equals(potentialString6));
    }

    @Test
    public void testToStringWithEmptyNGramsList(){
        //given
        String id="nGramRecord-1";
        Set<String> nGramSet=new HashSet<>();
        QGramRecordDto qGramRecordDto =new QGramRecordDto(id, nGramSet);
        String expectedString="id="+id+" ngrams={}";

        //when
        String actualString= qGramRecordDto.toString();

        //then
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithNGramsListIsNull(){
        //given
        String id="nGramRecord-1";
        Set<String> nGramSet=null;
        QGramRecordDto qGramRecordDto =new QGramRecordDto(id, nGramSet);
        String expectedString="id="+id+" ngrams={}";

        //when
        String actualString= qGramRecordDto.toString();

        //then
        assertEquals(expectedString, actualString);
    }
}