package sl.testapp.serverapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
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
		Thread.sleep(10000);
		 var iks = greetingService.getGreeting() + ": " + name;
		 log.debug("PO HELLOOOOO!!!!!");
		 return iks;
	}
	
	
	@GetMapping("/hello2")
	public Mono<String> getHello2(String name) {
		log.debug("HELLOOOOO2!!!!!");
		var res = greetingReactiveService.getGreeting().map(greeting -> greeting + ": " + name).map(elem -> {try {
			log.debug("I sleep ????");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elem;}).publishOn(Schedulers.newBoundedElastic(1, 3, "elalastic")).doOnNext(c -> log.debug("AFTER MONO HELLO"));
		log.debug("PO HELLOOOOO2!!!!!");
		return res;
		
	}
	
	@GetMapping("/hello2s")
	public Flux<String> getHello2s(String name){
		log.debug("HELLOOOOO2S!!!!!");
		return greetingReactiveService.getGreeting().repeat(4).map(greeting -> greeting + ": " + name);
	}
}
