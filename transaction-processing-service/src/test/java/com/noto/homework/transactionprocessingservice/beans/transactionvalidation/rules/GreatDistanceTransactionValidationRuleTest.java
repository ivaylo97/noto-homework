package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.exceptions.FraudTransactionDetectedException;
import com.noto.homework.transactionprocessingservice.model.TransactionDocument;
import com.noto.homework.transactionprocessingservice.model.TransactionTO;
import com.noto.homework.transactionprocessingservice.repositories.MongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import java.time.ZonedDateTime;

import static com.noto.homework.transactionprocessingservice.repositories.MongoRepository.TRANSACTIONS_COLLECTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=unittest")
class GreatDistanceTransactionValidationRuleTest {

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GreatDistanceTransactionValidationRule validationRule;

    @BeforeEach
    public void setup() {
        mongoTemplate.getCollection(TRANSACTIONS_COLLECTION)
                .drop();
    }

    @Test
    public void test_transactionFromReallyFarAway_isConsideredFraudulent() {
        // Given
        ZonedDateTime timestamp = ZonedDateTime.now().minusMinutes(25);
        TransactionDocument firstTransaction = getTransactionDocument(timestamp, 1.0, 1.0);
        TransactionDocument secondTransaction = getTransactionDocument(timestamp, 1.0, 1.0);
        TransactionDocument fraudulentTransaction = getTransactionDocument(timestamp, 27.0, 27.0);
        mongoRepository.persistTransaction(firstTransaction);
        mongoRepository.persistTransaction(secondTransaction);
        mongoRepository.persistTransaction(fraudulentTransaction);
        TransactionTO fraudulentTransactionTO = TransactionTO.builder()
                .userId(1)
                .latCoord(27.0)
                .longCoord(27.0)
                .timestamp(timestamp)
                .build();

        // When
        FraudTransactionDetectedException exception = assertThrows(FraudTransactionDetectedException.class,
                () -> validationRule.apply(fraudulentTransactionTO));

        // Then
        assertThat("Message should match the expected", exception.getMessage(),
                is("Fraud transaction detected: Recent transactions made from more than 300 km away."));
    }

    @Test
    public void test_nearbyTransaction_isConsideredOkay() {
        // Given
        ZonedDateTime timestamp = ZonedDateTime.now();
        TransactionDocument firstTransaction = getTransactionDocument(timestamp, 1.0, 1.0);
        TransactionDocument secondTransaction = getTransactionDocument(timestamp, 1.0, 1.0);
        TransactionDocument thirdTransaction = getTransactionDocument(timestamp, 1.0002, 1.0006);
        mongoRepository.persistTransaction(firstTransaction);
        mongoRepository.persistTransaction(secondTransaction);
        mongoRepository.persistTransaction(thirdTransaction);
        TransactionTO thirdTransactionTO = TransactionTO.builder()
                .userId(1)
                .latCoord(1.0002)
                .longCoord(1.0006)
                .timestamp(timestamp)
                .build();

        // When
        // Then
        assertDoesNotThrow(() -> validationRule.apply(thirdTransactionTO), "No exception should be thrown");
    }

    @Test
    public void test_twoFarAwayOldTransactions_oneRecentClose_isConsideredOkay() {
        // Given
        ZonedDateTime timestamp = ZonedDateTime.now();
        ZonedDateTime oldTimestamp = ZonedDateTime.now().minusMinutes(45);
        TransactionDocument firstTransaction = getTransactionDocument(oldTimestamp, 1.0, 1.0);
        TransactionDocument secondTransaction = getTransactionDocument(oldTimestamp, 1.0, 1.0);
        TransactionDocument thirdTransaction = getTransactionDocument(timestamp, 27.0, 27.0);
        mongoRepository.persistTransaction(firstTransaction);
        mongoRepository.persistTransaction(secondTransaction);
        mongoRepository.persistTransaction(thirdTransaction);
        TransactionTO thirdTransactionTO = TransactionTO.builder()
                .userId(1)
                .latCoord(27.0)
                .longCoord(27.0)
                .timestamp(timestamp)
                .build();

        // When
        // Then
        assertDoesNotThrow(() -> validationRule.apply(thirdTransactionTO), "No exception should be thrown");
    }

    private TransactionDocument getTransactionDocument(ZonedDateTime timestamp, double latCoord, double longCoord) {
        return TransactionDocument.builder()
                .userId(1)
                .latCoord(latCoord)
                .longCoord(longCoord)
                .timestamp(timestamp.toInstant().toString())
                .build();
    }
}