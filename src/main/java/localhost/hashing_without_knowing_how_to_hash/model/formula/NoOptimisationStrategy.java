package localhost.hashing_without_knowing_how_to_hash.model.formula;

import org.logicng.formulas.Formula;

public class NoOptimisationStrategy implements FormulaOptimisationStrategy{

    /**
     * Applies no optimisation at all to the formula.
     * Requires no runtime though.
     * @param unoptimisedFormula formula describing a hash function e.g. in CNF
     * @return the exact same formula
     */
    @Override
    public Formula optimiseFormula(Formula unoptimisedFormula){
        return unoptimisedFormula;
    }
}
