package mockdata;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SecretKeyTestData {

    public String getKeyHex(){
        return "2094eda4928ee1d3a7db7a33ea1a9f24a5d0fef0cb540787a20d8de20eb3bbb8";
    }

    /**
     * For Usage of Sha256 + this key determined values are
     * h(00)=2fe9091c10f2b803b8aeb02ab591f8f2bd3b6ebff8e0d7e61528b640844f557a
     * h(01)=7703db7a7cd4d0ce168c5dc325ae62d79a35382fb8494e543d4af61fd7dacdd5
     * h(02)=8c9483a8e849ecd1b7015bca7d0a02a0226c84c063caaf0090d5bf55350adf85
     * @return wrapped secret key for e.g. Sha256 HMAC
     */
    public HashFunctionSecretKeyDto getKeyWrapped(){
        HashFunctionSecretKeyDto secretKey=new HashFunctionSecretKeyDto(getKeyHex(), HashingAlgorithmType.SHA256);
        return secretKey;
    }
}
