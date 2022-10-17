package main.java.hashing_without_knowing_how_to_hash.dto.formula;

import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import lombok.NoArgsConstructor;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.io.parsers.PropositionalParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the propositional formulas which describe a hashing algorithm for a specified domain
 * Used by FormulaCache to hold the required formula in place
 * Is serialisable, so it can be written to file by FormulaReaderWriter
 */
@NoArgsConstructor
public class FormulaContainerDto implements Serializable {

    private List<Formula> formulas;

    public FormulaContainerDto(List<Formula> formulas){
        this.formulas=formulas;
    }

    /**
     * Just the 'get' method for the 'formulas' field
     * @return the private variable 'formulas'
     */
    public List<Formula> getFormulaList(){
        return formulas;
    }

    /**
     * Converts current formulas list into string representation
     * @return string of current formulas
     */
    public String getFormulas(){
        StringBuilder sb=new StringBuilder();
        for (Formula formula: formulas) {
            sb.append(formula.toString());
            sb.append(ReservedCharacters.HASH_FUNCTION_FILE_SEPARATOR);
        }
        if(sb.charAt(sb.length()-1)== ReservedCharacters.HASH_FUNCTION_FILE_SEPARATOR){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    /**
     * Re-instantiates list of formulas from previously generated string representation of equivalent formula list
     * @param formulaList string representation of formulas list
     */
    public void setFormulas(String formulaList){
        if(formulas==null){
            formulas=new ArrayList<>();
        }

        FormulaFactory factory=new FormulaFactory();
        PropositionalParser parser=new PropositionalParser(factory);
        String[] formulasSeparated=formulaList.split(String.valueOf(ReservedCharacters.HASH_FUNCTION_FILE_SEPARATOR));

        for(String formulaEncoded:formulasSeparated){
            Formula formula=null;
            try {
                formula = parser.parse(formulaEncoded);
            }
            catch (Exception e){
                e.printStackTrace();
                Literal a=factory.literal("A", true);
                Literal notA=factory.literal("A", false);
                Formula tautology=factory.or(a, notA);
                formula=tautology;
            }
            formulas.add(formula);
        }
    }

    /**
     * Is used for instantiation on FormulaContainerDto object
     * Is required to be serialised correctly
     * Method is only used by FormulaReaderWriter
     * @param oos
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(getFormulas());
    }

    /**
     * Is used for writing a Serializable instance to file
     * Is required to be serialised correctly
     * Method is only used by FormulaReaderWriter
     * @param ois
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        List<Formula> formulas=null;
        Object shouldBeListOfFormulaEncodedAsString = ois.readObject();
        if(shouldBeListOfFormulaEncodedAsString instanceof String){
            String encodedListOfFormula= (String) shouldBeListOfFormulaEncodedAsString;
            setFormulas(encodedListOfFormula);
        }
        else {
            this.formulas=new ArrayList<>();
        }
    }
}
