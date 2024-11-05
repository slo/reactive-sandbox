package sl.testapp.serverapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import io.restassured.RestAssured;


public class HelloFunctionalTest extends TestBase{

	@Autowired
	private WebTestClient webTestClient;
	
	@LocalServerPort
	private int port;

	@BeforeEach
	void restAssured() {
		RestAssured.port = port;
	}
	
	@Test
	//@Disabled("It would fail when blockhoud is turned on")
	void callItBadly() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/hello").queryParam("name", "Booo").build()).exchange()
				.expectStatus().isOk().expectBody()
				.consumeWith(response -> assertThat(response.getResponseBody()).asString().matches("Witaj..: Booo"));
	}
	
	@Test
	//@Disabled
	void callIt() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/hello2").queryParam("name", "Booo").build()).exchange()
				.expectStatus().isOk().expectBody()
				.consumeWith(response -> assertThat(response.getResponseBody()).asString().matches("Witaj..: Booo"));
	}
	
	@Test
	void callItRestAssured() {
		RestAssured.get("/hello2").then().statusCode(200);
	}

}
