package org.vaadin.spring.tutorial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.server.SpringVaadinServlet;

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
}
