package org.kgromov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = {"org.kgromov.config"})
@SpringBootApplication
public class SpringAiOllamaWebSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiOllamaWebSearchApplication.class, args);
	}

}
