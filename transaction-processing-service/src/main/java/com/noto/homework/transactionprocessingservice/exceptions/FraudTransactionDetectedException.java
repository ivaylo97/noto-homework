package com.noto.homework.transactionprocessingservice.exceptions;

/**
 * Used to indicate that a transaction has been deemed fraudulent.
 * <p>
 * Created by Ivaylo Sapunarov
 */
public class FraudTransactionDetectedException extends RuntimeException {

    public FraudTransactionDetectedException(String message) {
        super(message);
    }
}
