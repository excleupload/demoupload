package com.example.tapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
public class TappApplication {

	public static void main(String[] args) {
		SpringApplication.run(TappApplication.class, args);
	}

	 @Bean
	    public MessageSource messageSource() {
	        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
	        source.setBasename("classpath:NotificationMessage");
	        source.setDefaultEncoding("UTF-8");
	        return source;
	    }
}
