package localhost.hashing_without_knowing_how_to_hash.model.formula;

import org.logicng.formulas.Formula;

public interface FormulaOptimisationStrategy {
    Formula optimiseFormula(Formula unoptimisedFormula);
}
