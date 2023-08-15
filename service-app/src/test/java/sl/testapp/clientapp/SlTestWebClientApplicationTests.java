package sl.testapp.clientapp;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import sl.testapp.clientapp.users.User;
import sl.testapp.clientapp.users.UserCache;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
@WireMockTest(httpPort = 9090)
class SlTestWebClientApplicationTests {
	
	private final UserCache cache;

	
	@LocalServerPort
	Integer port;
	
	
	CloseableHttpClient client;
	
	
	 @BeforeEach
	  void init() {
	    client = HttpClientBuilder.create()
	      .useSystemProperties() // This must be enabled for auto proxy config
	      .build();
	  }
	
	@Test
	void call() {
		WebClient client = WebClient.builder()
				  .baseUrl("http://localhost:8081")
				  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
				  .build();
		var result = client.get().uri(uriBuilder -> uriBuilder.path("/hello").queryParam("name", "Wohoo").build()).retrieve().bodyToMono(String.class);
		
		StepVerifier.create(result).expectSubscription().expectNext("Hello Wohoo").verifyComplete();
	}
	
	

	@Test
	void call2() throws Exception {
		
		stubFor(get("/users").withHost(equalTo("localhost")).willReturn(ok(prepareData()).withHeader("Content-Type", "application/json")));
		
 
		var client = WebClient.builder().baseUrl("http://localhost:9090/users")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
		var result = client.get().exchangeToFlux(response -> {
			if(HttpStatus.OK.equals(response.statusCode())) {
				return response.bodyToFlux(User.class);
			} else {
				//return Flux.error(new RuntimeException());
				return response.createException().flatMapMany(Mono::error);
			}
		});
		
		StepVerifier.create(result).expectNext(new User("mojombo",1l,"User","https://avatars.githubusercontent.com/u/1?v=4")).expectNextCount(30).verifyComplete();
	}
	
	
	private String prepareData() {
		return """
				[
	{
    "login": "mojombo",
    "id": 1,
    "node_id": "MDQ6VXNlcjE=",
    "avatar_url": "https://avatars.githubusercontent.com/u/1?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/mojombo",
    "html_url": "https://github.com/mojombo",
    "followers_url": "https://api.github.com/users/mojombo/followers",
    "following_url": "https://api.github.com/users/mojombo/following{/other_user}",
    "gists_url": "https://api.github.com/users/mojombo/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/mojombo/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/mojombo/subscriptions",
    "organizations_url": "https://api.github.com/users/mojombo/orgs",
    "repos_url": "https://api.github.com/users/mojombo/repos",
    "events_url": "https://api.github.com/users/mojombo/events{/privacy}",
    "received_events_url": "https://api.github.com/users/mojombo/received_events",
    "type": "User",
    "site_admin": false
  },
    {
      "login": "archfear",
      "id": 286,
      "node_id": "MDQ6VXNlcjI4Ng==",
      "avatar_url": "https://avatars.githubusercontent.com/u/286?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/archfear",
      "html_url": "https://github.com/archfear",
      "followers_url": "https://api.github.com/users/archfear/followers",
      "following_url": "https://api.github.com/users/archfear/following{/other_user}",
      "gists_url": "https://api.github.com/users/archfear/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/archfear/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/archfear/subscriptions",
      "organizations_url": "https://api.github.com/users/archfear/orgs",
      "repos_url": "https://api.github.com/users/archfear/repos",
      "events_url": "https://api.github.com/users/archfear/events{/privacy}",
      "received_events_url": "https://api.github.com/users/archfear/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "mudge",
      "id": 287,
      "node_id": "MDQ6VXNlcjI4Nw==",
      "avatar_url": "https://avatars.githubusercontent.com/u/287?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/mudge",
      "html_url": "https://github.com/mudge",
      "followers_url": "https://api.github.com/users/mudge/followers",
      "following_url": "https://api.github.com/users/mudge/following{/other_user}",
      "gists_url": "https://api.github.com/users/mudge/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/mudge/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/mudge/subscriptions",
      "organizations_url": "https://api.github.com/users/mudge/orgs",
      "repos_url": "https://api.github.com/users/mudge/repos",
      "events_url": "https://api.github.com/users/mudge/events{/privacy}",
      "received_events_url": "https://api.github.com/users/mudge/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "johnny",
      "id": 288,
      "node_id": "MDQ6VXNlcjI4OA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/288?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/johnny",
      "html_url": "https://github.com/johnny",
      "followers_url": "https://api.github.com/users/johnny/followers",
      "following_url": "https://api.github.com/users/johnny/following{/other_user}",
      "gists_url": "https://api.github.com/users/johnny/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/johnny/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/johnny/subscriptions",
      "organizations_url": "https://api.github.com/users/johnny/orgs",
      "repos_url": "https://api.github.com/users/johnny/repos",
      "events_url": "https://api.github.com/users/johnny/events{/privacy}",
      "received_events_url": "https://api.github.com/users/johnny/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "scott-steadman",
      "id": 290,
      "node_id": "MDQ6VXNlcjI5MA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/290?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/scott-steadman",
      "html_url": "https://github.com/scott-steadman",
      "followers_url": "https://api.github.com/users/scott-steadman/followers",
      "following_url": "https://api.github.com/users/scott-steadman/following{/other_user}",
      "gists_url": "https://api.github.com/users/scott-steadman/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/scott-steadman/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/scott-steadman/subscriptions",
      "organizations_url": "https://api.github.com/users/scott-steadman/orgs",
      "repos_url": "https://api.github.com/users/scott-steadman/repos",
      "events_url": "https://api.github.com/users/scott-steadman/events{/privacy}",
      "received_events_url": "https://api.github.com/users/scott-steadman/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "ciconia",
      "id": 291,
      "node_id": "MDQ6VXNlcjI5MQ==",
      "avatar_url": "https://avatars.githubusercontent.com/u/291?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/ciconia",
      "html_url": "https://github.com/ciconia",
      "followers_url": "https://api.github.com/users/ciconia/followers",
      "following_url": "https://api.github.com/users/ciconia/following{/other_user}",
      "gists_url": "https://api.github.com/users/ciconia/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/ciconia/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/ciconia/subscriptions",
      "organizations_url": "https://api.github.com/users/ciconia/orgs",
      "repos_url": "https://api.github.com/users/ciconia/repos",
      "events_url": "https://api.github.com/users/ciconia/events{/privacy}",
      "received_events_url": "https://api.github.com/users/ciconia/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "doudou",
      "id": 292,
      "node_id": "MDQ6VXNlcjI5Mg==",
      "avatar_url": "https://avatars.githubusercontent.com/u/292?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/doudou",
      "html_url": "https://github.com/doudou",
      "followers_url": "https://api.github.com/users/doudou/followers",
      "following_url": "https://api.github.com/users/doudou/following{/other_user}",
      "gists_url": "https://api.github.com/users/doudou/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/doudou/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/doudou/subscriptions",
      "organizations_url": "https://api.github.com/users/doudou/orgs",
      "repos_url": "https://api.github.com/users/doudou/repos",
      "events_url": "https://api.github.com/users/doudou/events{/privacy}",
      "received_events_url": "https://api.github.com/users/doudou/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "david",
      "id": 293,
      "node_id": "MDQ6VXNlcjI5Mw==",
      "avatar_url": "https://avatars.githubusercontent.com/u/293?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/david",
      "html_url": "https://github.com/david",
      "followers_url": "https://api.github.com/users/david/followers",
      "following_url": "https://api.github.com/users/david/following{/other_user}",
      "gists_url": "https://api.github.com/users/david/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/david/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/david/subscriptions",
      "organizations_url": "https://api.github.com/users/david/orgs",
      "repos_url": "https://api.github.com/users/david/repos",
      "events_url": "https://api.github.com/users/david/events{/privacy}",
      "received_events_url": "https://api.github.com/users/david/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "pedromg",
      "id": 294,
      "node_id": "MDQ6VXNlcjI5NA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/294?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/pedromg",
      "html_url": "https://github.com/pedromg",
      "followers_url": "https://api.github.com/users/pedromg/followers",
      "following_url": "https://api.github.com/users/pedromg/following{/other_user}",
      "gists_url": "https://api.github.com/users/pedromg/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/pedromg/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/pedromg/subscriptions",
      "organizations_url": "https://api.github.com/users/pedromg/orgs",
      "repos_url": "https://api.github.com/users/pedromg/repos",
      "events_url": "https://api.github.com/users/pedromg/events{/privacy}",
      "received_events_url": "https://api.github.com/users/pedromg/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "wayne",
      "id": 295,
      "node_id": "MDQ6VXNlcjI5NQ==",
      "avatar_url": "https://avatars.githubusercontent.com/u/295?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/wayne",
      "html_url": "https://github.com/wayne",
      "followers_url": "https://api.github.com/users/wayne/followers",
      "following_url": "https://api.github.com/users/wayne/following{/other_user}",
      "gists_url": "https://api.github.com/users/wayne/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/wayne/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/wayne/subscriptions",
      "organizations_url": "https://api.github.com/users/wayne/orgs",
      "repos_url": "https://api.github.com/users/wayne/repos",
      "events_url": "https://api.github.com/users/wayne/events{/privacy}",
      "received_events_url": "https://api.github.com/users/wayne/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "Steve",
      "id": 296,
      "node_id": "MDQ6VXNlcjI5Ng==",
      "avatar_url": "https://avatars.githubusercontent.com/u/296?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/Steve",
      "html_url": "https://github.com/Steve",
      "followers_url": "https://api.github.com/users/Steve/followers",
      "following_url": "https://api.github.com/users/Steve/following{/other_user}",
      "gists_url": "https://api.github.com/users/Steve/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/Steve/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/Steve/subscriptions",
      "organizations_url": "https://api.github.com/users/Steve/orgs",
      "repos_url": "https://api.github.com/users/Steve/repos",
      "events_url": "https://api.github.com/users/Steve/events{/privacy}",
      "received_events_url": "https://api.github.com/users/Steve/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "mark",
      "id": 297,
      "node_id": "MDQ6VXNlcjI5Nw==",
      "avatar_url": "https://avatars.githubusercontent.com/u/297?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/mark",
      "html_url": "https://github.com/mark",
      "followers_url": "https://api.github.com/users/mark/followers",
      "following_url": "https://api.github.com/users/mark/following{/other_user}",
      "gists_url": "https://api.github.com/users/mark/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/mark/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/mark/subscriptions",
      "organizations_url": "https://api.github.com/users/mark/orgs",
      "repos_url": "https://api.github.com/users/mark/repos",
      "events_url": "https://api.github.com/users/mark/events{/privacy}",
      "received_events_url": "https://api.github.com/users/mark/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "ambethia",
      "id": 298,
      "node_id": "MDQ6VXNlcjI5OA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/298?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/ambethia",
      "html_url": "https://github.com/ambethia",
      "followers_url": "https://api.github.com/users/ambethia/followers",
      "following_url": "https://api.github.com/users/ambethia/following{/other_user}",
      "gists_url": "https://api.github.com/users/ambethia/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/ambethia/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/ambethia/subscriptions",
      "organizations_url": "https://api.github.com/users/ambethia/orgs",
      "repos_url": "https://api.github.com/users/ambethia/repos",
      "events_url": "https://api.github.com/users/ambethia/events{/privacy}",
      "received_events_url": "https://api.github.com/users/ambethia/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "halfbyte",
      "id": 299,
      "node_id": "MDQ6VXNlcjI5OQ==",
      "avatar_url": "https://avatars.githubusercontent.com/u/299?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/halfbyte",
      "html_url": "https://github.com/halfbyte",
      "followers_url": "https://api.github.com/users/halfbyte/followers",
      "following_url": "https://api.github.com/users/halfbyte/following{/other_user}",
      "gists_url": "https://api.github.com/users/halfbyte/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/halfbyte/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/halfbyte/subscriptions",
      "organizations_url": "https://api.github.com/users/halfbyte/orgs",
      "repos_url": "https://api.github.com/users/halfbyte/repos",
      "events_url": "https://api.github.com/users/halfbyte/events{/privacy}",
      "received_events_url": "https://api.github.com/users/halfbyte/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "cannikin",
      "id": 300,
      "node_id": "MDQ6VXNlcjMwMA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/300?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/cannikin",
      "html_url": "https://github.com/cannikin",
      "followers_url": "https://api.github.com/users/cannikin/followers",
      "following_url": "https://api.github.com/users/cannikin/following{/other_user}",
      "gists_url": "https://api.github.com/users/cannikin/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/cannikin/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/cannikin/subscriptions",
      "organizations_url": "https://api.github.com/users/cannikin/orgs",
      "repos_url": "https://api.github.com/users/cannikin/repos",
      "events_url": "https://api.github.com/users/cannikin/events{/privacy}",
      "received_events_url": "https://api.github.com/users/cannikin/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "anthonylewis",
      "id": 301,
      "node_id": "MDQ6VXNlcjMwMQ==",
      "avatar_url": "https://avatars.githubusercontent.com/u/301?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/anthonylewis",
      "html_url": "https://github.com/anthonylewis",
      "followers_url": "https://api.github.com/users/anthonylewis/followers",
      "following_url": "https://api.github.com/users/anthonylewis/following{/other_user}",
      "gists_url": "https://api.github.com/users/anthonylewis/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/anthonylewis/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/anthonylewis/subscriptions",
      "organizations_url": "https://api.github.com/users/anthonylewis/orgs",
      "repos_url": "https://api.github.com/users/anthonylewis/repos",
      "events_url": "https://api.github.com/users/anthonylewis/events{/privacy}",
      "received_events_url": "https://api.github.com/users/anthonylewis/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "markcornick",
      "id": 302,
      "node_id": "MDQ6VXNlcjMwMg==",
      "avatar_url": "https://avatars.githubusercontent.com/u/302?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/markcornick",
      "html_url": "https://github.com/markcornick",
      "followers_url": "https://api.github.com/users/markcornick/followers",
      "following_url": "https://api.github.com/users/markcornick/following{/other_user}",
      "gists_url": "https://api.github.com/users/markcornick/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/markcornick/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/markcornick/subscriptions",
      "organizations_url": "https://api.github.com/users/markcornick/orgs",
      "repos_url": "https://api.github.com/users/markcornick/repos",
      "events_url": "https://api.github.com/users/markcornick/events{/privacy}",
      "received_events_url": "https://api.github.com/users/markcornick/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "itfische",
      "id": 303,
      "node_id": "MDQ6VXNlcjMwMw==",
      "avatar_url": "https://avatars.githubusercontent.com/u/303?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/itfische",
      "html_url": "https://github.com/itfische",
      "followers_url": "https://api.github.com/users/itfische/followers",
      "following_url": "https://api.github.com/users/itfische/following{/other_user}",
      "gists_url": "https://api.github.com/users/itfische/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/itfische/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/itfische/subscriptions",
      "organizations_url": "https://api.github.com/users/itfische/orgs",
      "repos_url": "https://api.github.com/users/itfische/repos",
      "events_url": "https://api.github.com/users/itfische/events{/privacy}",
      "received_events_url": "https://api.github.com/users/itfische/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "ivoencarnacao",
      "id": 304,
      "node_id": "MDQ6VXNlcjMwNA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/304?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/ivoencarnacao",
      "html_url": "https://github.com/ivoencarnacao",
      "followers_url": "https://api.github.com/users/ivoencarnacao/followers",
      "following_url": "https://api.github.com/users/ivoencarnacao/following{/other_user}",
      "gists_url": "https://api.github.com/users/ivoencarnacao/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/ivoencarnacao/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/ivoencarnacao/subscriptions",
      "organizations_url": "https://api.github.com/users/ivoencarnacao/orgs",
      "repos_url": "https://api.github.com/users/ivoencarnacao/repos",
      "events_url": "https://api.github.com/users/ivoencarnacao/events{/privacy}",
      "received_events_url": "https://api.github.com/users/ivoencarnacao/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "mcg",
      "id": 305,
      "node_id": "MDQ6VXNlcjMwNQ==",
      "avatar_url": "https://avatars.githubusercontent.com/u/305?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/mcg",
      "html_url": "https://github.com/mcg",
      "followers_url": "https://api.github.com/users/mcg/followers",
      "following_url": "https://api.github.com/users/mcg/following{/other_user}",
      "gists_url": "https://api.github.com/users/mcg/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/mcg/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/mcg/subscriptions",
      "organizations_url": "https://api.github.com/users/mcg/orgs",
      "repos_url": "https://api.github.com/users/mcg/repos",
      "events_url": "https://api.github.com/users/mcg/events{/privacy}",
      "received_events_url": "https://api.github.com/users/mcg/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "sco",
      "id": 306,
      "node_id": "MDQ6VXNlcjMwNg==",
      "avatar_url": "https://avatars.githubusercontent.com/u/306?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/sco",
      "html_url": "https://github.com/sco",
      "followers_url": "https://api.github.com/users/sco/followers",
      "following_url": "https://api.github.com/users/sco/following{/other_user}",
      "gists_url": "https://api.github.com/users/sco/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/sco/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/sco/subscriptions",
      "organizations_url": "https://api.github.com/users/sco/orgs",
      "repos_url": "https://api.github.com/users/sco/repos",
      "events_url": "https://api.github.com/users/sco/events{/privacy}",
      "received_events_url": "https://api.github.com/users/sco/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "norio",
      "id": 307,
      "node_id": "MDQ6VXNlcjMwNw==",
      "avatar_url": "https://avatars.githubusercontent.com/u/307?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/norio",
      "html_url": "https://github.com/norio",
      "followers_url": "https://api.github.com/users/norio/followers",
      "following_url": "https://api.github.com/users/norio/following{/other_user}",
      "gists_url": "https://api.github.com/users/norio/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/norio/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/norio/subscriptions",
      "organizations_url": "https://api.github.com/users/norio/orgs",
      "repos_url": "https://api.github.com/users/norio/repos",
      "events_url": "https://api.github.com/users/norio/events{/privacy}",
      "received_events_url": "https://api.github.com/users/norio/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "anttih",
      "id": 308,
      "node_id": "MDQ6VXNlcjMwOA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/308?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/anttih",
      "html_url": "https://github.com/anttih",
      "followers_url": "https://api.github.com/users/anttih/followers",
      "following_url": "https://api.github.com/users/anttih/following{/other_user}",
      "gists_url": "https://api.github.com/users/anttih/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/anttih/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/anttih/subscriptions",
      "organizations_url": "https://api.github.com/users/anttih/orgs",
      "repos_url": "https://api.github.com/users/anttih/repos",
      "events_url": "https://api.github.com/users/anttih/events{/privacy}",
      "received_events_url": "https://api.github.com/users/anttih/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "vintrepid",
      "id": 309,
      "node_id": "MDQ6VXNlcjMwOQ==",
      "avatar_url": "https://avatars.githubusercontent.com/u/309?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/vintrepid",
      "html_url": "https://github.com/vintrepid",
      "followers_url": "https://api.github.com/users/vintrepid/followers",
      "following_url": "https://api.github.com/users/vintrepid/following{/other_user}",
      "gists_url": "https://api.github.com/users/vintrepid/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/vintrepid/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/vintrepid/subscriptions",
      "organizations_url": "https://api.github.com/users/vintrepid/orgs",
      "repos_url": "https://api.github.com/users/vintrepid/repos",
      "events_url": "https://api.github.com/users/vintrepid/events{/privacy}",
      "received_events_url": "https://api.github.com/users/vintrepid/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "bigfleet",
      "id": 310,
      "node_id": "MDQ6VXNlcjMxMA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/310?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/bigfleet",
      "html_url": "https://github.com/bigfleet",
      "followers_url": "https://api.github.com/users/bigfleet/followers",
      "following_url": "https://api.github.com/users/bigfleet/following{/other_user}",
      "gists_url": "https://api.github.com/users/bigfleet/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/bigfleet/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/bigfleet/subscriptions",
      "organizations_url": "https://api.github.com/users/bigfleet/orgs",
      "repos_url": "https://api.github.com/users/bigfleet/repos",
      "events_url": "https://api.github.com/users/bigfleet/events{/privacy}",
      "received_events_url": "https://api.github.com/users/bigfleet/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "lypanov",
      "id": 311,
      "node_id": "MDQ6VXNlcjMxMQ==",
      "avatar_url": "https://avatars.githubusercontent.com/u/311?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/lypanov",
      "html_url": "https://github.com/lypanov",
      "followers_url": "https://api.github.com/users/lypanov/followers",
      "following_url": "https://api.github.com/users/lypanov/following{/other_user}",
      "gists_url": "https://api.github.com/users/lypanov/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/lypanov/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/lypanov/subscriptions",
      "organizations_url": "https://api.github.com/users/lypanov/orgs",
      "repos_url": "https://api.github.com/users/lypanov/repos",
      "events_url": "https://api.github.com/users/lypanov/events{/privacy}",
      "received_events_url": "https://api.github.com/users/lypanov/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "ropiku",
      "id": 312,
      "node_id": "MDQ6VXNlcjMxMg==",
      "avatar_url": "https://avatars.githubusercontent.com/u/312?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/ropiku",
      "html_url": "https://github.com/ropiku",
      "followers_url": "https://api.github.com/users/ropiku/followers",
      "following_url": "https://api.github.com/users/ropiku/following{/other_user}",
      "gists_url": "https://api.github.com/users/ropiku/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/ropiku/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/ropiku/subscriptions",
      "organizations_url": "https://api.github.com/users/ropiku/orgs",
      "repos_url": "https://api.github.com/users/ropiku/repos",
      "events_url": "https://api.github.com/users/ropiku/events{/privacy}",
      "received_events_url": "https://api.github.com/users/ropiku/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "rds",
      "id": 313,
      "node_id": "MDQ6VXNlcjMxMw==",
      "avatar_url": "https://avatars.githubusercontent.com/u/313?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/rds",
      "html_url": "https://github.com/rds",
      "followers_url": "https://api.github.com/users/rds/followers",
      "following_url": "https://api.github.com/users/rds/following{/other_user}",
      "gists_url": "https://api.github.com/users/rds/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/rds/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/rds/subscriptions",
      "organizations_url": "https://api.github.com/users/rds/orgs",
      "repos_url": "https://api.github.com/users/rds/repos",
      "events_url": "https://api.github.com/users/rds/events{/privacy}",
      "received_events_url": "https://api.github.com/users/rds/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "zmoazeni",
      "id": 314,
      "node_id": "MDQ6VXNlcjMxNA==",
      "avatar_url": "https://avatars.githubusercontent.com/u/314?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/zmoazeni",
      "html_url": "https://github.com/zmoazeni",
      "followers_url": "https://api.github.com/users/zmoazeni/followers",
      "following_url": "https://api.github.com/users/zmoazeni/following{/other_user}",
      "gists_url": "https://api.github.com/users/zmoazeni/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/zmoazeni/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/zmoazeni/subscriptions",
      "organizations_url": "https://api.github.com/users/zmoazeni/orgs",
      "repos_url": "https://api.github.com/users/zmoazeni/repos",
      "events_url": "https://api.github.com/users/zmoazeni/events{/privacy}",
      "received_events_url": "https://api.github.com/users/zmoazeni/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "edwinmoss",
      "id": 315,
      "node_id": "MDQ6VXNlcjMxNQ==",
      "avatar_url": "https://avatars.githubusercontent.com/u/315?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/edwinmoss",
      "html_url": "https://github.com/edwinmoss",
      "followers_url": "https://api.github.com/users/edwinmoss/followers",
      "following_url": "https://api.github.com/users/edwinmoss/following{/other_user}",
      "gists_url": "https://api.github.com/users/edwinmoss/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/edwinmoss/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/edwinmoss/subscriptions",
      "organizations_url": "https://api.github.com/users/edwinmoss/orgs",
      "repos_url": "https://api.github.com/users/edwinmoss/repos",
      "events_url": "https://api.github.com/users/edwinmoss/events{/privacy}",
      "received_events_url": "https://api.github.com/users/edwinmoss/received_events",
      "type": "User",
      "site_admin": false
    },
    {
      "login": "tomfarm",
      "id": 316,
      "node_id": "MDQ6VXNlcjMxNg==",
      "avatar_url": "https://avatars.githubusercontent.com/u/316?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/tomfarm",
      "html_url": "https://github.com/tomfarm",
      "followers_url": "https://api.github.com/users/tomfarm/followers",
      "following_url": "https://api.github.com/users/tomfarm/following{/other_user}",
      "gists_url": "https://api.github.com/users/tomfarm/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/tomfarm/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/tomfarm/subscriptions",
      "organizations_url": "https://api.github.com/users/tomfarm/orgs",
      "repos_url": "https://api.github.com/users/tomfarm/repos",
      "events_url": "https://api.github.com/users/tomfarm/events{/privacy}",
      "received_events_url": "https://api.github.com/users/tomfarm/received_events",
      "type": "User",
      "site_admin": false
    }
  ]
				""";
	}
	
	@Test
	@SneakyThrows
	void call3() {
		var client = WebClient.builder().baseUrl("https://api.github.com/users")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
		var result = client.get().exchangeToFlux(response -> {
			if(HttpStatus.OK.equals(response.statusCode())) {
				return response.bodyToFlux(User.class);
			} else {
				//return Flux.error(new RuntimeException());
				System.err.println("error!!!");
				return response.createException().flatMapMany(Mono::error);
			}
		});
		
		result.take(2).subscribe(item -> {
			System.out.println(item);
		});
		Thread.sleep(1000);
	}
	
	
	@Test
	@SneakyThrows
	void cacheOnlyPublish() {
		cache.getUserOnlyPublish(1).subscribe(user -> {
			log.debug("Received: " + user);	
		});
		
		log.debug("End1");
		Thread.sleep(10000);
		log.debug("End2");
	}
	
	@Test
	@SneakyThrows
	void cacheSubscWithPublish() {
		cache.getUserSubscWithPublish(1).subscribe(user -> {
			log.debug("Received: " + user);	
		});
		
		log.debug("End1");
		Thread.sleep(10000);
		log.debug("End2");
	}
}
