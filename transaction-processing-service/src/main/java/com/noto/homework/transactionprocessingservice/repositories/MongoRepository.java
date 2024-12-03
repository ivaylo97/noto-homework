package com.noto.homework.transactionprocessingservice.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noto.homework.transactionprocessingservice.exceptions.PersistenceFailedException;
import com.noto.homework.transactionprocessingservice.model.TransactionDocument;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Acts as an abstraction layer, in order to make working the with the MongoDB easier.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Component
@RequiredArgsConstructor
public class MongoRepository {

    public static final String BLACKLISTED_COUNTRIES_COLLECTION = "blacklistedCountries";

    public static final String TRANSACTIONS_COLLECTION = "transactions";

    private final ObjectMapper objectMapper;

    private final MongoTemplate mongoTemplate;

    public List<String> fetchBlacklistedCountries() {
        List<Document> documents = mongoTemplate.findAll(Document.class, BLACKLISTED_COUNTRIES_COLLECTION);
        if (documents.isEmpty()) {
            return emptyList();
        }
        return documents.get(0).getList("countries", String.class, emptyList());
    }

    public List<TransactionDocument> fetchTransactionAfterTimestamp(long userId, ZonedDateTime timestamp) {
        Query query = query(where("userId").is(userId).and("timestamp").gte(timestamp.toInstant().toString()));
        return mongoTemplate.find(query, TransactionDocument.class, TRANSACTIONS_COLLECTION);
    }

    public List<String> fetchDistinctTransactionCountriesAfterTimestamp(long userId, ZonedDateTime timestamp) {
        Query query = query(where("userId").is(userId).and("timestamp").gte(timestamp.toInstant().toString()));
        return mongoTemplate.findDistinct(query, "country", TRANSACTIONS_COLLECTION, String.class);
    }

//    public List<Document> fetchTransactionsOutsideCurrentTransactionRange(TransactionTO transactionTO, int rangeInKilometers) {
//        String timestamp = transactionTO.getTimestamp()
//                .toInstant()
//                .toString();
//        NearQuery nearQuery = buildNearQuery(transactionTO.getLatCoord(), transactionTO.getLongCoord(), rangeInKilometers);
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.geoNear(nearQuery, "distance"),
//                Aggregation.match(where("user_id").is(transactionTO.getUserId())
//                        .and("timestamp").gte(timestamp)));
//        return mongoTemplate.aggregate(aggregation, TRANSACTIONS_COLLECTION, Document.class)
//                .getMappedResults();
//    }

    public void persistTransaction(TransactionDocument transaction) {
        try {
            mongoTemplate.save(objectMapper.writeValueAsString(transaction), TRANSACTIONS_COLLECTION);
        } catch (JsonProcessingException e) {
            throw new PersistenceFailedException("Failed to persist transaction due to: " + e.getMessage());
        }
    }

    public void persistBlacklistedCountries(List<String> countries) {
        Map<String, List<String>> document = new HashMap<>();
        document.put("countries", countries);
        mongoTemplate.save(document, BLACKLISTED_COUNTRIES_COLLECTION);
    }

//    private NearQuery buildNearQuery(double latCoord, double longCoord, int rangeInKilometers) {
//        return NearQuery.near(latCoord, longCoord)
//                .spherical(true)
//                .minDistance(rangeInKilometers, Metrics.KILOMETERS);
//    }
}
