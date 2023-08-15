package sl.testapp.serverapp;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

//this one is used for configuring Jetty factory in -web starter
//overrides what is configured in application.properties
//@Component
public class JettyCustomizerWeb implements WebServerFactoryCustomizer<JettyServletWebServerFactory>{

	@Override
	public void customize(JettyServletWebServerFactory factory) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(1);
        threadPool.setMaxThreads(2);
        threadPool.setIdleTimeout(60000);
        factory.setThreadPool(threadPool);
	}

}
