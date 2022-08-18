package sl.testapp.clientapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.test.StepVerifier;

@SpringBootTest
class SlTestWebClientApplicationTests {

	@Test
	void call() {
		WebClient client = WebClient.builder()
				  .baseUrl("http://localhost:8080")
				  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
				  .build();
		var result = client.get().uri(uriBuilder -> uriBuilder.path("/hello").queryParam("name", "Wohoo").build()).retrieve().bodyToMono(String.class);
		
		StepVerifier.create(result).expectSubscription().expectNext("Hello Wohoo").verifyComplete();
	}

}
