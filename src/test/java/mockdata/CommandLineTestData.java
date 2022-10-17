package mockdata;

import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandLineTestData {

    public final String OWN_PORT="8081";
    public final String SECRET_KEY= SecretKeyTestData.getKeyHex();
    public final String FILE="src/test/resources/existing_file_for_ArgumentsInterpreterTest.csv";
    public final String SECOND_HOST_NAME="192.168.1.2";
    public final String SECOND_HOST_PORT="8082";
    public final String SECOND_HOST =SECOND_HOST_NAME+ ReservedCharacters.ARGUMENTS_HOSTNAME_PORT_SECRETKEY_SEPARATOR+SECOND_HOST_PORT;
    public final String THIRD_HOST_NAME="192.168.1.3";
    public final String THIRD_HOST_PORT="8083";
    public final String THIRD_HOST=THIRD_HOST_NAME+ReservedCharacters.ARGUMENTS_HOSTNAME_PORT_SECRETKEY_SEPARATOR+THIRD_HOST_PORT;

    public final String[] CORRECT_ARGUMENTS_2P=new String[] {OWN_PORT, SECRET_KEY, FILE, SECOND_HOST};
    public final String[] CORRECT_ARGUMENTS_3P=new String[] {OWN_PORT, SECRET_KEY, FILE, SECOND_HOST, THIRD_HOST};

    public final String[] WRONG_PORT_ARGUMENT=new String[] {"i"+OWN_PORT, SECRET_KEY, FILE, SECOND_HOST};
    public final String[] WRONG_SECRET_KEY_ARGUMENT=new String[] {OWN_PORT, "zIsNotHexadecimal"+SECRET_KEY, FILE, SECOND_HOST};
    public final String[] WRONG_FILE_ARGUMENT=new String[] {OWN_PORT, SECRET_KEY, "non"+FILE, SECOND_HOST};
}
