package sl.testapp.serverapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.test.StepVerifier;

public class HelloControllerTest extends TestBase {
	
	@Autowired
	private HelloController hc;

	@Test
	void check() {
		assertThat(hc).isNotNull();
	}
	
	
	@Test
	void fluxTest() {
		var result = hc.getHello2s("asdf").log();
		StepVerifier.create(result).expectSubscription().expectNextCount(5).verifyComplete();
	}

}
