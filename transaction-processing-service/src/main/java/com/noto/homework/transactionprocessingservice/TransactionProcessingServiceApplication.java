package com.noto.homework.transactionprocessingservice;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableMongock
@SpringBootApplication
public class TransactionProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionProcessingServiceApplication.class, args);
	}
}
