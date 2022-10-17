package localhost.hashing_without_knowing_how_to_hash.dto.party;

import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import lombok.Getter;

/**
 * Splits those parts of the command line that concern data of a single party participating at the protocol
 * Extracts data and wraps extracted data into a dto
 */
public class ArgumentPartyConfigurationDto {

    @Getter
    HostDto hostDto;
    @Getter
    HashFunctionSecretKeyDto hashFunctionSecretKey;

    public ArgumentPartyConfigurationDto(String argsSection) throws IllegalArgumentException{
        String[] attributes=splitIntoAttributes(argsSection);
        hostDto=instantiateHostDtoFrom(attributes);
        hashFunctionSecretKey=extractHashFunctionSecretKeyFrom(attributes);
    }

    private String[] splitIntoAttributes(String argsSection) throws IllegalArgumentException{
        String[] attributes=argsSection.split(String.valueOf(ReservedCharacters.ARGUMENTS_HOSTNAME_PORT_SECRETKEY_SEPARATOR));
        int expectedNumArguments=3; //<HOSTNAME>:<PORT>:<HASHFUNCTIONSECRETKEY>
        if(attributes.length!=expectedNumArguments){
            throw new IllegalArgumentException(expectedNumArguments + " #attributes per party expected, but only got " + attributes.length);
        }
        return argsSection.split(String.valueOf(ReservedCharacters.ARGUMENTS_HOSTNAME_PORT_SECRETKEY_SEPARATOR));
    }

    private HostDto instantiateHostDtoFrom(String[] attributes){
        HostDto hostDto=null;
        String hostname=attributes[0];
        String port=attributes[1];
        hostDto=new HostDto(hostname, port);
        return hostDto;
    }

    private HashFunctionSecretKeyDto extractHashFunctionSecretKeyFrom(String[] attributes) throws IllegalArgumentException{
        HashFunctionSecretKeyDto secretKeyDto=null;
        String potentialHexKey=attributes[2];
        secretKeyDto=new HashFunctionSecretKeyDto(potentialHexKey, ModelMetaData.ALGORITHM_TYPE);
        return secretKeyDto;
    }

}
