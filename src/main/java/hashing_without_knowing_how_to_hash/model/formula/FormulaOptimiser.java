package main.java.hashing_without_knowing_how_to_hash.model.formula;

import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.HashTable;
import localhost.hashing_without_knowing_how_to_hash.model.lookuptable.TruthTable;
import lombok.Getter;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.transformations.simplification.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses library logicNG to parse and improve the formula-strings given from the truth tables
 */
public class FormulaOptimiser {


    List<Formula> formulas;
    @Getter
    List<Formula> improvedFormulas;
    HashTable hashTable;
    FormulaOptimisationStrategy strategy;

    public FormulaOptimiser(HashTable hashTable) {
        this.hashTable = hashTable;
        formulas = new ArrayList<>();
        improvedFormulas = new ArrayList<>();
        strategy= ModelMetaData.OPTIMISATION_STRATEGY;
    }

    public FormulaContainerDto genImprovedFormulas(){
        loadFormulas();
        applyImprovementToAllFormulas();
        return new FormulaContainerDto(getImprovedFormulas());
    }

    public void loadFormulas() {
        for(TruthTable truthTable: hashTable.getTruthTables()){
            Formula formulaOfTruthTable=generateFormulaFromTruthTable(truthTable);
            formulas.add(formulaOfTruthTable);
        }
    }

    private Formula generateFormulaFromTruthTable(TruthTable table){
        Formula logicNgFormula = null;
        String formulaOfTable = table.getCNF().toString();
        formulaOfTable=handleIfFormulaStringIsNull(formulaOfTable);
        try {
            logicNgFormula = genFormulaFromString(formulaOfTable);
        } catch (Exception parserException) {
            parserException.printStackTrace();
        }
        finally {
            logicNgFormula=handleIfLogicNgFormulaIsNull(logicNgFormula);
        }
        return logicNgFormula;
    }

    private String handleIfFormulaStringIsNull(String formula) {
        try{
        if (formula == null) {
            formula = "";
            throw new NullPointerException("formulaString==null");
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return formula;
    }

    private Formula handleIfLogicNgFormulaIsNull(Formula formula){
        Formula recentFormulaOrEmptyFormula=null;
        if(formula!=null){
            recentFormulaOrEmptyFormula=formula;
        }
        else {
            FormulaFactory factory=new FormulaFactory();
            recentFormulaOrEmptyFormula=factory.constant(false);
        }
        return recentFormulaOrEmptyFormula;
    }

    public Formula genFormulaFromString(String formulaString) throws ParserException {
        FormulaFactory factory=new FormulaFactory();
        PropositionalParser parser=new PropositionalParser(factory);
        Formula formula=null;
        while (formula==null){
            formula=parser.parse(formulaString);
        }
        return formula;
    }

    public void applyImprovementToAllFormulas(){
        //improvedFormulas=parallelOptimiseFormulas(formulas);
        improvedFormulas=linearOptimiseFormulas(formulas);
    }

    public List<Formula> parallelOptimiseFormulas(List<Formula> unoptimisedFormulas){
        List<Formula> optimisedFormulas=new ArrayList<>();
        unoptimisedFormulas
                .parallelStream()
                .forEach((Formula formula)->{
                    Formula optimisedFormula=strategy.optimiseFormula(formula);
                    optimisedFormulas.add(optimisedFormula);
                });
        return optimisedFormulas;
    }

    public List<Formula> linearOptimiseFormulas(List<Formula> unoptimisedFormulas){
        List<Formula> optimisedFormulas=new ArrayList<>();
        for (Formula formula:unoptimisedFormulas){
            Formula optimisedFormula=strategy.optimiseFormula(formula);
            optimisedFormulas.add(optimisedFormula);
        }
        return optimisedFormulas;
    }
}
