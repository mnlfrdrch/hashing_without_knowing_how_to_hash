package test.java.mockdata;

import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsAndWiresContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.model.circuit.CircuitBuilder;
import lombok.experimental.UtilityClass;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.PropositionalParser;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FormulaTestData {

    private PropositionalParser instantiateParser(){
        FormulaFactory factory=new FormulaFactory();
        PropositionalParser parser=new PropositionalParser(factory);
        return parser;
    }

    private Formula convertStringToFormula(String source){
        Formula formula=null;
        PropositionalParser parser=instantiateParser();
        try{
            formula=parser.parse(source);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return formula;
    }

    public Formula getConjunctionFormula(){
        String conjunction="A & B";
        return convertStringToFormula(conjunction);
    }

    public Formula getDisjunctionFormula(){
        String disjunction="A | B";
        return convertStringToFormula(disjunction);
    }

    public Formula getNegationFormula(){
        String conjunction="~A";
        return convertStringToFormula(conjunction);
    }

    public Formula getNOR(){
        String nor="~(A | B)";
        return convertStringToFormula(nor);
    }

    public Formula getNAND(){
        String nand="~(A & B)";
        return convertStringToFormula(nand);
    }

    public Formula getTautology(){
        String tautology="A | ~A | B | ~B";
        return convertStringToFormula(tautology);
    }

    public Formula getContradiction(){
        String contradiction="A & ~A & B";
        return convertStringToFormula(contradiction);
    }

    //equivalent to A & ~B
    public Formula getComplexFormula(){
        String complex="(A & ~B) | ~(~A | B)";
        return convertStringToFormula(complex);
    }

    public List<Formula> getTestData(){
        List<Formula> testData=new ArrayList<>();

        testData.add(getConjunctionFormula());
        testData.add(getDisjunctionFormula());
        testData.add(getNegationFormula());

        return testData;
    }

    public List<Formula> get12BitAAndBFunction(){
        List<Formula> functionShare=new ArrayList<>();
        for(int i=0; i<12;i++){
            functionShare.add(getConjunctionFormula());
        }
        return functionShare;
    }


    public CircuitsAndWiresContainerDto generateCircuitAndWiresFromFormula(Formula formula){
        List<Formula> formulas=new ArrayList<>();
        formulas.add(formula);
        FormulaContainerDto formulaContainer=new FormulaContainerDto(formulas);
        CircuitBuilder cb=new CircuitBuilder(formulaContainer);
        CircuitsAndWiresContainerDto circuitAndWires=cb.buildAll();
        return circuitAndWires;
    }

}
