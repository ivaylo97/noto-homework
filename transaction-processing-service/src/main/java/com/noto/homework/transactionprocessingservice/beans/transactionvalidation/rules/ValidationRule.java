package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.model.TransactionTO;

/**
 * Common interface for all validation rules.
 * <p>
 * Created by Ivaylo Sapunarov
 */
public interface ValidationRule {
    void apply(TransactionTO transactionTO);
}
