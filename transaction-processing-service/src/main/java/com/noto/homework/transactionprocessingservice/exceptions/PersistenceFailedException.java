package com.noto.homework.transactionprocessingservice.exceptions;

/**
 * Indicates that we failed to persist an entity to the database.
 * <p>
 * Created by Ivaylo Sapunarov
 */
public class PersistenceFailedException extends RuntimeException {

    public PersistenceFailedException(String message) {
        super(message);
    }
}
