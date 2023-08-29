package sl.testapp.recurrency;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
public class RecurrencyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecurrencyServerApplication.class, args);

		Mono<Integer> result = WebClient.create("http://localhost:8081").get().uri("/fib/{n}", 20)
				.accept(MediaType.TEXT_HTML).exchange().flatMap(resp -> resp.bodyToMono(String.class))
				.map(Integer::parseInt);

		System.out.println("BOBEKBOBEKBOBEKBOBEKBOBEKBOBEKBOBEK");
		System.out.println(result.block(Duration.ofMinutes(1)));
		System.out.println("BOBEKBOBEKBOBEKBOBEKBOBEKBOBEKBOBEK");
		System.exit(0);
	}
}
