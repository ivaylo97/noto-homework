package com.noto.homework.transactionprocessingservice.services;

import com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules.ValidationRule;
import com.noto.homework.transactionprocessingservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by Ivaylo Sapunarov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final Set<ValidationRule> transactionValidationRules;

    public void validate(Transaction transaction) {

        transactionValidationRules.parallelStream()
                .forEach(rule -> rule.apply(transaction));
        log.info("No fraudulent transactions detected.");
    }
}
