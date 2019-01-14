package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import domain.AnnotatedHello;

@Configuration
public class AnnotatedHelloConfig {

	@Bean
	public AnnotatedHello annotatedHello() {
		return new AnnotatedHello();
	}
}
