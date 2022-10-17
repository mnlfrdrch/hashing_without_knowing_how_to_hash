package localhost.hashing_without_knowing_how_to_hash.service;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.cache.WireCache;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsAndWiresContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.NamedWiresContainerDto;
import localhost.hashing_without_knowing_how_to_hash.model.circuit.CircuitBuilder;
import localhost.hashing_without_knowing_how_to_hash.security.access_control_tree.AccessControlTreeFinalLayer;
import org.springframework.stereotype.Service;
import yao.gate.Wire;

import java.util.List;

/**
 * Generates individual hash function and converts it into encrypted circuit
 */
@Service
public class CircuitService {

    /**
     * Generates new garbled circuits with each call
     * Is directly used by the equally called method of CircuitController
     * @return the CircuitsContainerDto containing the circuits to evaluate exactly one hash value secret
     */
    public CircuitsContainerDto getCircuit(){
        FormulaCache formulaCache=FormulaCache.getInstance();
        CircuitBuilder circuitBuilder=new CircuitBuilder(formulaCache.getFormulaContainerDto());

        WireCache wireCache=WireCache.getInstance();
        CircuitsAndWiresContainerDto circuitsAndWires=circuitBuilder.buildAll();
        CircuitsContainerDto circuits=circuitsAndWires.getCircuitsContainerDto();
        List<NamedWiresContainerDto> wiresContainers=circuitsAndWires.getNamedWiresContainerDtoList();
        for(NamedWiresContainerDto wiresContainer: wiresContainers){
            wireCache.cacheWires(circuits.getCircuitsContainerHash(),wiresContainer.getCircuitHash(),new AccessControlTreeFinalLayer<Wire>(wiresContainer.getWireIdToWire()));
        }
        return circuits;
    }

    /**
     * Status information about, which is required for timing of the protocol
     * @return *true* if getCiruit() is ready to be called, else *false*
     */
    public boolean getStatus(){
        FormulaCache formulaCache=FormulaCache.getInstance();
        return formulaCache.isFormulaContainerBuilt();
    }
}
