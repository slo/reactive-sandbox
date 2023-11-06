package sl.testapp.recurrency;

import java.time.Duration;

import org.springframework.http.HttpStatus;
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
	public Mono<String> fibn(@PathVariable int n) {
		log.debug("Received request for n=[" + n + "]");
		if(n<2) {
			log.debug("Returning response for n=[" + n + "]");
			return Mono.just(String.valueOf(n));
		} else {
			Mono<Integer> n_1 = WebClientHelper.prepareWebclient(n-1);
			
			Mono<Integer> n_2 = WebClientHelper.prepareWebclient(n-2);
			
			log.debug("Returning 'mono' for n=[" + n + "]");
			return n_1.flatMap(a -> n_2.map(b -> a+b)).map(String::valueOf);
		}
	}
}
