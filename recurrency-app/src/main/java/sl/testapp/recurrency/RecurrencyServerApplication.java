package sl.testapp.recurrency;

import java.time.Duration;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
public class RecurrencyServerApplication {

	public static void main(String[] args) {
		var ctx = SpringApplication.run(RecurrencyServerApplication.class, args);

		Mono<Integer> result = WebClientHelper.prepareWebclient(10);

		try {
			System.out.println("STARTSTARTSTARTSTARTSTARTSTART");
			log.debug("STARTSTARTSTARTSTARTSTARTSTART");
			System.out.println(result.block(Duration.ofSeconds(60)));
			System.out.println("STARTSTARTSTARTSTARTSTARTSTART");
		} catch(Exception e) {
			log.error("STARTSTARTSTARTSTARTSTARTSTART");
			log.error("EXCEPTION WAS RAISED", e);
		} finally {
			SpringApplication.exit(ctx, () -> 0 );
		}
		
	}
}
