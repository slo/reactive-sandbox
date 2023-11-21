package sl.testapp.serverapp;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.web.embedded.jetty.JettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

//this one is used for configuring Jetty factory in -webflux starter
//overrides what is configured in application.properties
@Component
public class JettyCustomizerWebflux implements WebServerFactoryCustomizer<JettyReactiveWebServerFactory>{

	@Override
	public void customize(JettyReactiveWebServerFactory factory) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(1);
        threadPool.setMaxThreads(4);
        //threadPool.setIdleTimeout(60000);
        threadPool.setName("jetty-generic");
        factory.setThreadPool(threadPool);
	}

}
