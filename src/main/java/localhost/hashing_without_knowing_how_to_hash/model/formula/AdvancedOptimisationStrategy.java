package localhost.hashing_without_knowing_how_to_hash.model.formula;

import org.logicng.formulas.Formula;
import org.logicng.transformations.simplification.AdvancedSimplifier;
import org.logicng.transformations.simplification.DefaultRatingFunction;
import org.logicng.transformations.simplification.RatingFunction;

public class AdvancedOptimisationStrategy implements FormulaOptimisationStrategy{

    private AdvancedSimplifier advancedSimplifier;

    public AdvancedOptimisationStrategy(){
        RatingFunction ratingFunction=new DefaultRatingFunction();
        advancedSimplifier=new AdvancedSimplifier(ratingFunction, null);
    }

    /**
     * Applies powerful optimisation to unoptimised formula.
     * Might run a long time for big formulas.
     * @param unoptimisedFormula formula describing a hash function e.g. in CNF
     * @return equivalent optimised formula
     */
    @Override
    public Formula optimiseFormula(Formula unoptimisedFormula){
        Formula improvedFormula=null;
        improvedFormula=advancedSimplifier.apply(unoptimisedFormula, true);
        return improvedFormula;
    }

}
