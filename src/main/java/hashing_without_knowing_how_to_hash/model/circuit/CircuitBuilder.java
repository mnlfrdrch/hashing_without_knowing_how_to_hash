package main.java.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.constants.GateTypeIds;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.*;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.util.GateTypeUtil;
import localhost.hashing_without_knowing_how_to_hash.util.LiteralUtil;
import org.logicng.formulas.*;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.*;

import static localhost.hashing_without_knowing_how_to_hash.constants.GateTypeIds.*;

/**
 * Builds the garbled circuits from formulas
 * InstantiationTracker keeps track of instantiated Garbled Tables and brings them in transferable format
 * Is used to generate garbled circuit and corresponding input wires
 * Is called by CircuitService
 */
public class CircuitBuilder {

    private GateInstantiationTracker tracker;

    private FormulaContainerDto formulaContainerDto;

    private FormulaFactory factory;


    public CircuitBuilder(FormulaContainerDto formulaContainerDto) {
        this.formulaContainerDto = formulaContainerDto;
        factory = new FormulaFactory();
    }

    /**
     * Builds all formulas in given FormulaContainerDto recursively
     * Is called by CircuitService every time, a new garbled circuit needs to be transferred
     * Produces the sharable garbled circuits for each bit and the corresponding input wires
     * The garbled circuit representation ans the corresponding input wire can be separated easily
     * @return a CircuitsAndWiresContainer containing transferable CircuitsContainerDto and corresponding Wires to all individual Circuits
     */
    public CircuitsAndWiresContainerDto buildAll(){
        List<CircuitDto> circuits=new LinkedList<>();
        List<NamedWiresContainerDto> inputWires=new LinkedList<>();
        List<Formula> formulaList= formulaContainerDto.getFormulaList();
        formulaList.stream().forEach((Formula f)->{
            CircuitDto c=buildCircuit(f);circuits.add(c);
            inputWires.add(getNamedWires(c.getCircuitHash()));
        });
        CircuitsContainerDto circuitsContainerDto =new CircuitsContainerDto(circuits);
        return new CircuitsAndWiresContainerDto(circuitsContainerDto, inputWires);
    }

    private NamedWiresContainerDto getNamedWires(int circuitHash){
        Map<String, Wire> wires=tracker.getGateChildrenMapping().getEncoding().getWireEncoding().getInputWires();
        return new NamedWiresContainerDto(wires, circuitHash);
    }

    private CircuitDto buildCircuit(Formula formula){
        CircuitDto circuit=null;
        Wire wireOutOfFinalGate=null;
        FinalGate finalGate=null;
        handleInstantiationOfTrackerForGivenFormula(formula);
        wireOutOfFinalGate=handleBuildCircuitRecCallForGivenFormula(formula);
        finalGate=instantiateFinalGateIfNoErrorOccurred(wireOutOfFinalGate);

        circuit=instantiateCircuitFromCollectedFormulaData(finalGate);
        return circuit;
    }

    private CircuitDto instantiateCircuitFromCollectedFormulaData(FinalGate finalGate){
        CircuitDto circuit=null;

        Map<String, String> circuitMapping=readMappingParentToChildrenFromTracker();
        GatesEncodingContainerDto gatesEncodingContainerDto=readGatesEncodingFromTracker();
        circuit=new CircuitDto(finalGate, circuitMapping, gatesEncodingContainerDto);

        return circuit;
    }

    private GatesEncodingContainerDto readGatesEncodingFromTracker(){
        GatesEncodingContainerDto transferableFormatOfGatesAbbreviation=null;
        GateChildrenMapping parentGateMappedToItsChildrenGateOrWires=tracker.getGateChildrenMapping();
        CircuitEncoding abbreviationOfGatesAndInputWires=parentGateMappedToItsChildrenGateOrWires.getEncoding();
        GateEncoding onlyAbbreviationOfGates=abbreviationOfGatesAndInputWires.getGateEncoding();
        transferableFormatOfGatesAbbreviation=onlyAbbreviationOfGates.getGatesEncodingContainer();

        return transferableFormatOfGatesAbbreviation;
    }
    private Map<String, String> readMappingParentToChildrenFromTracker(){
        Map<String, String> circuitAbbreviationMapping=null;

        GateChildrenMapping gateChildrenMapping=tracker.getGateChildrenMapping();
        circuitAbbreviationMapping=gateChildrenMapping.getChildrenToParentGate();

        if(circuitAbbreviationMapping==null){
            circuitAbbreviationMapping=new HashMap<>();
        }

        return circuitAbbreviationMapping;
    }

    private void handleInstantiationOfTrackerForGivenFormula(Formula formula){
        try {
            tracker = new GateInstantiationTracker(formula.literals());
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private Wire handleBuildCircuitRecCallForGivenFormula(Formula formula){
        Wire out=null;
        try{
            out = buildCircuitRec(formula);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }

    private FinalGate instantiateFinalGateIfNoErrorOccurred(Wire outWire){
        FinalGate finalGate=null;
        if(outWire!=null){
            Gate lastProcessedGate=tracker.getOutWireGateMapping().getGateTo(outWire);
            finalGate=new FinalGate(lastProcessedGate,outWire);
        }
        return finalGate;
    }

    private List<Formula> formulaToList(Formula formula){
        List subFormulas=new LinkedList();
        for(Formula sub: formula){
            subFormulas.add(sub);
        }
        return subFormulas;
    }

    private Wire buildCircuitRec(Formula formula) throws Exception{
        Wire outWire=null;
        //basisfall
        if(LiteralUtil.isLiteral(formula)){
            Literal literal=castFormulaWhichIsAlsoLiteralToActualTypeLiteral(formula);
            outWire=findFittingInputWireToLiteral(literal);
        }
        else{
            outWire=buildChildrenGatesToGivenFormulaRec(formula);
        }

        return outWire;
    }

    private Wire buildChildrenGatesToGivenFormulaRec(Formula subFormula) throws Exception{
        Wire outWireOfThisFormulaPart=null;
        if(GateTypeUtil.isNegation(subFormula)){
            outWireOfThisFormulaPart=buildCircuitRecursiveNegationCase(subFormula);
        }else{
            outWireOfThisFormulaPart=buildCircuitRecursiveAffirmationCase(subFormula);
        }
        return outWireOfThisFormulaPart;
    }

    private Literal castFormulaWhichIsAlsoLiteralToActualTypeLiteral(Formula formulaWhichOnlyConsistsOfALiteral) throws ClassCastException{
        Literal literal=null;
        if(LiteralUtil.isLiteral(formulaWhichOnlyConsistsOfALiteral)){
            literal=(Literal) formulaWhichOnlyConsistsOfALiteral;
        }
        else {
            literal=factory.literal("A", false);
            throw new ClassCastException("Formula '" + formulaWhichOnlyConsistsOfALiteral.toString() + "' of type Formula.class is not of type Literal.class");
        }
        return literal;
    }

    private Wire findFittingInputWireToLiteral(Literal literal) throws Exception{
        Wire outWire=null;
        if(LiteralUtil.isNegatedLiteral(literal)){
            outWire= counterNegateNegatedInputWireLiteral(literal);
        }
        else {
            String wireId=literal.toString();
            outWire = findInputWireToPositiveLiteral(wireId);//inputWires.get(literalString);
        }
        return outWire;
    }

    private Wire findInputWireToPositiveLiteral(String wireId) throws Exception{
        Wire inputWire=null;
        CircuitEncoding containerOfIdsToInputWires=tracker.getGateChildrenMapping().getEncoding();
        inputWire=containerOfIdsToInputWires.getWire(wireId);

        return inputWire;
    }

    private Wire counterNegateNegatedInputWireLiteral(Literal negativeLiteral) throws Exception{
        Wire wireOutOfNegation=null;
        String literalString=negativeLiteral.toString();
        String wireId=LiteralUtil.extractVariable(literalString);
        Wire in=findInputWireToPositiveLiteral(wireId);
        List<Wire> bothWiresTheSame=new ArrayList<>();
        bothWiresTheSame.add(in);
        bothWiresTheSame.add(in);
        wireOutOfNegation=connectOperandsWithNor(bothWiresTheSame);
        return wireOutOfNegation;
    }

    private Wire buildCircuitRecursiveNegationCase(Formula formula) throws Exception {
        Wire outWire=null;
        List<Formula> opList=formulaToList(GateTypeUtil.getFormulaInsideNegation(formula));//.stream().toList();
        List<Wire> opWires=recursiveCallOfSubFormulas(opList);
        if(GateTypeUtil.isNand(formula)){
            outWire=connectOperandsWithNand(opWires);
        }
        else if(GateTypeUtil.isNor(formula)){
            outWire=connectOperandsWithNor(opWires);
        }
        else{
            throw new Exception("Formula " + formula.toString() + " classified as negation, but is neither NAND nor NOR");
        }
        return outWire;
    }

    private Wire buildCircuitRecursiveAffirmationCase(Formula formula) throws Exception{
        Wire outWire=null;
        List<Formula> opList=formulaToList(formula);//.stream().toList();
        List<Wire> opWires=recursiveCallOfSubFormulas(opList);
        if(GateTypeUtil.isAnd(formula)){
            outWire=connectOperandsWithAnd(opWires);
        }
        else if(GateTypeUtil.isOr(formula)){
            outWire=connectOperandsWithOr(opWires);
        }
        else {
            throw new Exception("Formula " + formula.toString() + " classified as non-negation, but is neither AND nor OR");
        }
        return outWire;
    }

    private List<Wire> recursiveCallOfSubFormulas(List<Formula> subFormulas){
        //rekursiver Aufruf
        List<Wire> opWires=new ArrayList<>();
        subFormulas.forEach((Formula op)->{try{
            opWires.add(buildCircuitRec(op));
        }
        catch (Exception e){e.printStackTrace();}});
        return opWires;
    }

    private void setGate(GateTypeIds type, Wire leftIn, Wire rightIn, Wire out){
        Gate g=tracker.instantiateGate(type, leftIn, rightIn, out);
    }

    private Wire connectOperandsWithGate(List<Wire> wireList, GateTypeIds typeImmediate, GateTypeIds typeNextStep) throws Exception{
        Wire out = null;
        Wire leftWire = null;
        Wire rightWire = null;
        if(wireList==null){
            throw new NullPointerException("List is null");
        }
        if(wireList.size()==0){
            throw new IllegalArgumentException("List is empty");
        }
        int wlSize = wireList.size();
        if (wlSize == 1) {
            out = wireList.get(0);
        } else {
            out=connectAtLeastTwoWiresWithGate(wireList,typeImmediate,typeNextStep);
        }
        return out;
    }

    private Wire connectAtLeastTwoWiresWithGate(List<Wire> wireList, GateTypeIds typeImmediate, GateTypeIds typeNextStep) throws Exception{
        Wire out = new Wire();
        Wire leftWire=null;
        Wire rightWire=null;
        if (wireList.size() == 2) {
            leftWire = wireList.get(0);
            rightWire = wireList.get(1);
        }else{
            SplittableListContainerDto<Wire> slc = new SplittableListContainerDto<Wire>(wireList);
            leftWire = connectOperandsWithGate(slc.leftHalfOfList(), typeNextStep, typeNextStep);
            rightWire = connectOperandsWithGate(slc.rightHalfOfList(), typeNextStep, typeNextStep);
        }
        setGate(typeImmediate, leftWire, rightWire, out);
        return out;
    }


    private Wire connectOperandsWithNand(List<Wire> wireList) throws Exception{
        return connectOperandsWithGate(wireList, NAND, AND);
    }

    private Wire connectOperandsWithAnd(List<Wire> wireList) throws Exception{
        return connectOperandsWithGate(wireList, AND, AND);
    }

    private Wire connectOperandsWithNor(List<Wire> wireList) throws Exception{
        return connectOperandsWithGate(wireList, NOR, OR);
    }

    private Wire connectOperandsWithOr(List<Wire> wireList) throws Exception{
        return connectOperandsWithGate(wireList, OR, OR);
    }
}