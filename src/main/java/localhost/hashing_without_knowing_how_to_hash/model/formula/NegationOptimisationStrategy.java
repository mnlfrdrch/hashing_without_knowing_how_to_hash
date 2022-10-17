package localhost.hashing_without_knowing_how_to_hash.model.formula;

import org.logicng.formulas.Formula;
import org.logicng.transformations.simplification.*;

public class NegationOptimisationStrategy implements  FormulaOptimisationStrategy{

    private NegationSimplifier negationSimplifier;

    public NegationOptimisationStrategy(){
        negationSimplifier=new NegationSimplifier();
    }

    /**
     * Applies light optimisation to unoptimised formula.
     * Tries to minimise the number of negations.
     * Runs fast in general.
     * @param unoptimisedFormula formula describing a hash function e.g. in CNF
     * @return equivalent optimised formula
     */
    @Override
    public Formula optimiseFormula(Formula unoptimisedFormula){
        Formula negationSimplifiedFormula=negationSimplifier.apply(unoptimisedFormula, true);
        return negationSimplifiedFormula;
    }
}
