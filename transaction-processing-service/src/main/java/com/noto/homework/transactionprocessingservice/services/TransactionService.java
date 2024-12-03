package com.noto.homework.transactionprocessingservice.services;

import com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules.ValidationRule;
import com.noto.homework.transactionprocessingservice.model.TransactionDocument;
import com.noto.homework.transactionprocessingservice.model.TransactionTO;
import com.noto.homework.transactionprocessingservice.repositories.MongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Contains logic related to validating and persisting transactions.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final String POINT = "Point";

    private final Set<ValidationRule> transactionValidationRules;

    private final MongoRepository mongoRepository;

    public void processTransaction(TransactionTO transactionTO) {
        persistTransaction(transactionTO);
        validate(transactionTO);
    }

    private void persistTransaction(TransactionTO transactionTO) {
        TransactionDocument transaction = mapToDocument(transactionTO);
        mongoRepository.persistTransaction(transaction);
        log.info("Persisted transaction: {}", transaction);
    }

    private void validate(TransactionTO transactionTO) {
        transactionValidationRules.parallelStream()
                .forEach(rule -> rule.apply(transactionTO));
        log.info("No fraudulent transactions detected.");
    }

    private TransactionDocument mapToDocument(TransactionTO transactionTO) {
        String timestamp = transactionTO.getTimestamp()
                .toInstant()
                .toString();
        return TransactionDocument.builder()
                .transactionId(transactionTO.getTransactionId())
                .amount(transactionTO.getAmount())
                .country(transactionTO.getCountry())
                .userId(transactionTO.getUserId())
                .timestamp(timestamp)
                .longCoord(transactionTO.getLongCoord())
                .latCoord(transactionTO.getLatCoord())
                .build();
    }
}
