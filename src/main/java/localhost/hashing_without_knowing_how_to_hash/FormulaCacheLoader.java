package localhost.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.Domain;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashFunctionShare;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.HashTable;

/**
 * Prepares the generation of garbled circuits by loading the formulas describing the HashingAlgorithm into the FormulaCache
 */
public class FormulaCacheLoader extends Thread{

    private HashingAlgorithm hashingAlgorithm;
    public FormulaCacheLoader(HashingAlgorithm hashingAlgorithm){
        this.hashingAlgorithm=hashingAlgorithm;
    }

    /**
     * Generates or reads the formulas from file.
     * Those formulas are then loaded into the FormulaCache.
     */
    @Override
    public void run(){
        loadFormulaCache();
    }

    private void loadFormulaCache(){
        if(!FormulaCache.getInstance().isFormulaContainerBuilt()) {
            generateNewFormulas();
        }
    }

    private boolean generateNewFormulas(){
        boolean status=false;
        FormulaCache cache=FormulaCache.getInstance();
        Domain domain=new Domain(ModelMetaData.Q, ModelMetaData.CHARACTERS);
        HashFunctionShare hashFunctionShare =new HashFunctionShare(domain, hashingAlgorithm);
        HashTable hashTable =new HashTable(hashFunctionShare);
        cache.generateFormulasFromHashTable(hashTable);
        status=true;
        return status;
    }
}
