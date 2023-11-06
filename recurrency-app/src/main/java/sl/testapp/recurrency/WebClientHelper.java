package sl.testapp.recurrency;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class WebClientHelper {
	public static Mono<Integer> prepareWebclient(int n) {
		return WebClient.create("http://localhost:8081").get().uri("/fib/{n}", n)
				.accept(MediaType.TEXT_HTML)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(String.class);
					} else {
						return response.createException().flatMap(Mono::error);
					}
				}).map(Integer::parseInt);
	}

}
