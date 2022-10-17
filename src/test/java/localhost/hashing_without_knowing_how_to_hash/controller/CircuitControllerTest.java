package localhost.hashing_without_knowing_how_to_hash.controller;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.constants.ControllerPaths;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.util.HttpUtil;
import mockdata.FormulaTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.logicng.formulas.Formula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class CircuitControllerTest {

    @LocalServerPort
    private int PORT;

    private String PROTOCOL="http";
    private String HOST= "localhost";


    @Autowired
    private CircuitController circuitController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @AfterEach
    public void tearDown(){
        FormulaCache formulaCache=FormulaCache.getInstance();
        ReflectionTestUtils.setField(formulaCache, "formulaContainerDto", null);
    }

    private HostDto getHostDto(){
        return new HostDto(HOST, String.valueOf(PORT));
    }

    @Test
    public void testCircuitControllerIsNotNull() {
        assertNotNull(circuitController);
    }


    @Test
    public void testGetCircuitBuildsCircuit(){
        //Given
        String address=HttpUtil.buildBasicUrl(getHostDto(), ControllerPaths.CIRCUIT);
        CircuitsContainerDto circuits=null;

        //when
        circuits=testRestTemplate.getForObject(address, CircuitsContainerDto.class);

        //then
        assertNotNull(circuits);
    }

    @Test
    public void testGetStatusShouldBeFalseIfWireCacheIsNotLoadedYet(){
        //given
        ReflectionTestUtils.setField(FormulaCache.getInstance(), "formulaContainerDto", null);
        String address=HttpUtil.buildBasicUrl(getHostDto(), ControllerPaths.CIRCUIT+"/"+ControllerPaths.STATUS);

        //when
        boolean receivedStatusShouldBeFalse=testRestTemplate.getForObject(address, Boolean.class).booleanValue();

        //then
        assertFalse(receivedStatusShouldBeFalse);
    }

    private void loadFormulaCacheWithTestData(){
        List<Formula> testData= FormulaTestData.getTestData();
        FormulaContainerDto formulaContainer=new FormulaContainerDto(testData);

        FormulaCache cache=FormulaCache.getInstance();
        cache.setFormulaContainerDto(formulaContainer);
    }

    @Test
    public void testGetStatusShouldBeTrueIfWireCacheIsLoaded(){
        //given
        String address=HttpUtil.buildBasicUrl(getHostDto(), ControllerPaths.CIRCUIT+"/"+ControllerPaths.STATUS);
        loadFormulaCacheWithTestData();

        //when
        boolean receivedStatusShouldBeTrue=testRestTemplate.getForObject(address, Boolean.class).booleanValue();

        //then
        assertTrue(receivedStatusShouldBeTrue);
    }

    @Test
    public void testGetCircuitForTestDataShouldContainThreeIndividualGarbledCircuits(){
        //given
        String address=HttpUtil.buildBasicUrl(getHostDto(), ControllerPaths.CIRCUIT);
        loadFormulaCacheWithTestData();
        int expectedSize=FormulaCache.getInstance().getFormulaContainerDto().getFormulaList().size();

        //when
        CircuitsContainerDto circuitsContainer=testRestTemplate.getForObject(address, CircuitsContainerDto.class);
        int actualSize=circuitsContainer.getCircuits().size();

        //then
        assertEquals(expectedSize, actualSize);
    }
}