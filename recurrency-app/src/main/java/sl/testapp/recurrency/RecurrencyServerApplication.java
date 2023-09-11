package sl.testapp.recurrency;

import java.time.Duration;

import org.springframework.boot.ExitCodeGenerator;
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
		var ctx = SpringApplication.run(RecurrencyServerApplication.class, args);

		Mono<Integer> result = WebClient.create("http://localhost:8081").get().uri("/fib/{n}", 30)
				.accept(MediaType.TEXT_HTML).exchange().flatMap(resp -> resp.bodyToMono(String.class))
				.map(Integer::parseInt);

		try {
			System.out.println("STARTSTARTSTARTSTARTSTARTSTART");
			log.debug("STARTSTARTSTARTSTARTSTARTSTART");
			System.out.println(result.block(Duration.ofSeconds(6000)));
			System.out.println("STARTSTARTSTARTSTARTSTARTSTART");
		} catch(Exception e) {
			log.error("STARTSTARTSTARTSTARTSTARTSTART");
			log.error("EXCEPTION WAS RAISED", e);
		} finally {
			SpringApplication.exit(ctx, () -> 0 );
		}
		
	}
}
