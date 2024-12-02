package com.noto.homework.mockclient.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noto.homework.mockclient.beans.countryconfiguration.Coordinates;
import com.noto.homework.mockclient.beans.countryconfiguration.CountryLocationConfiguration;
import com.noto.homework.mockclient.domain.Transaction;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Component, used to simulate users sending transactions to the transaction processing service.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Slf4j
@Component
public class TransactionRequestSpewer {

	private static final String DEFAULT_COUNTRY = "Bulgaria";

	private final RestTemplate restTemplate;

	private final String transactionServiceUrl;

	private final UserIdGenerator userIdGenerator;

	private final CountryLocationConfiguration countryLocationConfiguration;

	private final ObjectMapper objectMapper;

	public TransactionRequestSpewer(RestTemplate restTemplate,
									@Value("${com.noto.homework.mockclient.transaction.processing.service.url}")
										String transactionServiceUrl, UserIdGenerator userIdGenerator,
									CountryLocationConfiguration countryLocationConfiguration,
									ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.transactionServiceUrl = transactionServiceUrl;
		this.userIdGenerator = userIdGenerator;
		this.countryLocationConfiguration = countryLocationConfiguration;
		this.objectMapper = objectMapper;
	}

	public void spewRequestByNumber(int numberOfRequests, long intervalBetweenRequests)
		throws InterruptedException, JsonProcessingException {
		for (int requestNumber = 0; requestNumber < numberOfRequests; requestNumber++) {
			Transaction transaction = buildTransaction(DEFAULT_COUNTRY);
			sendRequest(transaction);
			Thread.sleep(intervalBetweenRequests);
		}
	}

	public void spewRequestByCountry(long intervalBetweenRequests, List<String> countries)
		throws InterruptedException, JsonProcessingException {
		for(String country : countries) {
			Transaction transaction = buildTransaction(country);
			sendRequest(transaction);
			Thread.sleep(intervalBetweenRequests);
		}
	}

	private void sendRequest(Transaction transaction) throws JsonProcessingException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		String body = objectMapper.writeValueAsString(transaction);
		HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);
		restTemplate.postForEntity(transactionServiceUrl, httpEntity, String.class);
	}

	private Transaction buildTransaction(String country) {
		Coordinates countryCoordinates = countryLocationConfiguration.getCoordinates(country);
		return Transaction.builder()
			.userId(userIdGenerator.generateUserId())
			.transactionId(ZonedDateTime.now().toEpochSecond()) // To simulate unique transaction id
			.country(country)
			.amount("100")
			.latCoord(countryCoordinates.getLatitude())
			.longCoord(countryCoordinates.getLongitude())
			.timestamp(ZonedDateTime.now())
			.build();
	}
}
