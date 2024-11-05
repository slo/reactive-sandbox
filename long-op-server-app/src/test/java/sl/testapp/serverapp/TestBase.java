package sl.testapp.serverapp;

import java.time.Duration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = AutowireMode.ALL)
@AutoConfigureWebTestClient(timeout = "PT15M")
abstract class TestBase {
	
	@TestConfiguration
	static class MyConfig {
	
	@Bean
	WebTestClientBuilderCustomizer customize() {
		return (b) -> {
			b.responseTimeout(Duration.ofSeconds(750));
		};
	}
	}

}
