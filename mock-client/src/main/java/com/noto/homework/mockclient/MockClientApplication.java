package com.noto.homework.mockclient;

import com.noto.homework.mockclient.beans.countryconfiguration.CountryLocationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(CountryLocationConfiguration.class)
@SpringBootApplication
public class MockClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockClientApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
