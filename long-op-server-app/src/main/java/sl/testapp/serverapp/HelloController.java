package sl.testapp.serverapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sl.testapp.otherservice.GreetingService;

@RestController
@Slf4j
@AllArgsConstructor
class HelloController {
	
	private final GreetingService greetingService;
	private final GreetingReactiveService greetingReactiveService;

	@GetMapping("/hello")
	public String getHello(String name) throws InterruptedException {
		log.debug("HELLOOOOO!!!!!");
		return greetingService.getGreeting() + ": " + name;
	}
	
	
	@GetMapping("/hello2")
	public Mono<String> getHello2(String name) {
		log.debug("HELLOOOOO2!!!!!");
		return greetingReactiveService.getGreeting().map(greeting -> greeting + ": " + name);
	}
	
	@GetMapping("/hello2s")
	public Flux<String> getHello2s(String name){
		log.debug("HELLOOOOO2S!!!!!");
		return greetingReactiveService.getGreeting().repeat(4).map(greeting -> greeting + ": " + name);
	}
}
