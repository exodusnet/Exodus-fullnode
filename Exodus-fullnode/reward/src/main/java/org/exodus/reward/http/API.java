package org.exodus.reward.http;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.exodus.reward.RWD;
import org.exodus.reward.util.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "api")
@Component
public final class API {

	private Server apiServer;
	@Autowired
	private APIServlet servlet;

	@PostConstruct
	public void init() {
		boolean enableAPIServer = RWD.getBooleanProperty("rwd.enableAPIServer");
		if (enableAPIServer) {
			final int port = RWD.getIntProperty("rwd.apiServerPort");
			final String host = RWD.getStringProperty("rwd.apiServerHost");
			apiServer = new Server();
			ServerConnector connector;

			boolean enableSSL = RWD.getBooleanProperty("rwd.apiSSL");
			if (enableSSL) {
				log.info("Using SSL (https) for the API server");
				HttpConfiguration https_config = new HttpConfiguration();
				https_config.setSecureScheme("https");
				https_config.setSecurePort(port);
				https_config.addCustomizer(new SecureRequestCustomizer());
				SslContextFactory sslContextFactory = new SslContextFactory();
				sslContextFactory.setKeyStorePath(RWD.getStringProperty("nxt.keyStorePath"));
				sslContextFactory.setKeyStorePassword(RWD.getStringProperty("nxt.keyStorePassword"));
				sslContextFactory.setExcludeCipherSuites("SSL_RSA_WITH_DES_CBC_SHA", "SSL_DHE_RSA_WITH_DES_CBC_SHA",
						"SSL_DHE_DSS_WITH_DES_CBC_SHA", "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
						"SSL_RSA_EXPORT_WITH_DES40_CBC_SHA", "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
						"SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");
				sslContextFactory.setExcludeProtocols("SSLv3");
				connector = new ServerConnector(apiServer, new SslConnectionFactory(sslContextFactory, "http/1.1"),
						new HttpConnectionFactory(https_config));
			} else {
				connector = new ServerConnector(apiServer);
			}

			connector.setPort(port);
			connector.setHost(host);
			connector.setIdleTimeout(RWD.getIntProperty("rwd.apiServerIdleTimeout"));
			connector.setReuseAddress(true);
			apiServer.addConnector(connector);

			HandlerList apiHandlers = new HandlerList();

			ServletContextHandler apiHandler = new ServletContextHandler();
			String apiResourceBase = RWD.getStringProperty("rwd.apiResourceBase");
			if (apiResourceBase != null) {
				ServletHolder defaultServletHolder = new ServletHolder(new DefaultServlet());
				defaultServletHolder.setInitParameter("dirAllowed", "false");
				defaultServletHolder.setInitParameter("resourceBase", apiResourceBase);
				defaultServletHolder.setInitParameter("welcomeServlets", "true");
				defaultServletHolder.setInitParameter("redirectWelcome", "true");
				defaultServletHolder.setInitParameter("gzip", "true");
				apiHandler.addServlet(defaultServletHolder, "/*");
				apiHandler.setWelcomeFiles(new String[] { "index.html" });
			}

			String javadocResourceBase = RWD.getStringProperty("rwd.javadocResourceBase");
			if (javadocResourceBase != null) {
				ContextHandler contextHandler = new ContextHandler("/doc");
				ResourceHandler docFileHandler = new ResourceHandler();
				docFileHandler.setDirectoriesListed(false);
				docFileHandler.setWelcomeFiles(new String[] { "index.html" });
				docFileHandler.setResourceBase(javadocResourceBase);
				contextHandler.setHandler(docFileHandler);
				apiHandlers.addHandler(contextHandler);
			}

			apiHandler.addServlet(new ServletHolder(servlet), "/reward");
			if (RWD.getBooleanProperty("rwd.enableAPIServerGZIPFilter")) {
				FilterHolder gzipFilterHolder = apiHandler.addFilter(GzipFilter.class, "/reward", null);
				gzipFilterHolder.setInitParameter("methods", "GET,POST");
				gzipFilterHolder.setAsyncSupported(true);
			}

//			apiHandler.addServlet(APITestServlet.class, "/test");
//			if (enableDebugAPI) {
//				apiHandler.addServlet(DbShellServlet.class, "/dbshell");
//			}

			if (RWD.getBooleanProperty("rwd.apiServerCORS")) {
				FilterHolder filterHolder = apiHandler.addFilter(CrossOriginFilter.class, "/*", null);
				filterHolder.setInitParameter("allowedHeaders", "*");
				filterHolder.setAsyncSupported(true);
			}

			apiHandlers.addHandler(apiHandler);
			apiHandlers.addHandler(new DefaultHandler());

			apiServer.setHandler(apiHandlers);
			apiServer.setStopAtShutdown(true);

			ThreadPool.runBeforeStart(new Runnable() {
				@Override
				public void run() {
					try {
						apiServer.start();
						log.info("Started API server at " + host + ":" + port);
					} catch (Exception e) {
						log.error("Failed to start API server", e);
						throw new RuntimeException(e.toString(), e);
					}

				}
			}, true);

		} else {
			apiServer = null;
			log.info("API server not enabled");
		}
	}

	@PreDestroy
	public void shutdown() {
		if (apiServer != null) {
			try {
				apiServer.stop();
			} catch (Exception e) {
				log.error("Failed to stop API server", e);
			}
		}
	}
}
