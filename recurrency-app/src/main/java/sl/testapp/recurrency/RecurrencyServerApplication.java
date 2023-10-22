package sl.testapp.recurrency;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
public class RecurrencyServerApplication {

	public static void main(String[] args) throws URISyntaxException {
		var ctx = SpringApplication.run(RecurrencyServerApplication.class, args);

		
		var client = HttpClient.newBuilder().connectTimeout(Duration.ofMinutes(1000)).build();
		var N = "4";
		var request = HttpRequest.newBuilder(new URI("http://localhost:8081/fib/" + N))
				.header("Accept", MediaType.TEXT_HTML_VALUE)
				.header("tracen", N)
				.GET()
				.build();
				

		try {
			System.out.println("STARTSTARTSTARTSTARTSTARTSTART");
			log.debug("STARTSTARTSTARTSTARTSTARTSTART");
			var result = client.send(request, BodyHandlers.ofString());
			System.out.println(result.body());
			System.out.println("RESULTRESULTRESULTRESULTRESULT");
			log.debug("RESULTRESULTRESULTRESULTRESULT");
		} catch(Exception e) {
			log.error("STARTSTARTSTARTSTARTSTARTSTART");
			log.error("EXCEPTION WAS RAISED", e);
		} finally {
			SpringApplication.exit(ctx, () -> 0 );
		}
		
	}
}
