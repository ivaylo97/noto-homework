package com.noto.homework.mockclient.beans;

import com.noto.homework.mockclient.beans.countryconfiguration.Coordinates;
import com.noto.homework.mockclient.beans.countryconfiguration.CountryLocationConfiguration;
import com.noto.homework.mockclient.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Component, used to simulate users sending transactions to the transaction processing service.
 * <p>
 * Created by Ivaylo Sapunarov - Delta Source Bulgaria on 12/1/24.
 */
@Slf4j
@Component
public class TransactionRequestSpewer {

	private static final String DEFAULT_COUNTRY = "Bulgaria";

	private final RestTemplate restTemplate;

	private final String transactionServiceUrl;

	private final UserIdGenerator userIdGenerator;

	private final CountryLocationConfiguration countryLocationConfiguration;

	public TransactionRequestSpewer(RestTemplate restTemplate,
									@Value("${com.noto.homework.mockclient.transaction.processing.service.url}") String transactionServiceUrl, UserIdGenerator userIdGenerator, CountryLocationConfiguration countryLocationConfiguration) {
		this.restTemplate = restTemplate;
		this.transactionServiceUrl = transactionServiceUrl;
		this.userIdGenerator = userIdGenerator;
		this.countryLocationConfiguration = countryLocationConfiguration;
	}

	public void spewRequestByNumber(int numberOfRequests, long intervalBetweenRequests) throws InterruptedException {
		for (int requestNumber = 0; requestNumber < numberOfRequests; requestNumber++) {
			Transaction transaction = buildTransaction(DEFAULT_COUNTRY);
			spewRequest(transaction);
			Thread.sleep(intervalBetweenRequests);
		}
	}

	public void spewRequestByCountry(long intervalBetweenRequests, List<String> countries) throws InterruptedException {
		for(String country : countries) {
			Transaction transaction = buildTransaction(country);
			spewRequest(transaction);
			Thread.sleep(intervalBetweenRequests);
		}
	}

	private void spewRequest(Transaction transaction) {
		restTemplate.postForEntity(transactionServiceUrl, transaction, String.class);

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
