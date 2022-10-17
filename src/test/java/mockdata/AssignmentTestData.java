package test.java.mockdata;

import lombok.experimental.UtilityClass;
import org.logicng.datastructures.Assignment;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;

@UtilityClass
public class AssignmentTestData {

    public Assignment getAssignmentAxBy(boolean assignmentA, boolean assignmentB){
        Assignment logicNgAssignment=new Assignment();
        FormulaFactory factory=new FormulaFactory();
        Literal a=factory.literal("A", assignmentA);
        Literal b=factory.literal("B", assignmentB);
        logicNgAssignment.addLiteral(a);
        logicNgAssignment.addLiteral(b);
        return logicNgAssignment;
    }

    public Assignment getAssignment(){
        FormulaFactory factory=new FormulaFactory();
        Literal a=factory.literal("A", true);
        Literal b=factory.literal("B", true);
        Literal c=factory.literal("C", true);
        Literal d=factory.literal("D", true);
        Literal e=factory.literal("E", true);
        Literal f=factory.literal("F", true);
        Literal g=factory.literal("G", true);
        Literal h=factory.literal("H", true);
        Literal i=factory.literal("I", true);
        Literal j=factory.literal("J", true);
        Literal k=factory.literal("K", true);
        Assignment logicNgAssignment=new Assignment();
        logicNgAssignment.addLiteral(a);
        logicNgAssignment.addLiteral(b);
        logicNgAssignment.addLiteral(c);
        logicNgAssignment.addLiteral(d);
        logicNgAssignment.addLiteral(e);
        logicNgAssignment.addLiteral(f);
        logicNgAssignment.addLiteral(g);
        logicNgAssignment.addLiteral(h);
        logicNgAssignment.addLiteral(i);
        logicNgAssignment.addLiteral(j);
        logicNgAssignment.addLiteral(j);
        logicNgAssignment.addLiteral(k);

        return logicNgAssignment;
    }

}
