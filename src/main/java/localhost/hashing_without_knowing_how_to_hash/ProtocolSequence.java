package localhost.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.cache.HashFunctionCache;
import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.dao.PRIMATParser;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import localhost.hashing_without_knowing_how_to_hash.util.HttpUtil;
import localhost.hashing_without_knowing_how_to_hash.util.SleepUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Applies the HashingProtocol in a procedure.
 * For this task, required qgrams are red from an example file.
 * The HashingProtocol is used to securely evaluate the hash value secret of each qgram.
 * Pairs (qgram, h(qgram)) are then saved to the HashValueCache
 */
public class ProtocolSequence extends Thread{

    private List<HostDto> hosts;
    private Map<HostDto, Boolean> alreadyProcessed;
    private HashingProtocol hashingProtocol;

    private File csvFile;

    public ProtocolSequence(File csvFile, HashingAlgorithm ownHashingAlgorithm, List<HostDto> hosts){
        this.csvFile=csvFile;
        this.hosts=hosts;

        alreadyProcessed=new HashMap<>();
        for(HostDto host: hosts){
            alreadyProcessed.put(host, false);
        }

        hashingProtocol=new HashingProtocol(ownHashingAlgorithm, hosts);
    }

    /**
     * Implements the procedure, which executes the following steps:
     * 1) Read in all the required qgrams form the sample file
     * 2) Wait until are hosts are up
     * 3) Wait until all hosts are ready to transmit garbled circuits
     * 4) Securely evaluate the hash value for all of the previously determined qgrams
     * 5) Save the pairs (qgram,h(qgram)) into HashValueCache
     */
    public void run(){
        Set<String> requiredQGrams=collectRequiredQGrams();

        waitUntilAllHostsAreUp();
        waitUntilAllHostsHaveLoadedFormulaCache();

        HashFunctionCache hashFunctionCache=HashFunctionCache.getInstance();

        for(String qgram:requiredQGrams){
            if(hashingProtocol.isHashable(qgram)) { //temporary to filter qgrams consisting not only of numbers and #
                String secretHashValue=hashingProtocol.h(qgram);
                hashFunctionCache.cacheQGramHashValuePair(qgram, secretHashValue);
                System.out.println("h("+qgram + ") = "+secretHashValue);
            }
        }
    }

    private Set<String> collectRequiredQGrams(){
        PRIMATParser parser=new PRIMATParser(csvFile,ModelMetaData.RECORD_SCHEMA_CONFIGURATION, ModelMetaData.Q,ModelMetaData.WITH_PADDING);
        parser.loadRecords();
        return parser.collectRequiredQGrams();
    }

    private void waitUntilAllHostsHaveLoadedFormulaCache(){
        boolean allHostsHaveLoadedFormulaCache=haveAllHostsLoadedFormulaCache();
        while (!allHostsHaveLoadedFormulaCache) {
            SleepUtil.sleep();
            allHostsHaveLoadedFormulaCache=haveAllHostsLoadedFormulaCache();
        }
    }

    private void waitUntilAllHostsAreUp(){
        boolean areAllHostsUp=false;
        while (!areAllHostsUp){
            boolean isAtLeastOneHostDown=false;
            try{
                for(HostDto host:hosts) {
                    HttpUtil.httpGetStatus(host);
                }
            }
            catch (Exception e){
                isAtLeastOneHostDown=true;
            }
            if(isAtLeastOneHostDown) {
                SleepUtil.sleep();
            }
            else {
                areAllHostsUp=true;
            }
        }
    }

    private boolean haveAllHostsLoadedFormulaCache(){
        boolean allHostsHaveLoadedFormulas=true;
        for(HostDto host:hosts){
            allHostsHaveLoadedFormulas=allHostsHaveLoadedFormulas && hasHostLoadedFormulaCacheYet(host);
        }
        return allHostsHaveLoadedFormulas;
    }

    private boolean hasHostLoadedFormulaCacheYet(HostDto host){
        boolean currentSavedStatus=alreadyProcessed.get(host);
        boolean updatedStatus=false;
        if(!currentSavedStatus){
            updatedStatus=HttpUtil.httpGetStatus(host);
            alreadyProcessed.replace(host, currentSavedStatus, updatedStatus);
            return updatedStatus;
        }
        else {
            return currentSavedStatus;
        }
    }

}
