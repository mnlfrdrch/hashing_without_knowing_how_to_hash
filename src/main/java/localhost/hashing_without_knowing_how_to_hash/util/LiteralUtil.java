package localhost.hashing_without_knowing_how_to_hash.util;

import lombok.experimental.UtilityClass;
import org.logicng.formulas.Formula;
import org.logicng.formulas.Literal;

/**
 * Utility class to identify type of literal from formula
 * Is used by CircuitBuilder to identify certain structures in the given propositional formula
 * CircuitBuilder uses it to detect, if an initial wire needs to be set or needs to be negated
 */
@UtilityClass
public class LiteralUtil {


    /**
     * Detects, if a formula consists of a literal only
     * e.g. the formula A consists of a literal only,
     * the formula ~A also consists of a literal only,
     * the formula A & B does not consist of a literal only.
     * @param formula any propositional formula
     * @return *true* if formula consists of a single literal only, else *false*
     */
    public boolean isLiteral(Formula formula){
        return formula instanceof Literal;
    }

    /**
     * Detects, if a formula consists of a negated literal only
     * e.g. the formula A does not consists of a negated literal only,
     * while the formula ~A consists of a negated literal only.
     * The formula A & B does not consist of a negated literal only,
     * it does not even consist of a single literal
     * @param formula any propositional formula
     * @return *true* if formula consists of a single negated literal only, else *false*
     */
    public boolean isNegatedLiteral(Formula formula) {
        if (isLiteral(formula)) {
            String originalStringFormula = formula.toString();
            char firstChar = originalStringFormula.charAt(0);
            if (firstChar == '~') {
                return true;
            }
        }
        return false;
    }

    /**
     * cuts of the first character of a string
     * @param literal let literal be ~A
     * @return then ~ is cut off an A is returned
     */
    public String extractVariable(String literal){
        if(literal==null){

        }
        if(literal.length()<2){

        }
        return literal.substring(1);
    }
}
