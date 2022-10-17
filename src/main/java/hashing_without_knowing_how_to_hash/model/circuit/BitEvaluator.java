package main.java.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.cache.WireLabelCache;
import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import org.logicng.datastructures.Assignment;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Variable;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.Arrays;
import java.util.Map;

/**
 * Is able to securely evaluate a Garbled Circuit of exactly one bit of a hash value share.
 * Fetches Wire Labels using Oblivious Transfer from host.
 * Selection of Wire Labels is represented by assignment.
 * Important step in evaluation of a whole hash value share
 * Is used by HashValueEvaluator and applied to every CircuitDto in CircuitsContainerDto
 */
public class BitEvaluator {

    private int circuitContainerHash;
    private CircuitDto circuit;
    private byte[] evaluatedWireLabel;
    private Assignment assignment;
    private HostDto hostDto;

    /**
     * Constructor, which requires
     * @param hostDto the host the circuit to evaluate had been received from and therefore the host which is sender in oblivious transfer of wire labels
     * @param circuitContainerHash is part of the address necessary to receive wire labels
     * @param circuit to be evaluated to a single bit of a hash value share
     * @param assignment of the input wire of garbled circuit; used as selection in oblivious transfer
     */
    public BitEvaluator(HostDto hostDto, int circuitContainerHash, CircuitDto circuit, Assignment assignment){
        this.hostDto=hostDto;
        this.circuitContainerHash=circuitContainerHash;
        this.circuit=circuit;
        this.assignment=assignment;
    }

    /**
     * Evaluate the garbled circuit with assignment to bit (boolean value)
     * First call is computationally more expensive than further calls due to caching of evaluated value
     * @return evaluated bit
     * @throws Exception that could be thrown during the evaluation
     */
    public boolean evaluateBit() throws Exception{
        boolean evaluatedBit=false;
        FinalGate finalGate=circuit.getFinalGate();
        if(evaluatedWireLabel==null){
            evaluatedWireLabel=evaluateFinalGate();
        }
        else{
            if(!finalGate.isLegitimate(evaluatedWireLabel)){
                throw new Exception("Evaluated value is not legitimate");
            }
        }
        evaluatedBit=finalGate.isTrue(evaluatedWireLabel);
        return evaluatedBit;
    }


    /**
     * Evaluation of final gate starts recursive evaluation of garbeld circuit
     * @return evaluated wire label of final gate
     * @throws Exception that could be thrown during the evaluation
     */
    private byte[] evaluateFinalGate() throws Exception{
        String idOfFinalGate=getIdOfFinalGate();
        byte[] finalGateWireLabel=evaluateRec(idOfFinalGate);
        return finalGateWireLabel;
    }

    private byte[] evaluateRec(String wireOrGateId) throws Exception{
        byte[] outWireLabel=null;
        if(isWireId(wireOrGateId)){
            outWireLabel=getWireLabelFromCache(wireOrGateId);
        }
        else {
            outWireLabel=evaluateChildrenGatesRec(wireOrGateId);
        }
        return outWireLabel;
    }
    private byte[] getWireLabelFromCache(String wireId) throws Exception{
        byte[] wireLabel=null;
        WireLabelCache cache=WireLabelCache.getInstance();
        WireAddressDto wireAddress=new WireAddressDto(circuitContainerHash, circuit.getCircuitHash(), wireId);
        if(cache.existsWireLabel(wireAddress)){
            wireLabel=cache.requestWireLabel(hostDto, wireAddress);}
        else {
            wireLabel=cache.cacheNewWireLabel(hostDto, wireAddress, lookUpVariableAssignment(wireId));
        }
        return wireLabel;
    }

    private byte[] evaluateChildrenGatesRec(String gateId) throws Exception{
        Map<String, String> parentAndChildren=circuit.getGateIdToChildrenGateIdsOrWireIds();
        String[] children=parentAndChildren.get(gateId).split(String.valueOf(ReservedCharacters.GATE_CHILDREN_SEPARATOR));
        byte[] leftInWireLabel=evaluateRec(children[0]);
        byte[] rightInWireLabel=evaluateRec(children[1]);
        byte[][] encodedGate=circuit.getGatesAbbreviation().getIdAndGates().get(gateId);
        byte[] outWireLabel= decodeGarbledTable(encodedGate, leftInWireLabel, rightInWireLabel);
        return outWireLabel;
    }

    private boolean isWireId(String wireOrGateId){
        boolean isWireId=false;
        try {
            Integer.parseInt(wireOrGateId);
        }
        catch (NumberFormatException e){
            isWireId=true;
        }
        return isWireId;
    }

    private String getIdOfFinalGate(){
        String idOfFinalGate=null;
        FinalGate finalGate=circuit.getFinalGate();
        Map<String,byte[][]> idAndGates=circuit.getGatesAbbreviation().getIdAndGates();

        String estimatedId=estimateFinalGateId(idAndGates);
        if(Arrays.deepEquals(idAndGates.get(estimatedId),finalGate.getEncodedGate())){
            idOfFinalGate=estimatedId;
        }
        else {
            idOfFinalGate= lookUpIdOfFinalGateIdInAbbreviationTable(idAndGates, finalGate);
        }

        return idOfFinalGate;
    }

    private String lookUpIdOfFinalGateIdInAbbreviationTable(Map<String, byte[][]> idsToEncodedGates, FinalGate finalGate){
        String finalGateId=null;
        for (String currentId : idsToEncodedGates.keySet()) {
            byte[][] currentEncodedGate=idsToEncodedGates.get(currentId);
            if (Arrays.deepEquals(currentEncodedGate,finalGate.getEncodedGate())) {
                finalGateId=currentId;
            }
        }
        return finalGateId;
    }

    private String estimateFinalGateId(Map<String, byte[][]> idsToEncodedGates){
        int finalGateIdIsProbablyLastInsertedElement=idsToEncodedGates.size();
        String estimatedId=String.valueOf(finalGateIdIsProbablyLastInsertedElement);
        return estimatedId;
    }

    private byte[] decodeGarbledTable(byte[][] encodedGate, byte[] firstInLabel, byte[] secondInLabel){
        Gate gate=new Gate(encodedGate);
        byte[] normal=null;
        byte[] reversed=null;
        try {
            normal = gate.operate(firstInLabel, secondInLabel);
            reversed = gate.operate(secondInLabel, firstInLabel);
        }catch (Exception e){
            //ignore
            // e.printStackTrace();
        }
        if(normal!=null){
            return normal;
        }
        if(reversed!=null){
            return reversed;
        }
        return new byte[Wire.AES_KEYLENGTH];
    }

    private boolean lookUpVariableAssignment(String variableId) throws Exception{
        boolean variableAssignment=false;
        FormulaFactory factory=new FormulaFactory();
        Variable variable=factory.variable(variableId);
        if(assignment.positiveVariables().contains(variable)){
            variableAssignment=true;
        }else if (assignment.negativeVariables().contains(variable)){
            variableAssignment=false;
        }else {
            throw new Exception("Variable " + variableId + " is not assigned");
        }
        return variableAssignment;
    }
}
