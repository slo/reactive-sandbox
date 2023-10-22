package sl.testapp.recurrency;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@Slf4j
public class FibController {

	@GetMapping("/fib/{n}")
	public Mono<String> fibn(@PathVariable int n, @RequestHeader("tracen") String trace) throws URISyntaxException, IOException, InterruptedException {
		log.debug("Received request for n=[" + n + "]");
		log.debug("Received request for trace=[" + trace + "]");
		if(n<2) {
			log.debug("Returning response for n=[" + trace + "]");
			//return Mono.just(String.valueOf(n));
			return Mono.fromCallable(() -> {
				log.debug("Sleepy");
				//Thread.sleep(5000);
				return "1";
			});
		} else {
			var client = HttpClient.newBuilder().connectTimeout(Duration.ofMinutes(1000)).build();
			var request_n_1 = HttpRequest.newBuilder(new URI("http://localhost:8081/fib/" + (n-1)))
					.header("Accept", MediaType.TEXT_HTML_VALUE)
					.header("tracen", trace +", " + (n-1))
					.GET()
					.build();
			
			
			var request_n_2 = HttpRequest.newBuilder(new URI("http://localhost:8081/fib/" + (n-2)))
					.header("Accept", MediaType.TEXT_HTML_VALUE)
					.header("tracen", trace +", " + (n-2))
					.GET()
					.build();
			
			
			var result_n_1 = client.sendAsync(request_n_1, BodyHandlers.ofString());
			
			var result_n_2 = client.sendAsync(request_n_2, BodyHandlers.ofString());
			
						
			var mono_n_1 = Mono.fromFuture(result_n_1).subscribeOn(Schedulers.boundedElastic()).publishOn(Schedulers.boundedElastic());
			
			var mono_n_2 = Mono.fromFuture(result_n_2).subscribeOn(Schedulers.boundedElastic()).publishOn(Schedulers.boundedElastic());
			
			
			log.debug("Returning 'mono' for n=[" + trace + "]");
			return mono_n_1.flatMap(a -> mono_n_2.map(b -> Integer.parseInt(a.body())+Integer.parseInt(b.body()))).doOnNext(s -> log.debug("Returning value for trace=[" + trace +"]")).map(String::valueOf);
		}
	}
}
