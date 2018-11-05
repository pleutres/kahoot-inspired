package com.gorki.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.TomcatServletWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer extends TomcatServletWebServerFactoryCustomizer implements ServletContextInitializer {

	private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

	@Inject
	private Environment env;

	public WebConfigurer(ServerProperties serverProperties) {
		super(serverProperties);
	}

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
		final EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD,
			DispatcherType.ASYNC);
		initCachingHttpHeadersFilter(servletContext, disps);
		//initStaticResourcesProductionFilter(servletContext, disps);
		initH2Console(servletContext);
		log.info("Web application fully configured");
	}

	/**
	 * Set up Mime types.
	 */
	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		super.customize(factory);
		final MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		// IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
		mappings.add("html", "text/html;charset=utf-8");
		// CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
		mappings.add("json", "text/html;charset=utf-8");
		factory.setMimeMappings(mappings);
	}


	/**
	 * Initializes the static resources production Filter.
	 */
	private void initStaticResourcesProductionFilter(final ServletContext servletContext,
		final EnumSet<DispatcherType> disps) {

		log.debug("Registering static resources production Filter");
		final FilterRegistration.Dynamic staticResourcesProductionFilter = servletContext.addFilter(
			"staticResourcesProductionFilter", new StaticResourcesProductionFilter());

		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/index.html");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
		staticResourcesProductionFilter.setAsyncSupported(true);
	}

	/**
	 * Initializes the cachig HTTP Headers Filter.
	 */
	private void initCachingHttpHeadersFilter(final ServletContext servletContext, final EnumSet<DispatcherType> disps) {
		log.debug("Registering Caching HTTP Headers Filter");
		final FilterRegistration.Dynamic cachingHttpHeadersFilter = servletContext.addFilter("cachingHttpHeadersFilter",
			new CachingHttpHeadersFilter());

		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
		cachingHttpHeadersFilter.setAsyncSupported(true);
	}


	/**
	 * Initializes H2 console
	 */
	private void initH2Console(final ServletContext servletContext) {
		log.debug("Initialize H2 console");
		final ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console",
			new org.h2.server.web.WebServlet());
		h2ConsoleServlet.addMapping("/console/*");
		h2ConsoleServlet.setInitParameter("-properties", "src/main/resources");
		h2ConsoleServlet.setLoadOnStartup(1);
	}

}
