package com.noto.homework.mockclient.controller;

import com.noto.homework.mockclient.beans.TransactionRequestSpewer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Defines endpoints from which we can trigger requests to the transaction processing application.
 * <p>
 * Created by Ivaylo Sapunarov - Delta Source Bulgaria on 12/1/24.
 */
@Slf4j
@RestController
@RequestMapping(MockClientController.CONTROLLER_BASE_PATH)
@RequiredArgsConstructor
public class MockClientController {

	public static final String CONTROLLER_BASE_PATH = "/mock-client/api";

	private final TransactionRequestSpewer requestSpewer;

	@GetMapping("/trigger")
	public ResponseEntity<String> triggerRequests(@RequestParam("intervalBetweenRequests") Long intervalBetweenRequests,
												  @RequestParam(value = "numberOfRequests") Integer numberOfRequests) {
		try {
			log.info("Triggering {} requests with interval: {}", numberOfRequests, intervalBetweenRequests);
			requestSpewer.spewRequestByNumber(numberOfRequests, intervalBetweenRequests);
			return ResponseEntity.ok("Transactions set successfully!");
		} catch (Exception e) {
			log.error("Failed to send transactions for params - interval: {}, requests: {} due to: {}",
				intervalBetweenRequests, numberOfRequests, e.getMessage());
			return ResponseEntity.internalServerError().body("Could not send transactions due to: " + e.getMessage());
		}
	}

	@GetMapping("/triggerForCountries")
	public ResponseEntity<String> triggerRequestsForDifferentCountries(@RequestParam("intervalBetweenRequests") Long intervalBetweenRequests,
																	   @RequestParam(name = "countries") List<String> countries) {
		if(countries == null || countries.isEmpty()) {
			return ResponseEntity.badRequest().body("Countries list cannot be empty!");
		}
		try {
			log.info("Triggering {} requests with interval: {}, with countries: {}",
				countries.size(), intervalBetweenRequests, countries);
			requestSpewer.spewRequestByCountry(intervalBetweenRequests, countries);
			return ResponseEntity.ok("Transactions set successfully!");
		} catch (Exception e) {
			log.error("Failed to send transactions for params - interval: {} countries: {} due to: {}",
				intervalBetweenRequests, countries, e.getMessage());
			return ResponseEntity.internalServerError().body("Could not send transactions due to: " + e.getMessage());
		}
	}
}
