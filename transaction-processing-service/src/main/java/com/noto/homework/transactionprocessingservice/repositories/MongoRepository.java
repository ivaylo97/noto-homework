package com.noto.homework.transactionprocessingservice.repositories;

import com.noto.homework.transactionprocessingservice.beans.ResourceReader;
import com.noto.homework.transactionprocessingservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Acts as an abstraction layer, in order to make working the with the MongoDB easier.
 * <p>
 * Created by Ivaylo Sapunarov - Delta Source Bulgaria on 12/2/24.
 */
@Component
@RequiredArgsConstructor
public class MongoRepository {

    private static final String BLACKLISTED_COUNTRIES_COLLECTION = "blacklistedCountries";

    private static final String TRANSACTIONS_COLLECTION = "transactions";

    private final MongoTemplate mongoTemplate;

    public List<String> fetchBlacklistedCountries() {
        return mongoTemplate.findAll(String.class, BLACKLISTED_COUNTRIES_COLLECTION);
    }

    public List<Document> fetchTransactionAfterTimestamp(long userId, ZonedDateTime timestamp) {
        Query query = query(where("user_id").is(userId).and("timestamp").gte(timestamp));
        return mongoTemplate.find(query, Document.class, TRANSACTIONS_COLLECTION);
    }

    public List<Document> fetchTransactionsDistinctByCountryAfterTimestamp(long userId, ZonedDateTime timestamp) {
        Query query = query(where("user_id").is(userId).and("timestamp").gte(timestamp));
        return mongoTemplate.findDistinct(query, "country", TRANSACTIONS_COLLECTION, Document.class);
    }

}
