package test.java.mockdata;

import lombok.experimental.UtilityClass;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class LiteralTestData {

    public Set<Literal> getLiteralsAtoG(){
        FormulaFactory factory=new FormulaFactory();
        Literal a=factory.literal("A", false);
        Literal b=factory.literal("B", false);
        Literal c=factory.literal("C", false);

        Literal d=factory.literal("D", false);
        Literal e=factory.literal("E", false);
        Literal f=factory.literal("F", false);

        Literal g=factory.literal("G", false);

        Set<Literal> literals=new HashSet<>();
        literals.add(a);
        literals.add(b);
        literals.add(c);
        literals.add(d);
        literals.add(e);
        literals.add(f);
        literals.add(g);

        return literals;
    }

    public Set<Literal> getLiteralA(){
        FormulaFactory factory=new FormulaFactory();
        Literal a=factory.literal("A", false);

        Set<Literal> literals=new HashSet<>();
        literals.add(a);

        return literals;
    }

    public Set<Literal> getLiteralsAtoC(){
        FormulaFactory factory=new FormulaFactory();
        Literal a=factory.literal("A", false);
        Literal b=factory.literal("B", false);
        Literal c=factory.literal("C", false);

        Set<Literal> literals=new HashSet<>();
        literals.add(a);
        literals.add(b);
        literals.add(c);

        return literals;
    }


}
