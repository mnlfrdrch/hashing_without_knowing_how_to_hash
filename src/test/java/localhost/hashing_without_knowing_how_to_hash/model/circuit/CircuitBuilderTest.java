package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsAndWiresContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.NamedWiresContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import mockdata.FormulaTestData;
import org.junit.jupiter.api.AfterEach;
import yao.gate.Gate;
import yao.gate.Wire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.logicng.datastructures.Assignment;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.io.parsers.PropositionalParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CircuitBuilderTest {

    private CircuitBuilder circuitBuilder;


    private CircuitDto getFirstCircuit(CircuitsAndWiresContainerDto circuitsAndWiresContainerDto){
        return circuitsAndWiresContainerDto.getCircuitsContainerDto().getCircuits().iterator().next();
    }

    private Wire getWireFromFirstCircuitByWireId(CircuitsAndWiresContainerDto circuitsAndWiresContainer, String wireId){
        Wire wire=null;
        NamedWiresContainerDto namedWiresContainer=circuitsAndWiresContainer.getNamedWiresContainerDtoList().iterator().next();
        Map<String, Wire> idToWire=namedWiresContainer.getWireIdToWire();
        wire=idToWire.get(wireId);
        return wire;
    }

    private Gate getGateFromCircuitByGateAbbreviation(CircuitDto circuit, String gateId){
        Gate gate=null;
        Map<String, byte[][]> idAndEncodedGates=circuit.getGatesAbbreviation().getIdAndGates();
        byte[][] encodedGate=idAndEncodedGates.get(gateId);
        gate=new Gate(encodedGate);
        return gate;
    }

    private boolean evaluateGateOfFirstCircuit(CircuitsAndWiresContainerDto circuitsAndWiresContainer, String gateId, String leftWireId, boolean leftWireValue, String rightWireId, boolean rightWireValue){
        CircuitDto circuit=getFirstCircuit(circuitsAndWiresContainer);
        Gate gate=getGateFromCircuitByGateAbbreviation(circuit,gateId);
        FinalGate finalGate=circuit.getFinalGate();
        Wire leftWire=getWireFromFirstCircuitByWireId(circuitsAndWiresContainer, leftWireId);
        Wire rightWire=getWireFromFirstCircuitByWireId(circuitsAndWiresContainer, rightWireId);

        byte[] leftWireLabel=leftWireValue ? leftWire.getValue1() : leftWire.getValue0();
        byte[] rightWireLabel=rightWireValue ? rightWire.getValue1() : rightWire.getValue0();

        byte[] outWireLabel=null;
        try{
            byte[] normalPos=gate.operate(leftWireLabel,rightWireLabel);
            byte[] reversedPos=gate.operate(rightWireLabel,leftWireLabel);
            outWireLabel=(normalPos!=null) ? normalPos : reversedPos;
        }
        catch (Exception e){
            fail(e.getMessage());
        }

        return finalGate.isTrue(outWireLabel);
    }

    @BeforeEach
    public void setUp(){
        circuitBuilder=null;
    }

    @Test
    public void testBuildAllForNotOperatorShouldNotBeNull(){
        //given
        Formula notA= FormulaTestData.getNegationFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(notA)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        assertNotNull(circuitsAndWires);

    }

    @Test
    public void testBuildAllForNotOperatorShouldContainASingleGateAbbreviation(){
        //given
        Formula notA= FormulaTestData.getNegationFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(notA)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,byte[][]> gateAbbreviations=circuit.getGatesAbbreviation().getIdAndGates();
        assertEquals(1,gateAbbreviations.size());
    }

    @Test
    public void testBuildAllForNotOperatorGateAbbreviationShouldBe1(){
        //given
        Formula notA= FormulaTestData.getNegationFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(notA)));
        String expectedGateAbbreviation="1";

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,byte[][]> gateAbbreviations=circuit.getGatesAbbreviation().getIdAndGates();
        String actualGateAbbreviation=List.copyOf(gateAbbreviations.keySet()).iterator().next();
        assertEquals(expectedGateAbbreviation,actualGateAbbreviation);
    }

    @Test
    public void testBuildAllForNotOperatorNumOfGateToChildrenMappingsShouldBe1(){
        //given
        Formula notA= FormulaTestData.getNegationFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(notA)));
        int expectedNumOfGateToChildrenMappings=1; // only mapping 1 -> AßA , because ~A <=> A NAND A

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,String> gateToChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        int actualNumOfGateToChildrenMapping=gateToChildren.size();
        assertEquals(expectedNumOfGateToChildrenMappings,actualNumOfGateToChildrenMapping);
    }

    @Test
    public void testBuildAllForNotOperatorTheOnlyGateToChildrenMappingsShouldBe1toAßA(){
        //given
        Formula notA= FormulaTestData.getNegationFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(notA)));
        String gateId="1";
        String expectedGateToChildrenMappingValue="A"+ReservedCharacters.GATE_CHILDREN_SEPARATOR+"A"; // mapping should be 1 -> AßA

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,String> gateToChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        String actualGateToChildrenMappingValue=gateToChildren.get(gateId);
        assertEquals(expectedGateToChildrenMappingValue,actualGateToChildrenMappingValue);
    }


    @Test
    public void testBuildAllForNotOperatorEvaluatedShouldBeTrueForAIsFalse(){
        //given
        Formula notA= FormulaTestData.getNegationFormula();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=false;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(notA)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeTrue=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireAId, wireAPhase);

        assertTrue(evaluatedValueShouldBeTrue);
    }

    @Test
    public void testBuildAllForNotOperatorEvaluatedShouldBeFalseForAIsTrue() {
        //given
        Formula notA= FormulaTestData.getNegationFormula();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=true;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(notA)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeFalse=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireAId, wireAPhase);

        assertFalse(evaluatedValueShouldBeFalse);
    }


    @Test
    public void testBuildAllForAndGateShouldNotBeNull(){
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        assertNotNull(circuitsAndWires);

    }

    @Test
    public void testBuildAllForAndGateShouldContainASingleGateAbbreviation(){
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,byte[][]> gateAbbreviations=circuit.getGatesAbbreviation().getIdAndGates();
        assertEquals(1,gateAbbreviations.size());
    }

    @Test
    public void testBuildAllForAndGateGateAbbreviationShouldBe1(){
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));
        String expectedGateAbbreviation="1";

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,byte[][]> gateAbbreviations=circuit.getGatesAbbreviation().getIdAndGates();
        String actualGateAbbreviation=List.copyOf(gateAbbreviations.keySet()).iterator().next();
        assertEquals(expectedGateAbbreviation,actualGateAbbreviation);
    }

    @Test
    public void testBuildAllForAndGateNumOfGateToChildrenMappingsShouldBe1(){
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));
        int expectedNumOfGateToChildrenMappings=1;

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,String> gateToChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        int actualNumOfGateToChildrenMapping=gateToChildren.size();
        assertEquals(expectedNumOfGateToChildrenMappings,actualNumOfGateToChildrenMapping);
    }

    @Test
    public void testBuildAllForAndGateTheOnlyGateToChildrenMappingsShouldBe1ToAßB(){
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));
        String gateId="1";
        String expectedGateToChildrenMappingValue="A"+ReservedCharacters.GATE_CHILDREN_SEPARATOR+"B";

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,String> gateToChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        String actualGateToChildrenMappingValue=gateToChildren.get(gateId);
        assertEquals(expectedGateToChildrenMappingValue,actualGateToChildrenMappingValue);
    }


    @Test
    public void testBuildAllForAndGateEvaluatedShouldBeFalseForAIsFalseAndBIsFalse(){
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=false;
        String wireBId="B";
        boolean wireBPhase=false;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeFalse=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertFalse(evaluatedValueShouldBeFalse);
    }

    @Test
    public void testBuildAllForAndGateEvaluatedShouldBeFalseForAIsFalseAndBIsTrue() {
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=false;
        String wireBId="B";
        boolean wireBPhase=true;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeFalse=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertFalse(evaluatedValueShouldBeFalse);
    }

    @Test
    public void testBuildAllForAndGateEvaluatedShouldBeFalseForAIsTrueAndBIsFalse() {
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=true;
        String wireBId="B";
        boolean wireBPhase=false;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeFalse=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertFalse(evaluatedValueShouldBeFalse);
    }

    @Test
    public void testBuildAllForAndGateEvaluatedShouldBeTrueForAIsTrueAndBIsTrue() {
        //given
        Formula aAndB= FormulaTestData.getConjunctionFormula();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=true;
        String wireBId="B";
        boolean wireBPhase=true;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aAndB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeTrue=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertTrue(evaluatedValueShouldBeTrue);
    }

    @Test
    public void testBuildAllForNandGateShouldNotBeNull(){
        //given
        Formula aNandB= FormulaTestData.getNAND();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        assertNotNull(circuitsAndWires);

    }

    @Test
    public void testBuildAllForNandGateShouldContainASingleGateAbbreviation(){
        //given
        Formula aNandB= FormulaTestData.getNAND();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,byte[][]> gateAbbreviations=circuit.getGatesAbbreviation().getIdAndGates();
        assertEquals(1,gateAbbreviations.size());
    }

    @Test
    public void testBuildAllForNandGateGateAbbreviationShouldBe1(){
        //given
        Formula aNandB= FormulaTestData.getNAND();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));
        String expectedGateAbbreviation="1";

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,byte[][]> gateAbbreviations=circuit.getGatesAbbreviation().getIdAndGates();
        String actualGateAbbreviation=List.copyOf(gateAbbreviations.keySet()).iterator().next();
        assertEquals(expectedGateAbbreviation,actualGateAbbreviation);
    }

    @Test
    public void testBuildAllForNandGateNumOfGateToChildrenMappingsShouldBe1(){
        //given
        Formula aNandB= FormulaTestData.getNAND();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));
        int expectedNumOfGateToChildrenMappings=1;

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,String> gateToChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        int actualNumOfGateToChildrenMapping=gateToChildren.size();
        assertEquals(expectedNumOfGateToChildrenMappings,actualNumOfGateToChildrenMapping);
    }

    @Test
    public void testBuildAllForNandGateTheOnlyGateToChildrenMappingsShouldBe1ToAßB(){
        //given
        Formula aNandB= FormulaTestData.getNAND();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));
        String gateId="1";
        String expectedGateToChildrenMappingValue="A"+ReservedCharacters.GATE_CHILDREN_SEPARATOR+"B";

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,String> gateToChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        String actualGateToChildrenMappingValue=gateToChildren.get(gateId);
        assertEquals(expectedGateToChildrenMappingValue,actualGateToChildrenMappingValue);
    }


    @Test
    public void testBuildAllForNandGateEvaluatedShouldBeTrueForAIsFalseAndBIsFalse(){
        //given
        Formula aNandB= FormulaTestData.getNAND();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=false;
        String wireBId="B";
        boolean wireBPhase=false;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeTrue=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertTrue(evaluatedValueShouldBeTrue);
    }

    @Test
    public void testBuildAllForAndGateEvaluatedShouldBeTrueForAIsFalseAndBIsTrue() {
        //given
        Formula aNandB= FormulaTestData.getNAND();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=false;
        String wireBId="B";
        boolean wireBPhase=true;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeTrue=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertTrue(evaluatedValueShouldBeTrue);
    }

    @Test
    public void testBuildAllForNandGateEvaluatedShouldBeTrueForAIsTrueAndBIsFalse() {
        //given
        Formula aNandB= FormulaTestData.getNAND();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=true;
        String wireBId="B";
        boolean wireBPhase=false;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeTrue=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertTrue(evaluatedValueShouldBeTrue);
    }

    @Test
    public void testBuildAllForNandGateEvaluatedShouldBeFalseForAIsTrueAndBIsTrue() {
        //given
        Formula aNandB= FormulaTestData.getNAND();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=true;
        String wireBId="B";
        boolean wireBPhase=true;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNandB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeFalse=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertFalse(evaluatedValueShouldBeFalse);
    }

    @Test
    public void testCircuitBuilderBuildsNorGateCorrect(){
        //given
        Formula aNorB=FormulaTestData.getNOR();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));
        String expectedId="1";
        String expectedChildren="A"+ ReservedCharacters.GATE_CHILDREN_SEPARATOR+"B";

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().get(0);

        Map<String, Wire> idAndWire=circuitsAndWires.getNamedWiresContainerDtoList().get(0).getWireIdToWire();
        Wire wireA=idAndWire.get("A");
        Wire wireB=idAndWire.get("B");

        Map<String, byte[][]> idAndGate=circuit.getGatesAbbreviation().getIdAndGates();
        int actualIdAnGateSize=idAndGate.size();
        int expectedIdAndGateSize=1;

        String actualId=List.copyOf(idAndGate.keySet()).get(0);

        String actualChildren=circuit.getGateIdToChildrenGateIdsOrWireIds().get(actualId);

        Gate shouldBeNorGate=new Gate(idAndGate.get(actualId));
        FinalGate finalGate=circuit.getFinalGate();

        boolean aFalseNorBFalseShouldBeTrue=false;
        boolean aFalseNorBTrueShouldBeFalse=true;
        boolean aTrueNorBFalseShouldBeFalse=true;
        boolean aTrueNorBTrueShouldBeFalse=true;

        try{
            byte[] wl0And0=shouldBeNorGate.operate(wireB.getValue0(),wireA.getValue0());
            byte[] wl0And1=shouldBeNorGate.operate(wireB.getValue0(),wireA.getValue1());
            byte[] wl1And0=shouldBeNorGate.operate(wireB.getValue1(),wireA.getValue0());
            byte[] wl1And1=shouldBeNorGate.operate(wireB.getValue1(),wireA.getValue1());
            aFalseNorBFalseShouldBeTrue=finalGate.isTrue(wl0And0);
            aFalseNorBTrueShouldBeFalse=finalGate.isTrue(wl0And1);
            aTrueNorBFalseShouldBeFalse=finalGate.isTrue(wl1And0);
            aTrueNorBTrueShouldBeFalse=finalGate.isTrue(wl1And1);
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }

        assertEquals(expectedIdAndGateSize, actualIdAnGateSize);
        assertEquals(expectedId, actualId);
        assertEquals(expectedChildren, actualChildren);

        assertTrue(aFalseNorBFalseShouldBeTrue);
        assertFalse(aFalseNorBTrueShouldBeFalse);
        assertFalse(aTrueNorBFalseShouldBeFalse);
        assertFalse(aTrueNorBTrueShouldBeFalse);
    }

    @Test
    public void testBuildAllForNorGateShouldNotBeNull(){
        //given
        Formula aNorB= FormulaTestData.getNOR();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        assertNotNull(circuitsAndWires);

    }

    @Test
    public void testBuildAllForNorGateShouldContainASingleGateAbbreviation(){
        //given
        Formula aNorB=FormulaTestData.getNOR();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,byte[][]> gateAbbreviations=circuit.getGatesAbbreviation().getIdAndGates();
        assertEquals(1,gateAbbreviations.size());
    }

    @Test
    public void testBuildAllForNorGateGateAbbreviationShouldBe1(){
        //given
        Formula aNorB=FormulaTestData.getNOR();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));
        String expectedGateAbbreviation="1";

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,byte[][]> gateAbbreviations=circuit.getGatesAbbreviation().getIdAndGates();
        String actualGateAbbreviation=List.copyOf(gateAbbreviations.keySet()).iterator().next();
        assertEquals(expectedGateAbbreviation,actualGateAbbreviation);
    }

    @Test
    public void testBuildAllForNorGateNumOfGateToChildrenMappingsShouldBe1(){
        //given
        Formula aNorB=FormulaTestData.getNOR();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));
        int expectedNumOfGateToChildrenMappings=1;

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,String> gateToChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        int actualNumOfGateToChildrenMapping=gateToChildren.size();
        assertEquals(expectedNumOfGateToChildrenMappings,actualNumOfGateToChildrenMapping);
    }

    @Test
    public void testBuildAllForNorGateTheOnlyGateToChildrenMappingsShouldBe1ToAßB(){
        //given
        Formula aNorB=FormulaTestData.getNOR();
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));
        String gateId="1";
        String expectedGateToChildrenMappingValue="A"+ReservedCharacters.GATE_CHILDREN_SEPARATOR+"B";

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        CircuitDto circuit=circuitsAndWires.getCircuitsContainerDto().getCircuits().iterator().next();
        Map<String,String> gateToChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        String actualGateToChildrenMappingValue=gateToChildren.get(gateId);
        assertEquals(expectedGateToChildrenMappingValue,actualGateToChildrenMappingValue);
    }


    @Test
    public void testBuildAllForNorGateEvaluatedShouldBeTrueForAIsFalseAndBIsFalse(){
        //given
        Formula aNorB=FormulaTestData.getNOR();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=false;
        String wireBId="B";
        boolean wireBPhase=false;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeTrue=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertTrue(evaluatedValueShouldBeTrue);
    }

    @Test
    public void testBuildAllForNorGateEvaluatedShouldBeFalseForAIsFalseAndBIsTrue() {
        //given
        Formula aNorB=FormulaTestData.getNOR();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=false;
        String wireBId="B";
        boolean wireBPhase=true;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeFalse=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertFalse(evaluatedValueShouldBeFalse);
    }

    @Test
    public void testBuildAllForNorGateEvaluatedShouldBeFalseForAIsTrueAndBIsFalse() {
        //given
        Formula aNorB=FormulaTestData.getNOR();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=true;
        String wireBId="B";
        boolean wireBPhase=false;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeFalse=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertFalse(evaluatedValueShouldBeFalse);
    }

    @Test
    public void testBuildAllForNorGateEvaluatedShouldBeFalseForAIsTrueAndBIsTrue() {
        //given
        Formula aNorB=FormulaTestData.getNOR();
        String gateId="1";
        String wireAId="A";
        boolean wireAPhase=true;
        String wireBId="B";
        boolean wireBPhase=true;
        circuitBuilder=new CircuitBuilder(new FormulaContainerDto(List.of(aNorB)));

        //when
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();

        //then
        boolean evaluatedValueShouldBeFalse=evaluateGateOfFirstCircuit(circuitsAndWires, gateId, wireAId, wireAPhase, wireBId, wireBPhase);

        assertFalse(evaluatedValueShouldBeFalse);
    }

}