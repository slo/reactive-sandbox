package sl.testapp.serverapp;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.web.embedded.jetty.JettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JettyCustomizer2 implements WebServerFactoryCustomizer<JettyReactiveWebServerFactory>{

	@Override
	public void customize(JettyReactiveWebServerFactory factory) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(2);
        threadPool.setMaxThreads(4);
        threadPool.setIdleTimeout(60000);
        factory.setThreadPool(threadPool);
	}

}
