package com.noto.homework.transactionprocessingservice.beans.transactionvalidation.rules;

import com.noto.homework.transactionprocessingservice.exceptions.FraudTransactionDetectedException;
import com.noto.homework.transactionprocessingservice.model.TransactionTO;
import com.noto.homework.transactionprocessingservice.repositories.MongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.noto.homework.transactionprocessingservice.repositories.MongoRepository.BLACKLISTED_COUNTRIES_COLLECTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=unittest")
class BlackListedTransactionValidationRuleTest {

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BlackListedTransactionValidationRule validationRule;

    @BeforeEach
    public void setup() {
        mongoTemplate.getCollection(BLACKLISTED_COUNTRIES_COLLECTION)
                .drop();
    }

    @Test
    public void test_transactionFromBlacklistedCountry_isConsideredFraudulent() {
        // Given
        mongoRepository.persistBlacklistedCountries(List.of("Blacklisted"));
        TransactionTO transactionTO = TransactionTO.builder()
                .country("Blacklisted")
                .build();

        // When
        FraudTransactionDetectedException exception = assertThrows(FraudTransactionDetectedException.class,
                () -> validationRule.apply(transactionTO));

        // Then
        assertThat("Message should match the expected", exception.getMessage(), is("TransactionTO is from blacklisted country: Blacklisted"));
    }

    @Test
    public void test_transactionFromNonBlacklistedCountry_isConsideredOkay() {
        // Given
        TransactionTO transactionTO = TransactionTO.builder()
                .country("UK")
                .build();

        // When
        // Then
        assertDoesNotThrow(() -> validationRule.apply(transactionTO),  "No exception should be thrown");
    }
}