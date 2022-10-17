package test.java.mockdata;

import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;

@UtilityClass
public class HashingAlgorithmMockData {

    public HashingAlgorithm mockConstantFunction() {
        HashingAlgorithm mockedHashingAlgorithm = Mockito.mock(HashingAlgorithm.class);
        Mockito.when(mockedHashingAlgorithm.h(Mockito.any(String.class))).thenReturn("1234567890");
        Mockito.when(mockedHashingAlgorithm.getHashValueLength()).thenReturn(10);
        Mockito.when(mockedHashingAlgorithm.getSecretKey()).thenReturn(SecretKeyTestData.getKeyWrapped());
        Mockito.when(mockedHashingAlgorithm.getSecretKeyLength()).thenReturn(SecretKeyTestData.getKeyWrapped().getKey().length());
        return mockedHashingAlgorithm;
    }
}
