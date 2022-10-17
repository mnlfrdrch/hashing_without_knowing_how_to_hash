package main.java.hashing_without_knowing_how_to_hash.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Hash values to qgrams are securely evaluated one by one
 * HashFunctionCache caches pairs (q-gram, secret_hash_value) or rather (x, h(x)) for the runtime of the program
 * It's a singleton, so other objects can have access to the cache without explicitly holding a reference to it
 * Has basic functionality of a key-value store
 * After secure evaluation it basically functions as the secret hash function
 */
public class HashFunctionCache {

    private static HashFunctionCache singleton;

    /**
     * Holds actual (x,h(x)) pairs
     */
    private Map<String, String> qGramToHashValue;

    private HashFunctionCache(){
        qGramToHashValue =new HashMap<>();
    }

    /**
     * The same HashFunctionCache instance should be accessible from any object without an explicit reference to it
     * That's why HashFunctionCache implements the singleton pattern
     * @return always the same HashFunctionCache instance
     */
    public static HashFunctionCache getInstance(){
        if(HashFunctionCache.singleton==null){
            HashFunctionCache.singleton= new HashFunctionCache();
        }
        return HashFunctionCache.singleton;
    }

    /**
     * Adds specified pair (q-gram, secret_hash_value) or rather (x, h(x)) to the cache
     * Once added pairs can later be called from potentially every object within the scope
     * @param qGram x
     * @param secretHashValue h(x)
     */
    public void cacheQGramHashValuePair(String qGram, String secretHashValue){
        qGramToHashValue.put(qGram,secretHashValue);
    }

    /**
     * Tells about existence of a pair (x,h(x)) in cache for given x
     * Relevant to assure that a hash value to a qgram can be requested
     * Can be called before requestSecretHashValueForQGram() to prevent an error
     * @param qGram x for cached (x,h(x))
     * @return *true* if there is a (q-gram, secret_hash_value) pair cached, else *false*
     */
    public boolean isQGramHashValuePairInCache(String qGram){
        return qGramToHashValue.containsKey(qGram);
    }

    /**
     * Like calculating h(x) if there is a pair (x, h(x)) cached
     * Existence of such a pair should be checked in advance by use of 'isQGramHashValuePairInCache()'-method in order to prevent errors
     * Exception will be thrown, handled and printed to stack trace
     * Error message of exception will also be returned instead of actual h(x) secret_hash_value
     * @param qGram
     * @return secret_hash_value or rather h(x) if there is an (x,h(x)) cached, else error message
     */
    public String requestSecretHashValueForQGram(String qGram){
        if(isQGramHashValuePairInCache(qGram))
            return qGramToHashValue.get(qGram);
        else
            printStackTraceQGramHashValuePairNotInCache(qGram);
            return null;
    }

    private String printStackTraceQGramHashValuePairNotInCache(String qGram){
        try{
            throw new NoSuchElementException("Requested (q-gram, secret_hash_value) pair for g-gram=" + qGram + " is not cached yet");
        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Collects all q-grams which occur within a currently cached (q-gram, secret_hash_value) pair
     * There is no direct access to map 'qGramToHashValue'
     * For this reason it is important to provide direct information which hash values can be requested
     * Can be used e.g. by HashFunctionCorrectnessChecker.class to iterate over all cached values
     * @return Set of all 'x'es, if there is a (x, h(x)) cached
     */
    public Set<String> getAllCachedQGrams(){
        return qGramToHashValue.keySet();
    }
}
