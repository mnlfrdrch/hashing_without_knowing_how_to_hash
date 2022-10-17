package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.cache.WireCache;
import localhost.hashing_without_knowing_how_to_hash.cache.WireLabelCache;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsAndWiresContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import mockdata.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.logicng.formulas.Formula;
import org.springframework.test.util.ReflectionTestUtils;
import yao.gate.Wire;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BitEvaluatorTest {

    private BitEvaluator bitEvaluator;

    private CircuitDto circuit;
    private Map<String, Wire> wires;
    private int circuitsContainerHash;

    @BeforeEach
    public void setUp(){
        WireCache.getInstance();
    }

    @AfterEach
    public void tearDown(){
        ReflectionTestUtils.setField(WireLabelCache.getInstance(), "wireAddressToWireLabel", new HashMap<>());
        bitEvaluator=null;
        circuit=null;
        wires=null;
        circuitsContainerHash=-1;
    }

    private void loadGarbledCircuitForFormula(Formula formula){
        CircuitsAndWiresContainerDto circuitsAndWires= FormulaTestData.generateCircuitAndWiresFromFormula(formula);
        circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().get(0);
        wires=circuitsAndWires.getNamedWiresContainerDtoList().get(0).getWireIdToWire();
        circuitsContainerHash=circuitsAndWires.getCircuitsContainerDto().getCircuitsContainerHash();
    }

    private void setAssignmentToWireLabelCache(boolean assignmentA, boolean assignmentB){
        Map<String, byte[]> wireLabels=new HashMap<>();
        WireAddressDto addressA=new WireAddressDto(circuitsContainerHash, circuit.getCircuitHash(), "A");
        WireAddressDto addressB=new WireAddressDto(circuitsContainerHash, circuit.getCircuitHash(), "B");
        wireLabels.put(addressA.toString(), getLabelA(assignmentA));
        wireLabels.put(addressB.toString(), getLabelB(assignmentB));
        ReflectionTestUtils.setField(WireLabelCache.getInstance(), "wireAddressToWireLabel", wireLabels);
    }

    private byte[] getLabelA(boolean assignment){
        return getGeneralLabel("A", assignment);
    }

    private byte[] getLabelB(boolean assignment){
        return getGeneralLabel("B", assignment);
    }

    private byte[] getGeneralLabel(String id, boolean assignment){
        if(assignment){
            return wires.get(id).getValue1();
        }
        else {
            return wires.get(id).getValue0();
        }
    }

    private void loadFormulaCacheWithFormula(Formula formula){
        FormulaCache cache=FormulaCache.getInstance();
        List<Formula> formulas=new ArrayList<>();
        formulas.add(formula);
        FormulaContainerDto formulaContainerDto=new FormulaContainerDto(formulas);
        cache.setFormulaContainerDto(formulaContainerDto);
    }

    private void initBitEvaluatorTest(Formula formula, boolean assignmentA, boolean assignmentB){
        loadGarbledCircuitForFormula(formula);
        setAssignmentToWireLabelCache(assignmentA, assignmentB);
        bitEvaluator=new BitEvaluator(HostDtoMockData.mockHost(), circuitsContainerHash, circuit, AssignmentTestData.getAssignmentAxBy(assignmentA, assignmentB));
        loadFormulaCacheWithFormula(FormulaTestData.getConjunctionFormula());
    }

    @Test
    public void testEvaluateBitShouldBe0For0AND0() throws Exception{
        //given
        boolean assignmentA=false;
        boolean assignmentB=false;
        Formula conjunction= FormulaTestData.getConjunctionFormula();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe0For0AND1() throws Exception{
        //given
        boolean assignmentA=false;
        boolean assignmentB=true;
        Formula conjunction= FormulaTestData.getConjunctionFormula();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe0For1AND0() throws Exception{
        //given
        boolean assignmentA=true;
        boolean assignmentB=false;
        Formula conjunction= FormulaTestData.getConjunctionFormula();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe1For1AND1() throws Exception{
        //given
        boolean assignmentA=true;
        boolean assignmentB=true;
        Formula conjunction= FormulaTestData.getConjunctionFormula();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertTrue(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe0For0COMPLEX0() throws Exception{
        //given
        boolean assignmentA=false;
        boolean assignmentB=false;
        Formula conjunction= FormulaTestData.getComplexFormula();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe0For0COMPLEX1() throws Exception{
        //given
        boolean assignmentA=false;
        boolean assignmentB=true;
        Formula conjunction= FormulaTestData.getComplexFormula();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe1For1COMPLEX0() throws Exception{
        //given
        boolean assignmentA=true;
        boolean assignmentB=false;
        Formula conjunction= FormulaTestData.getComplexFormula();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertTrue(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe0For1COMPLEX1() throws Exception{
        //given
        boolean assignmentA=true;
        boolean assignmentB=true;
        Formula conjunction= FormulaTestData.getComplexFormula();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe1For0NOR0() throws Exception{
        //given
        boolean assignmentA=false;
        boolean assignmentB=false;
        Formula conjunction= FormulaTestData.getNOR();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertTrue(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe0For0NOR1() throws Exception{
        //given
        boolean assignmentA=false;
        boolean assignmentB=true;
        Formula conjunction= FormulaTestData.getNOR();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe0For1NOR0() throws Exception{
        //given
        boolean assignmentA=true;
        boolean assignmentB=false;
        Formula conjunction= FormulaTestData.getNOR();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }

    @Test
    public void testEvaluateBitShouldBe0For1NOR1() throws Exception{
        //given
        boolean assignmentA=true;
        boolean assignmentB=true;
        Formula conjunction= FormulaTestData.getNOR();
        initBitEvaluatorTest(conjunction, assignmentA, assignmentB);

        //when
        boolean evaluatedBit=bitEvaluator.evaluateBit();

        //then
        assertFalse(evaluatedBit);
    }
}