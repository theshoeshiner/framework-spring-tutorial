package org.vaadin.spring.tutorial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

import com.salesforce.canvas.filter.CanvasFilter;
import com.vaadin.server.VaadinServlet;

@SpringBootApplication(scanBasePackageClasses = {CanvasFilter.class,CanvasFilterApplication.class})
public class CanvasFilterApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(CanvasFilterApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CanvasFilterApplication.class, args);
	}

	/*@Bean
	public VaadinServlet vaadinServlet() {
		LOGGER.info("returning vaadin servlet...");
		return new OAuthServlet();
	}*/

	
	/*	@Bean
		public EmbeddedServletContainerCustomizer customizer() {
			return new EmbeddedServletContainerCustomizer() {
	
				@Override
				public void customize(ConfigurableEmbeddedServletContainer container) {
					LOGGER.info("container: {}",container);
					LOGGER.info("container: {}",container.getClass());
					
				}
				
			};
	
		}*/
	
}
