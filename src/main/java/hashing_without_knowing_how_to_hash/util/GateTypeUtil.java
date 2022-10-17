package main.java.hashing_without_knowing_how_to_hash.util;

import lombok.experimental.UtilityClass;
import org.logicng.formulas.*;
import org.logicng.functions.SubNodeFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to identify the type of gates, that need to be instantiated for a given formula
 */
@UtilityClass
public class GateTypeUtil {

    /**
     * Detects, if formula is negated
     * @param formula any propositional formula
     * @return *true* is formula if of form ~(subformula), else *false*
     */
    public boolean isNegation(Formula formula){
        return formula instanceof Not;
    }

    /**
     * Detects, if formula is a conjunction of subformulas
     * @param formula any propositional formula
     * @return *true* is formula if of form subformula1 & subformula2, else *false*
     */
    public boolean isAnd(Formula formula){
        return formula instanceof And;
    }

    /**
     * Detects, if formula is a disjunction of subformulas
     * @param formula any propositional formula
     * @return *true* is formula if of form subformula1 | subformula2, else *false*
     */
    public boolean isOr(Formula formula){
        return formula instanceof Or;
    }

    /**
     * Detects, if formula is a negated conjunction of subformulas
     * @param formula any propositional formula
     * @return *true* is formula if of form ~(subformula1 & subformula2), else *false*
     */
    public boolean isNand(Formula formula){
        boolean res=false;
        if(isNegation(formula) && !LiteralUtil.isLiteral(formula)) {
            Formula extracted=getFormulaInsideNegation(formula);
            res=isAnd(extracted);
        }
        return res;
    }

    /**
     * Detects, if formula is a negated disjunction of subformulas
     * @param formula any propositional formula
     * @return *true* is formula if of form ~(subformula1 | subformula2), else *false*
     */
    public boolean isNor(Formula formula){
        boolean res=false;
        if(isNegation(formula) && !LiteralUtil.isLiteral(formula)) {
            Formula extracted=getFormulaInsideNegation(formula);
            res=isOr(extracted);
        }
        return res;
    }

    /**
     * For formulas like ~(A & B) extracts A & B
     * @param formula which is a negation
     * @return a new instance of the given formula, but the negation is removed
     */
    public Formula getFormulaInsideNegation(Formula formula){
        Formula formulaInsideNegation=null;
        SubNodeFunction subNodeFunction=new SubNodeFunction();

        if (formula != null && isNegation(formula) && !LiteralUtil.isLiteral(formula)){
            List<Formula> subFormulas = new ArrayList<>(subNodeFunction.apply(formula, false));
            subFormulas.remove(formula);
            formulaInsideNegation = subFormulas.get(subFormulas.size() - 1);
        }
        else{
            formulaInsideNegation = formula;
        }

        return formulaInsideNegation;
    }
}
