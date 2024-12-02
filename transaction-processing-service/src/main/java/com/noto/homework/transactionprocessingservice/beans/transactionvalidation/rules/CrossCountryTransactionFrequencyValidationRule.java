package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.exceptions.FraudTransactionDetectedException;
import com.noto.homework.transactionprocessingservice.model.Transaction;
import com.noto.homework.transactionprocessingservice.repositories.MongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Checks if the user has made more than 3 cross-country transactions in the last 10 minutes.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Component
@RequiredArgsConstructor
public class CrossCountryTransactionFrequencyValidationRule implements ValidationRule {

    private final MongoRepository mongoRepository;

    @Override
    public void apply(Transaction transaction) {
        long userId = transaction.getUserId();
        ZonedDateTime timestamp = ZonedDateTime.now().minusMinutes(10);
        List<Document> transactions =
                mongoRepository.fetchTransactionsDistinctByCountryAfterTimestamp(userId, timestamp);
        if (transactions.size() > 3) {
            throw new FraudTransactionDetectedException("Fraudulent cross-country transaction detected: " + transaction);
        }
    }
}
