package main.java.hashing_without_knowing_how_to_hash.util;

import java.util.HashSet;
import java.util.Set;


/**
 * Provides the union operation over multiple Sets of type T
 * Multiple Sets can be united into a single set using this class
 * @param <T> could be of type Character for example
 */
public class SetUnionUtil<T> {

    /**
     * Union of two sets
     * @param firstSource
     * @param secondSource
     * @return firstSource \cup secondSource
     */
    public Set<T> union(Set<T> firstSource, Set<T> secondSource){
        Set<T> destination=new HashSet<>();
        destination.addAll(firstSource);
        destination.addAll(secondSource);
        return destination;
    }

    /**
     * Union of three sets
     * @param firstSource
     * @param secondSource
     * @param thirdSource
     * @return firstSource \cup secondSource \cup thirdSource
     */
    public Set<T> union(Set<T> firstSource, Set<T> secondSource, Set<T> thirdSource){
        Set<T> partialDestination=union(firstSource, secondSource);
        Set<T> destination=union(partialDestination, thirdSource);
        return destination;
    }

    /**
     * Union of four sets
     * @param firstSource
     * @param secondSource
     * @param thirdSource
     * @param fourthSource
     * @return firstSource \cup secondSource \cup thirdSource \cup fourthSource
     */
    public Set<T> union(Set<T> firstSource, Set<T> secondSource, Set<T> thirdSource, Set<T> fourthSource){
        Set<T> partialDestination=union(firstSource, secondSource, thirdSource);
        Set<T> destination=union(partialDestination, fourthSource);
        return destination;
    }
}
