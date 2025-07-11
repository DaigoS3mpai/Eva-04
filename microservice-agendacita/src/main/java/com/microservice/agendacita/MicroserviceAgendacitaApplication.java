package com.microservice.agendacita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceAgendacitaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAgendacitaApplication.class, args);
	}

}
