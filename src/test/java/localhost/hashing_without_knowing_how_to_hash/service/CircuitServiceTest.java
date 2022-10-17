package localhost.hashing_without_knowing_how_to_hash.service;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import org.junit.jupiter.api.*;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CircuitServiceTest {

    CircuitService circuitService;
    String FORMULA="A & B";



    @BeforeEach
    public void setUp(){
        loadFormulaCache();
        circuitService=new CircuitService();
    }

    @AfterEach
    public void tearDown(){
        circuitService=null;
        emptyFormulaCache();
    }
    public void loadFormulaCache(){
        FormulaCache formulaCache=FormulaCache.getInstance();
        try{
            FormulaContainerDto mockedFormulaContainer=mockFormulaContainerWithFormula(FORMULA);
            formulaCache.setFormulaContainerDto(mockedFormulaContainer);
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    public void emptyFormulaCache(){
        FormulaCache formulaCache=FormulaCache.getInstance();
        formulaCache.setFormulaContainerDto(null);
    }

    public FormulaContainerDto mockFormulaContainerWithFormula(String formula) throws ParserException {
        FormulaContainerDto formulaContainer=mock(FormulaContainerDto.class);
        FormulaFactory factory=new FormulaFactory();
        PropositionalParser parser=new PropositionalParser(factory);
        Formula logicNgFormula=parser.parse(formula);
        when(formulaContainer.getFormulaList()).thenReturn(List.of(logicNgFormula));
        return formulaContainer;
    }


    @Test
    public void testGetCircuitShouldTerminates(){
        Duration timeout=Duration.ofSeconds(1);
        assertTimeoutPreemptively(timeout, ()->{circuitService.getCircuit();});
    }

    @Test
    public void testGetCircuitShouldNotBeNull(){
        //when
        CircuitsContainerDto circuitsContainer=circuitService.getCircuit();

        //then
        assertNotNull(circuitsContainer);
    }

    @Test
    public void testGetCircuitShouldContainExactlyOneCircuit(){
        //given
        int expectedNumOfCircuits=1;

        //when
        CircuitsContainerDto circuitsContainer=circuitService.getCircuit();
        int actualNumOfCircuits=circuitsContainer.getCircuits().size();

        //then
        assertEquals(expectedNumOfCircuits, actualNumOfCircuits);
    }

    @Test
    public void testGetCircuitShouldContainExactlyOneEncodedGate(){
        //given
        int expectedNumOfEncodedGates=1;

        //when
        CircuitsContainerDto circuitsContainer=circuitService.getCircuit();
        CircuitDto circuit=circuitsContainer.getCircuits().get(0);
        int actualNumOfEncodedGates=circuit.getGatesAbbreviation().getIdAndGates().size();

        //then
        assertEquals(expectedNumOfEncodedGates, actualNumOfEncodedGates);
    }

    @Test
    public void testGetCircuitShouldContainMappingFromFirstGateToAGateB(){
        //given
        String expectedChildrenAlphabetic="A"+ ReservedCharacters.GATE_CHILDREN_SEPARATOR+"B";
        String expectedChildrenAntiAlphabetic="B"+ ReservedCharacters.GATE_CHILDREN_SEPARATOR+"A";

        //when
        CircuitsContainerDto circuitsContainer=circuitService.getCircuit();
        CircuitDto circuit=circuitsContainer.getCircuits().get(0);
        String actualChildren=circuit.getGateIdToChildrenGateIdsOrWireIds().get("1");

        boolean equalsChildrenAlphabetic=expectedChildrenAlphabetic.equals(actualChildren);
        boolean equalsChildrenAntiAlphabetic=expectedChildrenAntiAlphabetic.equals(actualChildren);

        //then
        assertTrue(equalsChildrenAlphabetic || equalsChildrenAntiAlphabetic);
    }
}