package com.MicroGuardAI.aianalysisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AianalysisserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AianalysisserviceApplication.class, args);
	}
}