package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.model.Transaction;

/**
 * Created by Ivaylo Sapunarov 
 */
public abstract class ValidationRule {
    
        public abstract void apply(Transaction transaction);
}
