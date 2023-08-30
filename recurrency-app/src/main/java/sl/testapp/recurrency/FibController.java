package sl.testapp.recurrency;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class FibController {

	@GetMapping("/fib/{n}")
	public String fibn(@PathVariable int n) {
		log.debug("Received request for n=[" + n + "]");
		if(n<2) {
			log.debug("Returning response for n=[" + n + "]");
			return String.valueOf(n);
		} else {
			Mono<Integer> n_1 = WebClient.create("http://localhost:8081").get().uri("/fib/{n}", n-1)
					.accept(MediaType.TEXT_HTML)
					.exchange()
					.flatMap(resp -> resp.bodyToMono(String.class))
					.map(Integer::parseInt);
			
			Mono<Integer> n_2 = WebClient.create("http://localhost:8081").get().uri("/fib/{n}", n-2)
					.accept(MediaType.TEXT_HTML)
					.exchange()
					.flatMap(resp -> resp.bodyToMono(String.class))
					.map(Integer::parseInt);
			
			Integer a = n_1.block(Duration.ofSeconds(60));
			Integer b = n_2.block(Duration.ofSeconds(60));
			
			log.debug("Returning response for n=[" + n + "]");
			return String.valueOf(a+b); 
		}
	}
}
