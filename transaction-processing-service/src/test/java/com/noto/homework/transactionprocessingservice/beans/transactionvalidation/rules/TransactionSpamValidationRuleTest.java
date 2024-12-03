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
class TransactionSpamValidationRuleTest {

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TransactionSpamValidationRule validationRule;

    @BeforeEach
    public void setup() {
        mongoTemplate.getCollection(TRANSACTIONS_COLLECTION)
                .drop();
    }

    @Test
    public void test_reallyFrequentTransactions_areConsideredFraudulent() {
        // Given
        ZonedDateTime timestamp = ZonedDateTime.now();
        for (int i = 0; i < 10; i++) {
            TransactionDocument transaction = getTransactionDocument(1, timestamp.minusSeconds(2));
            mongoRepository.persistTransaction(transaction);
        }
        TransactionDocument fraudulentTransaction = getTransactionDocument(1, timestamp.minusSeconds(1));
        mongoRepository.persistTransaction(fraudulentTransaction);
        TransactionTO fraudulentTransactionTO = TransactionTO.builder()
                .userId(1)
                .timestamp(timestamp.minusSeconds(1))
                .build();

        // When
        FraudTransactionDetectedException exception = assertThrows(FraudTransactionDetectedException.class,
                () -> validationRule.apply(fraudulentTransactionTO));

        // Then
        assertThat("Message should match the expected", exception.getMessage(),
                is("Fraudulent spammed transaction detected for user: 1"));
    }

    @Test
    public void test_nonFrequentTransactions_areConsideredOkay() throws InterruptedException {
        // Given
        ZonedDateTime timestamp = ZonedDateTime.now();
        for (int i = 0; i < 9; i++) {
            TransactionDocument transaction = getTransactionDocument(1, timestamp.minusSeconds(59));
            mongoRepository.persistTransaction(transaction);
        }
        Thread.sleep(1_000);
        TransactionDocument transactionDocument = getTransactionDocument(1, timestamp);
        mongoRepository.persistTransaction(transactionDocument);
        TransactionTO transactionTO = TransactionTO.builder()
                .userId(1)
                .timestamp(timestamp)
                .build();

        // When
        // Then
        assertDoesNotThrow(() -> validationRule.apply(transactionTO), "No exception should be thrown");
    }

    private TransactionDocument getTransactionDocument(long userId, ZonedDateTime timestamp) {
        return TransactionDocument.builder()
                .userId(userId)
                .timestamp(timestamp.toInstant().toString())
                .build();
    }
}