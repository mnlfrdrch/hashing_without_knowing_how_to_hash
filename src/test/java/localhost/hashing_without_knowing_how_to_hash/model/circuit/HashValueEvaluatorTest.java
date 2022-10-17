package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.controller.CircuitController;
import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.service.ObliviousTransferService;
import mockdata.BitSetTestData;
import mockdata.FormulaTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HashValueEvaluatorTest {

    @LocalServerPort
    private int PORT;

    @Autowired
    private CircuitController circuitController;

    @Autowired
    private ObliviousTransferService obliviousTransferService;

    private HostDto getHost(){
        return new HostDto("localhost", String.valueOf(PORT));
    }

    @BeforeEach
    public void setUp(){
        FormulaContainerDto formulaContainer=new FormulaContainerDto(FormulaTestData.get12BitAAndBFunction());
        ReflectionTestUtils.setField(FormulaCache.getInstance(), "formulaContainerDto", formulaContainer);
    }

    @Test
    public void testEvaluateEachBitProducesOutput(){
        //given
        BitSet anyEncodedQGram= BitSetTestData.getBitSet12Bit();

        //when
        HashValueEvaluator hashValueEvaluator=new HashValueEvaluator(getHost(), anyEncodedQGram);
        BitSet receivedBitSetShouldNotBeNull=hashValueEvaluator.evaluateEachBit();

        //then
        assertNotNull(receivedBitSetShouldNotBeNull);
    }

    @Test
    public void testEvaluateEachBitForAssignmentAFalseBFalseShouldBeAllFalse(){
        //given
        BitSet encodedQGram=new FixedLengthBitSet(11); //assignment A=false and B=false
        BitSet expectedBitSet=new FixedLengthBitSet(12); // function is A&B for each bit of the 12 bits, therefore should lead to 12 bit all zeros

        //when
        HashValueEvaluator hashValueEvaluator=new HashValueEvaluator(getHost(), encodedQGram);
        BitSet actualBitSet=hashValueEvaluator.evaluateEachBit(); // all bits should be false

        //then
        assertEquals(expectedBitSet, actualBitSet);
    }

    @Test
    public void testEvaluateEachBitForAssignmentAFalseBTrueShouldBeAllFalse(){
        //given
        BitSet encodedQGram=new FixedLengthBitSet(11);
        encodedQGram.set(1); //assignment A=false and B=true
        BitSet expectedBitSet=new FixedLengthBitSet(12); // function is A&B for each bit of the 12 bits, therefore should lead to 12 bit all zeros

        //when
        HashValueEvaluator hashValueEvaluator=new HashValueEvaluator(getHost(), encodedQGram);
        BitSet actualBitSet=hashValueEvaluator.evaluateEachBit(); // all bits should be false

        //then
        assertEquals(expectedBitSet, actualBitSet);
    }

    @Test
    public void testEvaluateEachBitForAssignmentATrueBFalseShouldBeAllFalse(){
        //given
        BitSet encodedQGram=new FixedLengthBitSet(11);
        encodedQGram.set(0); //assignment A=true and B=false
        BitSet expectedBitSet=new FixedLengthBitSet(12); // function is A&B for each bit of the 12 bits, therefore should lead to 12 bit all zeros

        //when
        HashValueEvaluator hashValueEvaluator=new HashValueEvaluator(getHost(), encodedQGram);
        BitSet actualBitSet=hashValueEvaluator.evaluateEachBit(); // all bits should be false

        //then
        assertEquals(expectedBitSet, actualBitSet);
    }

    @Test
    public void testEvaluateEachBitForAssignmentATrueBTrueShouldBeAllTrue(){
        //given
        BitSet encodedQGram=new FixedLengthBitSet(11);
        encodedQGram.set(0);
        encodedQGram.set(1);//assignment A=true and B=true
        BitSet expectedBitSet=new FixedLengthBitSet(12);
        expectedBitSet.set(0, 12);// function is A&B for each bit of the 12 bits, therefore should lead to 12 bit all zeros

        //when
        HashValueEvaluator hashValueEvaluator=new HashValueEvaluator(getHost(), encodedQGram);
        BitSet actualBitSet=hashValueEvaluator.evaluateEachBit(); // all bits should be false

        //then
        assertEquals(expectedBitSet, actualBitSet);
    }



}