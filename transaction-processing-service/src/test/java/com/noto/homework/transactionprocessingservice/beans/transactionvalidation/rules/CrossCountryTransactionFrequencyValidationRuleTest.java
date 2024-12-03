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
class CrossCountryTransactionFrequencyValidationRuleTest {

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CrossCountryTransactionFrequencyValidationRule validationRule;

    @BeforeEach
    public void setup() {
        mongoTemplate.getCollection(TRANSACTIONS_COLLECTION)
                .drop();
    }

    @Test
    public void test_transactionFrequentCrossCountryTransactions_areConsideredFraudulent() {
        // Given
        ZonedDateTime timestamp = ZonedDateTime.now();
        TransactionDocument firstCrossCountryTransaction = getTransactionDocument(1, "GE", timestamp.minusMinutes(3));
        TransactionDocument secondCrossCountryTransaction = getTransactionDocument(1, "FR", timestamp.minusMinutes(2));
        TransactionDocument thirdCrossCountryTransaction = getTransactionDocument(1, "BE", timestamp.minusMinutes(1));
        TransactionTO thirdCrossCountryTransactionTO = TransactionTO.builder()
                .userId(1)
                .country("BE")
                .timestamp(timestamp.minusMinutes(1))
                .build();
        mongoRepository.persistTransaction(firstCrossCountryTransaction);
        mongoRepository.persistTransaction(secondCrossCountryTransaction);
        mongoRepository.persistTransaction(thirdCrossCountryTransaction);

        // When
        FraudTransactionDetectedException exception = assertThrows(FraudTransactionDetectedException.class,
                () -> validationRule.apply(thirdCrossCountryTransactionTO));

        // Then
        assertThat("Message should match the expected", exception.getMessage(),
                is("Fraudulent cross-country transaction detected for user: 1"));
    }


    @Test
    public void test_rareCrossCountryTransactions_areConsideredOkay() {
        // Given
        TransactionDocument firstCrossCountryTransaction = getTransactionDocument(1, "GE", ZonedDateTime.now().minusMinutes(40));
        TransactionDocument secondCrossCountryTransaction = getTransactionDocument(1, "FR", ZonedDateTime.now().minusMinutes(20));
        TransactionDocument thirdCrossCountryTransaction = getTransactionDocument(1, "BE", ZonedDateTime.now().minusMinutes(5));
        TransactionTO thirdCrossCountryTransactionTO = TransactionTO.builder()
                .userId(1)
                .country("BE")
                .timestamp(ZonedDateTime.now().plusMinutes(5))
                .build();
        mongoRepository.persistTransaction(firstCrossCountryTransaction);
        mongoRepository.persistTransaction(secondCrossCountryTransaction);
        mongoRepository.persistTransaction(thirdCrossCountryTransaction);

        // When
        // Then
        assertDoesNotThrow(() -> validationRule.apply(thirdCrossCountryTransactionTO), "No exception should be thrown");
    }

    private TransactionDocument getTransactionDocument(long userId, String country, ZonedDateTime timestamp) {
        return TransactionDocument.builder()
                .userId(userId)
                .country(country)
                .timestamp(timestamp.toInstant().toString())
                .build();
    }
}