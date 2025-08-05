package com.renanloureiro.feature_flags.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.renanloureiro.feature_flags")
@EnableJpaRepositories(basePackages = "com.renanloureiro.feature_flags.infrastructure.repositories")
@EntityScan(basePackages = "com.renanloureiro.feature_flags.domain")
public class FeatureFlagsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeatureFlagsApplication.class, args);
	}

}
