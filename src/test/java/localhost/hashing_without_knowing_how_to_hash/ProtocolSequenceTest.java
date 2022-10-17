package localhost.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProtocolSequenceTest {

    ProtocolSequence protocolSequence;

    @BeforeEach
    public void setUp(){
        HashingAlgorithm hashingAlgorithm=HashingAlgorithmFactory.instantiateHashingAlgorithmWithRandomKey(ModelMetaData.ALGORITHM_TYPE);
        File file=new File(ModelMetaData.PATH);
        List<HostDto> hosts=new ArrayList<>();
        protocolSequence=new ProtocolSequence(file, hashingAlgorithm, hosts);
    }

    @Test
    public void testThreadDoesNotThrowException(){
        assertDoesNotThrow(()->{protocolSequence.run();});
    }
}