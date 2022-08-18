package sl.testapp.serverapp;

import reactor.core.publisher.Mono;

public interface GreetingReactiveService {
	Mono<String> getGreeting();
	

}
