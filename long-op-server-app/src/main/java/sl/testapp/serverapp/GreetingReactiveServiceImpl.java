package sl.testapp.serverapp;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import sl.testapp.otherservice.GreetingService;

@Service
@AllArgsConstructor
public class GreetingReactiveServiceImpl implements GreetingReactiveService {

	private final GreetingService greetingService;
	
	@Override
	public Mono<String> getGreeting() {
		//1
//		try {
//			return Mono.just(greetingService.getGreeting());
//		} catch (InterruptedException e) {
//			return Mono.error(e);
//		}
		//2
//		return Mono.fromCallable(greetingService::getGreeting);
		//3
		return Mono.fromCallable(greetingService::getGreeting).subscribeOn(Schedulers.boundedElastic());
		
	}

}
