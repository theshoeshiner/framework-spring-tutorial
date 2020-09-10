package org.vaadin.spring.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.server.SpringVaadinServlet;

@SpringBootApplication
public class TutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(TutorialApplication.class, args);
    }
    
    @Bean
    public VaadinServlet vaadinServlet() {
    	
        return new OAuthServlet();
    }
}
