package de.hhu.mentoring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import de.hhu.mentoring.services.storage.StorageProperties;
import de.hhu.mentoring.services.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MentoringApplication extends SpringBootServletInitializer{
	
	 @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	      return builder.sources(MentoringApplication.class);
	  }
	
	public static void main(String[] args) {
		SpringApplication.run(MentoringApplication.class, args);
	}
	
	@Bean
    CommandLineRunner init(StorageService storageService) {
    	return (args) -> {
    		String profile = System.getProperty("spring.profiles.active");
    		if (profile == null) {
    			// Only delete in development setup
    			storageService.deleteAll();
    		}
    		storageService.init();
    	};
    }
}

