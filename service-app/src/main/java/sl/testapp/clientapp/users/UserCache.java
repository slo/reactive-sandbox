package sl.testapp.clientapp.users;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.AllArgsConstructor;
import lombok.Value;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserCache {

	private final AsyncLoadingCache<Integer, User> userLoadingCacheOnlyPublish;
	
	private final AsyncLoadingCache<Integer, User> userLoadingCacheSubscWithPublish;


	public Mono<User> getUserOnlyPublish(Integer key) {
		return Mono.fromCompletionStage(() -> userLoadingCacheOnlyPublish.get(key));
	}
	
	public Mono<User> getUserSubscWithPublish(Integer key) {
		return Mono.fromCompletionStage(() -> userLoadingCacheSubscWithPublish.get(key));
	}

}
