package org.vaadin.spring.tutorial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.vaadin.server.VaadinServlet;

@SpringBootApplication
public class TutorialApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(TutorialApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TutorialApplication.class, args);
	}

	@Bean
	public VaadinServlet vaadinServlet() {
		LOGGER.info("returning vaadin servlet...");
		return new OAuthServlet();
	}

	/*
	 * @Bean public WebServerFactoryCustomizer<TomcatServletWebServerFactory>
	 * cookieProcessorCustomizer() {
	 * 
	 * return (factory) -> factory.addContextCustomizers((context) -> {
	 * LegacyCookieProcessor legacyCookieProcessor = new LegacyCookieProcessor();
	 * legacyCookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
	 * context.setCookieProcessor(legacyCookieProcessor); });
	 * 
	 * }
	 */
	
	@Bean
	public EmbeddedServletContainerCustomizer customizer() {
		return new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				LOGGER.info("container: {}",container);
				LOGGER.info("container: {}",container.getClass());
				
			}
			
		};
		/*
		 * return container -> { if (container instanceof
		 * TomcatEmbeddedServletContainerFactory) {
		 * TomcatEmbeddedServletContainerFactory tomcat =
		 * (TomcatEmbeddedServletContainerFactory) container;
		 * tomcat.addContextCustomizers(context -> context.setCookieProcessor(new
		 * LegacyCookieProcessor())); } };
		 */
	}
	
	@EventListener
	  public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		
		//CookieSerializer cs;
	    DefaultCookieSerializer cookieSerializer = applicationContext.getBean(DefaultCookieSerializer.class);
	    //log.info("Received DefaultCookieSerializer, Overriding SameSite Strict");
	    cookieSerializer.setSameSite("None");
	  }
}
