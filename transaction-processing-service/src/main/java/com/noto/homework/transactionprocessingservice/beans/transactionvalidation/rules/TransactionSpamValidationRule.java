package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.exceptions.FraudTransactionDetectedException;
import com.noto.homework.transactionprocessingservice.model.TransactionDocument;
import com.noto.homework.transactionprocessingservice.model.TransactionTO;
import com.noto.homework.transactionprocessingservice.repositories.MongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Checks if the user has made more than 10 transactions in the last 1 minute.
 * If yes, the current transaction is considered spammed, thus fraudulent.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Component
@RequiredArgsConstructor
public class TransactionSpamValidationRule implements ValidationRule {

    private final MongoRepository mongoRepository;

    @Override
    public void apply(TransactionTO transactionTO) {
        long userId = transactionTO.getUserId();
        ZonedDateTime lastOneMinuteTimestamp = ZonedDateTime.now().minusMinutes(1);
        List<TransactionDocument> transactions = mongoRepository.fetchTransactionAfterTimestamp(userId, lastOneMinuteTimestamp);
        if (transactions.size() > 10) {
            throw new FraudTransactionDetectedException(
                    "Fraudulent spammed transaction detected for user: " + transactionTO.getUserId());
        }
    }
}
