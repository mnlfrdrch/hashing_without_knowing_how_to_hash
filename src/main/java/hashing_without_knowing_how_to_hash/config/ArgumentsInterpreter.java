package main.java.hashing_without_knowing_how_to_hash.config;

import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the arguments of command line
 * Exits program with error in case specified parameter are invalid and gives hint of correct usage
 * If arguments are valid, the arguments are then parsed and can then be accessed from private variables of this class
 */
public class ArgumentsInterpreter {

    @Getter
    private String ownServerPort;
    @Getter
    private HashFunctionSecretKeyDto ownHashFunctionKey;
    @Getter
    private File modelFile;
    @Getter
    private List<HostDto> hosts;

    /**
     * Checks, whether der args is of the correct form "<OWN_SERVER_PORT> <OWN_HASH_FUNCTION_SECRET_KEY> <MODEL_FILE> <PARTY1_HOST_NAME>:<PARTY1_PORT>:<PARTY1_SECRET_KEY> <...> <PARTYN_HOST_NAME>:<PARTYN_PORT>:<PARTYN_SECRET_KEY>"
     * If args is valid, parses attributes
     * Else exit with error and print hint of correct usage
     * @param args parameter of main function
     */
    public ArgumentsInterpreter(String[] args){
        exitIfArgsAreInvalid(args);
        ownServerPort=extractOwnServerPort(args);
        ownHashFunctionKey=extractOwnHashFunctionKey(args);
        modelFile= extractModelFile(args);
        List<String> otherPartysAttributes=extractPartyAttributes(args);
        hosts = saveOtherHostsAttributes(otherPartysAttributes);
    }

    private void printUsage(){
        System.out.println("Usage: $java -jar hashing_without_knowing_how_to_hash.jar <OWN_SERVER_PORT> <OWN_HASH_FUNCTION_SECRET_KEY> <MODEL_FILE> <PARTY1_HOST_NAME>:<PARTY1_PORT> ... <PARTYN_HOST_NAME>:<PARTYN_PORT>");
    }

    private boolean isOfRequiredMinLength(String[] args){
        boolean isOfRequiredMinLength=false;
        isOfRequiredMinLength=args.length>=3; //mind. file, key, one other party
        return isOfRequiredMinLength;
    }


    private void exitIfArgsAreInvalid(String[] args){
        if(args==null){
            System.err.println("No arguments specified");
            exitBecauseInvalid();
        }
        if(!isOfRequiredMinLength(args)){
            System.err.println("Not enough arguments specified");
            exitBecauseInvalid();
        }
        if(!isServerPort(args[0])){
            System.err.println("Own server port is invalid");
            exitBecauseInvalid();
        }
        if(!isHashFunctionKey(args[1])){
            System.err.println("Secret key is invalid");
            exitBecauseInvalid();
        }
        if(!isExistingFile(args[2])){
            System.err.println("File does not exist");
            exitBecauseInvalid();
        }
    }

    private void exitBecauseInvalid(){
        printUsage();
        System.exit(-1);
    }

    private boolean isServerPort(String argument){
        boolean isPort=false;
        int MIN_TCP_PORT=0;
        int MAX_TCP_PORT=65535;
        int port;
        try{
            port=Integer.parseInt(argument);
            if(MIN_TCP_PORT <= port && port <= MAX_TCP_PORT){
                isPort=true;
            }
        }
        catch (NumberFormatException e){
            isPort=false;
        }
        return isPort;
    }

    private boolean isHashFunctionKey(String argument){
        boolean isValidKey=false;
        try{
            new HashFunctionSecretKeyDto(argument, ModelMetaData.ALGORITHM_TYPE);
            isValidKey=true;
        }catch (IllegalArgumentException e){
            isValidKey=false;
        }
        return isValidKey;
    }

    private boolean isExistingFile(String argument) {
        File file=new File(argument);
        return file.exists();
    }

    private String extractOwnServerPort(String[] args){
        return args[0];
    }

    private HashFunctionSecretKeyDto extractOwnHashFunctionKey(String[] args){
        String potentialHexKey=args[1];
        HashFunctionSecretKeyDto secretKey=null;
        try{
            secretKey=new HashFunctionSecretKeyDto(potentialHexKey, ModelMetaData.ALGORITHM_TYPE);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return secretKey;
    }

    private File extractModelFile(String[] args){
        return new File(args[2]);
    }

    private List<String> extractPartyAttributes(String[] args){
        List<String> hostAttributes=new ArrayList<>();
        int indexFirstHost=3;
        for(int i=indexFirstHost; i<args.length; i++){
            hostAttributes.add(args[i]);
        }
        return hostAttributes;
    }

    private List<HostDto> saveOtherHostsAttributes(List<String> partyArgs) throws IllegalArgumentException{
        List<HostDto> hosts=new ArrayList<>();
        try{
            for(String argsSection: partyArgs){
                HostDto host=extractHostDtoFromArgumentSection(argsSection);
                hosts.add(host);
            }
        }
        catch (IllegalArgumentException e){
            printUsage();
            System.exit(-1);
        }
        return hosts;
    }

    private HostDto extractHostDtoFromArgumentSection(String argsSection){
        HostDto host=null;
        String[] hostNameAndPort=argsSection.split(String.valueOf(ReservedCharacters.ARGUMENTS_HOSTNAME_PORT_SECRETKEY_SEPARATOR));
        String hostName=hostNameAndPort[0];
        String port=hostNameAndPort[1];
        host=new HostDto(hostName, port);
        return host;
    }
}
