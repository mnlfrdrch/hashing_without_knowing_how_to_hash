package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.util.LiteralUtil;
import lombok.Getter;
import org.logicng.formulas.Literal;
import yao.gate.Wire;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Generates the input wires for an individual garbled circuit
 * Holds the pairs of wire instance and wire id, which are then request able by getId() and getWire()
 * An input wire id is a string of alphabetical uppercase letters like 'A', 'F', 'Z' or 'AC'
 */
public class WireEncoding {

    @Getter
    private Map<String, Wire> inputWires;

    /**
     * The constructor requires all literals used in the logic formula, which serves as the babsis for the garbled circuit
     * @param literalsInFormula the set of all literals used the formula
     */
    public WireEncoding(Set<Literal> literalsInFormula){
        inputWires=new HashMap<>();
        genInputWires(literalsInFormula);
    }

    /**
     * Requests the id to a given input wire instance
     * An input wire id is a string of alphabetical uppercase letters like 'A', 'F', 'Z' or 'AC'
     * @param wire the input wire instance
     * @return id of the wire if wire is actually an input wire, else *null*
     */
    public String getId(Wire wire){
        return reverseInputWireMap().get(wire);
    }

    /**
     * Requests the input wire instance to a wire id
     * An input wire id is a string of alphabetical uppercase letters like 'A', 'F', 'Z' or 'AC'
     * @param id the id of the requested input wire
     * @return the instance of the input wire or *null* if the id was invalid
     */
    public Wire getWire(String id){
        return inputWires.get(id);
    }

    private void genInputWires(Set<Literal> literalSet) {
        literalSet.stream()
                .filter(literal -> filterOutAlreadyRegisteredLiterals(literal))
                .forEach(literal -> saveNewWireForLiteral(literal));
    }

    private void saveNewWireForLiteral(Literal literal){
        try {
            String wireId=forceAnyLiteralPositiveWireId(literal);
            inputWires.put(wireId, new Wire());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean filterOutAlreadyRegisteredLiterals(Literal literal){
        return !inputWires.containsKey(literal.toString());
    }

    private String forceAnyLiteralPositiveWireId(Literal positiveOrNegativeLiteral){
        String positiveWireId=null;
        if(LiteralUtil.isNegatedLiteral(positiveOrNegativeLiteral)){
            positiveWireId=LiteralUtil.extractVariable(positiveOrNegativeLiteral.toString());
        }else {
            positiveWireId=positiveOrNegativeLiteral.toString();
        }
        return positiveWireId;
    }

    private Map<Wire, String> reverseInputWireMap(){
        Map<Wire, String> reverseMap=new HashMap<>();
        for(String s: inputWires.keySet()){
            Wire w=inputWires.get(s);
            reverseMap.put(w,s);
        }
        return reverseMap;
    }

}
