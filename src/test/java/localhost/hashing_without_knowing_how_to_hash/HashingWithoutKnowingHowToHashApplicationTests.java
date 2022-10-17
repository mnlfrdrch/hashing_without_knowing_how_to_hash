package localhost.hashing_without_knowing_how_to_hash;

import mockdata.CommandLineTestData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
class HashingWithoutKnowingHowToHashApplicationTests {

	@Test
	void testApplicationDoesNotThrowException() {
		assertDoesNotThrow(()->{
            HashingWithoutKnowingHowToHashApplication.main(CommandLineTestData.CORRECT_ARGUMENTS_2P);});
	}
}
