package sl.testapp.clientapp;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import sl.testapp.clientapp.users.User;

@Configuration
@Slf4j
public class MyConfiguration {

	@Bean
	AsyncLoadingCache<Integer, User> userLoadingCacheOnlyPublish(AsyncCacheLoader<Integer, User> userLoaderOnlyPublish) {
		//CaffeineCacheMetrics.monitor(meterRegistry,buildCache(loader),"nazwaCache'a") 
		return buildCache(userLoaderOnlyPublish);

	}
	
	@Bean
	AsyncLoadingCache<Integer, User> userLoadingCacheSubscWithPublish(AsyncCacheLoader<Integer, User> userLoaderSubscWithPublish) {
		//CaffeineCacheMetrics.monitor(meterRegistry,buildCache(loader),"nazwaCache'a") 
		return buildCache(userLoaderSubscWithPublish);

	}

	private AsyncLoadingCache<Integer, User> buildCache(AsyncCacheLoader<Integer, User> loader) {
		return Caffeine.newBuilder()
				.maximumSize(10)
				.expireAfterWrite(Duration.ofSeconds(10))
				//.refreshAfterWrite(Duration.ofSeconds(1))
				.buildAsync(loader);
	}
	
	
	@Bean
	AsyncCacheLoader<Integer, User> userLoaderOnlyPublish() {
		return new AsyncCacheLoader<Integer, User>() {

			@Override
			public CompletableFuture<? extends User> asyncLoad(Integer key, Executor executor) throws Exception {
				return Mono.fromCallable(() -> {
					log.debug("Getting1!!");
					Thread.sleep(4110);
					log.debug("Getting2!!");
					return new User("login", key.longValue(), "asd", "asdf");
				})
						//.subscribeOn(Schedulers.fromExecutor(executor))
						.doOnNext(elem -> log.debug("Got element!!! " + elem + "!!!!!"))
						.publishOn(Schedulers.newBoundedElastic(2, 2, "publisher"))
						.doOnNext(elem -> log.debug("Got element2!!! " + elem + "!!!!!"))
						.toFuture();
			}
			
		};
	}
	
	@Bean
	AsyncCacheLoader<Integer, User> userLoaderSubscWithPublish() {
		return new AsyncCacheLoader<Integer, User>() {

			@Override
			public CompletableFuture<? extends User> asyncLoad(Integer key, Executor executor) throws Exception {
				return Mono.fromCallable(() -> {
					log.debug("Getting1!!");
					Thread.sleep(4110);
					log.debug("Getting2!!");
					return new User("login", key.longValue(), "asd", "asdf");
				})
						//.subscribeOn(Schedulers.fromExecutor(executor))
						.subscribeOn(Schedulers.newBoundedElastic(2, 2, "subscriber"))
						.doOnNext(elem -> log.debug("Got element!!! " + elem + "!!!!!"))
						.publishOn(Schedulers.newBoundedElastic(2, 2, "publisher"))
						.doOnNext(elem -> log.debug("Got element2!!! " + elem + "!!!!!"))
						.toFuture();
			}
			
		};
	}

}
