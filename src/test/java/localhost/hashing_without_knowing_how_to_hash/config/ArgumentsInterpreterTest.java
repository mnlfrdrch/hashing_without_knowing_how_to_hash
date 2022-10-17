package localhost.hashing_without_knowing_how_to_hash.config;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import mockdata.CommandLineTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.junit.jupiter.api.Assertions.*;

class ArgumentsInterpreterTest {

    private ArgumentsInterpreter argumentsInterpreter;

    @AfterEach
    public void tearDown(){
        argumentsInterpreter=null;
    }

    @Test
    public void testServerPortShouldBeReadInCorrectly(){
        //given
        argumentsInterpreter=new ArgumentsInterpreter(CommandLineTestData.CORRECT_ARGUMENTS_2P);

        //when
        String actualPort=argumentsInterpreter.getOwnServerPort();

        //then
        assertEquals(CommandLineTestData.OWN_PORT, actualPort);
    }

    @Test
    public void testSecretKeyShouldBeReadInCorrectly(){
        //given
        argumentsInterpreter=new ArgumentsInterpreter(CommandLineTestData.CORRECT_ARGUMENTS_2P);

        //when
        HashFunctionSecretKeyDto actualSecretKey=argumentsInterpreter.getOwnHashFunctionKey();

        //then
        assertEquals(CommandLineTestData.SECRET_KEY, actualSecretKey.getKey());
    }

    @Test
    public void testFileShouldBeReadInCorrectly(){
        //given
        argumentsInterpreter=new ArgumentsInterpreter(CommandLineTestData.CORRECT_ARGUMENTS_2P);

        //when
        File actualFile=argumentsInterpreter.getModelFile();

        //then
        File expectedFile=new File(CommandLineTestData.FILE);
        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void testForASingleHostSpecifiedGetHostsShouldReturnAListOfSize1(){
        //given
        argumentsInterpreter=new ArgumentsInterpreter(CommandLineTestData.CORRECT_ARGUMENTS_2P);

        //when
        List<HostDto> hosts=argumentsInterpreter.getHosts();

        //then
        assertEquals(1, hosts.size());
    }

    @Test
    public void testSingleHostShouldBeHandledCorrectly(){
        //given
        argumentsInterpreter=new ArgumentsInterpreter(CommandLineTestData.CORRECT_ARGUMENTS_2P);

        //when
        HostDto actualHost=argumentsInterpreter.getHosts().get(0);

        //then
        assertEquals(CommandLineTestData.SECOND_HOST_NAME, actualHost.getHost());
        assertEquals(CommandLineTestData.SECOND_HOST_PORT, actualHost.getPort());
    }

    @Test
    public void testForTwoHostSpecifiedGetHostsShouldReturnAListOfSize2(){
        //given
        argumentsInterpreter=new ArgumentsInterpreter(CommandLineTestData.CORRECT_ARGUMENTS_3P);

        //when
        List<HostDto> hosts=argumentsInterpreter.getHosts();

        //then
        assertEquals(2, hosts.size());
    }

    @Test
    public void testSecondHostHostShouldBeParsedCorrectly(){
        //given
        argumentsInterpreter=new ArgumentsInterpreter(CommandLineTestData.CORRECT_ARGUMENTS_3P);

        //when
        HostDto actualHost=argumentsInterpreter.getHosts().get(1);

        //then
        assertEquals(CommandLineTestData.THIRD_HOST_NAME, actualHost.getHost());
        assertEquals(CommandLineTestData.THIRD_HOST_PORT, actualHost.getPort());
    }

    @Test
    public void testNullAsConstructorArgumentShouldTriggerUnsuccessfullSystemExit(){
        //given

        //when
        int actualStatus=0;
        try {
            actualStatus=catchSystemExit(() -> {
                argumentsInterpreter = new ArgumentsInterpreter(null);
            });
        }catch (Exception e){
            fail(e.getMessage());
        }

        //then
        assertEquals(-1, actualStatus);
    }

    @Test
    public void testIllegalPortAsArgumentShouldTriggerUnsuccessfullSystemExit() throws Exception {
        //given

        //when
        int actualStatus=0;
        try {
            actualStatus=catchSystemExit(() -> {
                argumentsInterpreter = new ArgumentsInterpreter(CommandLineTestData.WRONG_PORT_ARGUMENT);
            });
        }catch (Exception e){
            fail(e.getMessage());
        }

        //then
        assertEquals(-1, actualStatus);
    }

    @Test
    public void testIllegalSecretKeyAsArgumentShouldTriggerUnsuccessfullSystemExit() throws Exception {
        //given

        //when
        int actualStatus=0;
        try {
            actualStatus=catchSystemExit(() -> {
                argumentsInterpreter = new ArgumentsInterpreter(CommandLineTestData.WRONG_SECRET_KEY_ARGUMENT);
            });
        }catch (Exception e){
            fail(e.getMessage());
        }

        //then
        assertEquals(-1, actualStatus);
    }

    @Test
    public void testNonExistingFileAsArgumentShouldTriggerUnsuccessfullSystemExit() throws Exception {
        //given

        //when
        int actualStatus=0;
        try {
            actualStatus=catchSystemExit(() -> {
                argumentsInterpreter = new ArgumentsInterpreter(CommandLineTestData.WRONG_FILE_ARGUMENT);
            });
        }catch (Exception e){
            fail(e.getMessage());
        }

        //then
        assertEquals(-1, actualStatus);
    }
}