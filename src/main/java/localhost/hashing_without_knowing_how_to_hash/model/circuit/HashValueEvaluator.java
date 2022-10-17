package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.dto.FixedLengthBitSet;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.LiteralNameAllocator;
import localhost.hashing_without_knowing_how_to_hash.util.HttpUtil;
import org.logicng.datastructures.Assignment;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;

import java.util.BitSet;
import java.util.List;

/**
 * Evaluates a single hash value (share) of multiple bits using a BitEvaluator per bit
 * Therefore computes a value h_{host}(qgram)
 * Is used by the HashingProtocol to securely evaluate alle the required hash value shares
 */
public class HashValueEvaluator {

    private HostDto host;
    private BitSet encodedQGram;


    public HashValueEvaluator(HostDto host, BitSet encodedQGram){
        this.host=host;
        this.encodedQGram = encodedQGram;
    }

    /**
     * Evaluates requested hash value share BitSet for host and gram
     * @return hash value share h_{host}(qgram) as BitSet
     */
    public BitSet evaluateEachBit(){
        Assignment assignment=generateAssignmentBitSet();

        CircuitsContainerDto circuitsContainer= HttpUtil.httpGetCircuitsContainerDto(host);
        int circuitsContainerHash=circuitsContainer.getCircuitsContainerHash();
        List<CircuitDto> circuits=circuitsContainer.getCircuits();

        int circuitsCount=circuits.size();
        BitSet evaluatedValue=new FixedLengthBitSet(circuitsCount);

        for(int i=0; i<circuitsCount; i++){
            CircuitDto circuit=circuits.get(i);
            BitEvaluator evaluator=new BitEvaluator(host, circuitsContainerHash, circuit, assignment);
            boolean evaluatedBit=false;
            try{
                evaluatedBit=evaluator.evaluateBit();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            evaluatedValue.set(i, evaluatedBit);
        }

        return evaluatedValue;
    }

    private Assignment generateAssignmentBitSet(){
        Assignment assignment=new Assignment();
        FormulaFactory factory=new FormulaFactory();
        int numOfRequiredLiterals= encodedQGram.length();
        LiteralNameAllocator literalNameAllocator =new LiteralNameAllocator(numOfRequiredLiterals);

        for(int i=0; i<numOfRequiredLiterals; i++){
            String literalName=literalNameAllocator.getLiteralName(i).toString();
            boolean literalPhase= encodedQGram.get(i);
            Literal literal=factory.literal(literalName,literalPhase);
            assignment.addLiteral(literal);
        }

        return assignment;
    }
}
