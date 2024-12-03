package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.exceptions.FraudTransactionDetectedException;
import com.noto.homework.transactionprocessingservice.model.TransactionTO;
import com.noto.homework.transactionprocessingservice.repositories.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Verifies that the transaction is not from a blacklisted country.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Component
public class BlackListedTransactionValidationRule implements ValidationRule {

    private final MongoRepository mongoRepository;

    public BlackListedTransactionValidationRule(MongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void apply(TransactionTO transactionTO) {
        List<String> blacklistedCountries = mongoRepository.fetchBlacklistedCountries();
        if (blacklistedCountries.contains(transactionTO.getCountry())) {
            throw new FraudTransactionDetectedException("TransactionTO is from blacklisted country: " + transactionTO.getCountry());
        }
    }
}
