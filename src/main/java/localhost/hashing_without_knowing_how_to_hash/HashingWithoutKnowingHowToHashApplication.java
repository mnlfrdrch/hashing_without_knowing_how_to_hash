package localhost.hashing_without_knowing_how_to_hash;

import localhost.hashing_without_knowing_how_to_hash.config.ArgumentsInterpreter;
import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class HashingWithoutKnowingHowToHashApplication {

	public static void main(String[] args) {
		//extract arguments from terminal
		ArgumentsInterpreter interpreter=new ArgumentsInterpreter(args);
		String port=interpreter.getOwnServerPort();
		HashFunctionSecretKeyDto secretKey=interpreter.getOwnHashFunctionKey();
		File modelFile=interpreter.getModelFile();
		List<HostDto> hosts=interpreter.getHosts();

		//set up spring
		SpringApplication springBoot=new SpringApplication(HashingWithoutKnowingHowToHashApplication.class);
		springBoot.setDefaultProperties(Collections.singletonMap("server.port", port));
		springBoot.run(new String[0]);

		//load formula cache
		HashingAlgorithm hashingAlgorithm= HashingAlgorithmFactory.instantiateHashingAlgorithmWithKey(ModelMetaData.ALGORITHM_TYPE, secretKey);
		Thread formulaCacheLoader = new FormulaCacheLoader(hashingAlgorithm);
		formulaCacheLoader.start();

		//start the protocol
		Thread protocolSequence=new ProtocolSequence(modelFile, hashingAlgorithm, hosts);
		protocolSequence.start();
	}
}
