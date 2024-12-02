package com.noto.homework.transactionprocessingservice.controller;

import com.noto.homework.transactionprocessingservice.exceptions.FraudTransactionDetectedException;
import com.noto.homework.transactionprocessingservice.model.Transaction;
import com.noto.homework.transactionprocessingservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

/**
 * Exposes endpoints, from which the application can receive {@link Transaction}s for processing.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(TransactionProcessingServiceController.TRANSACTION_PROCESSING_SERVICE_BASE_PATH)
public class TransactionProcessingServiceController {

    public static final String TRANSACTION_PROCESSING_SERVICE_BASE_PATH = "/transaction-processing-service/api";

    private final TransactionService transactionService;

    @PermitAll
    @PostMapping("/process")
    public ResponseEntity<String> process(@RequestBody Transaction transaction) {
        log.info("Processing transaction: {}", transaction);
        try {
            transactionService.validate(transaction);
            return ResponseEntity.ok().body("OK");
        } catch (FraudTransactionDetectedException e) {
            log.info("Transaction: {} is fraudulent due to: {}", transaction, e.getMessage());
            return ResponseEntity.ok().body("Transaction: " + transaction + " is fraudulent due to: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process transaction: {} due to: {}", transaction, e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to process transaction: " + transaction + " due to: " + e.getMessage());
        }
    }
}
