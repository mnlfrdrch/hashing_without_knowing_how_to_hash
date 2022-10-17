package localhost.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.model.formula.FormulaOptimiser;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.HashTable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Generates and holds propositional formulas of a HashingAlgorithm accessible in one place
 * Is a singleton so though out the lifetime of a program all objects handle the same FormulaCache instance
 */
public class FormulaCache implements Serializable {

    private static FormulaCache singleton;

    @Getter
    @Setter
    private FormulaContainerDto formulaContainerDto;

    private FormulaCache(){
        formulaContainerDto=null;
    }

    /**
     * FormulaCache is a singleton and can be instantiated by calling FormulaCache.getInstance()
     * Multiple objects access FormulaCache asynchronously
     * The singleton pattern is therefore necessary to ensure that all instances operate on the same formulas
     * @return always the same instance of FormulaCache
     */
    public static FormulaCache getInstance(){
        if(FormulaCache.singleton==null){
            FormulaCache.singleton=new FormulaCache();
        }
        return FormulaCache.singleton;
    }

    /**
     * Generates one propositional formula per bit of hash value in the hash table
     * Formulas will be optimised by OptimisationStrategy specified in ModelMetaData
     * The generation Process might therefore take a long time
     * After processing, the formulas are stored in formulaContainerDto and can be accessed
     * The propositional formula is an important intermediate step between the HashingAlgorithm to be evaluated and Yao's garbled circuit which can actually be securely evaluated
     * Method is called by FormulaCacheLoader to provide formulas in order to build circuit
     * @param hashTable resulting from a combination of HashingAlgorithm and Domain
     */
    public void generateFormulasFromHashTable(HashTable hashTable){
        FormulaOptimiser formulaOptimiser=new FormulaOptimiser(hashTable);
        formulaContainerDto = formulaOptimiser.genImprovedFormulas();
    }

    /**
     * Provides binary state of formula generation
     * In particular, indicates if formula is already loaded
     * Generation of formula container can take a long time
     * Objects, which access the formula container need therefore report whether the formula container is ready to use or no
     * It can thus be called e.g. within "state" method of CircuitController.class
     * Other use would be by FormulaCacheLoader to determine if formulas need to be generated for the first time
     * @return *true* if formula loaded and *false* if not
     */
    public boolean isFormulaContainerBuilt(){
        return formulaContainerDto!=null;
    }
}
