package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.exceptions.FraudTransactionDetectedException;
import com.noto.homework.transactionprocessingservice.model.TransactionDocument;
import com.noto.homework.transactionprocessingservice.model.TransactionTO;
import com.noto.homework.transactionprocessingservice.repositories.MongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * Checks if any other transactions exist in the last 30 minutes, that more than
 * 300 km away from the current transaction.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Component
@RequiredArgsConstructor
public class GreatDistanceTransactionValidationRule implements ValidationRule {

    private static final double EARTH_RADIUS = 6371.01;

    private final MongoRepository mongoRepository;

    @Override
    public void apply(TransactionTO transactionTO) {
        ZonedDateTime thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30);
        mongoRepository.fetchTransactionAfterTimestamp(transactionTO.getUserId(), thirtyMinutesAgo)
                .forEach(transactionDocument -> {
                    double distance = calculateDistance(transactionTO, transactionDocument);
                    if (distance > 300) {
                        throw new FraudTransactionDetectedException("Fraud transaction detected: "
                                + "Recent transactions made from more than 300 km away.");
                    }
                });
    }

    /**
     * Equirectangular Distance Approximation formula.
     * Fun fact:
     * Equirectangular approximation isn’t very accurate when calculating long distances.
     * In fact, it treats the Earth as a perfect sphere and maps the sphere to a rectangular grid.
     */
    private double calculateDistance(TransactionTO transactionTO, TransactionDocument transactionDocument) {
        double toLat = Math.toRadians(transactionTO.getLatCoord());
        double toLong = Math.toRadians(transactionTO.getLongCoord());

        double documentLat = Math.toRadians(transactionDocument.getLatCoord());
        double documentLong = Math.toRadians(transactionDocument.getLongCoord());

        double x = (toLong - documentLong) * Math.cos((toLat + documentLat) / 2);
        double y = (documentLat - toLat);
        return Math.sqrt(x * x + y * y) * EARTH_RADIUS;
    }
}
