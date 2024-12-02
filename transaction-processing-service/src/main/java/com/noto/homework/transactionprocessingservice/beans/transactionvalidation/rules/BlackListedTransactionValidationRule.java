package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.exceptions.FraudTransactionDetectedException;
import com.noto.homework.transactionprocessingservice.model.Transaction;
import com.noto.homework.transactionprocessingservice.repositories.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Verifies that the transaction is not from a blacklisted country.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Component
public class BlackListedTransactionValidationRule implements ValidationRule {

    private final Set<String> blacklistedCountries;

    public BlackListedTransactionValidationRule(MongoRepository mongoRepository) {
        this.blacklistedCountries = Set.copyOf(mongoRepository.fetchBlacklistedCountries());
    }

    @Override
    public void apply(Transaction transaction) {
        if (blacklistedCountries.contains(transaction.getCountry())) {
            throw new FraudTransactionDetectedException("Transaction is from blacklisted country: " + transaction.getCountry());
        }
    }
}
