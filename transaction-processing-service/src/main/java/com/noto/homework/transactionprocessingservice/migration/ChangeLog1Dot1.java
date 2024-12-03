package com.noto.homework.transactionprocessingservice.migration;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import static com.noto.homework.transactionprocessingservice.repositories.MongoRepository.TRANSACTIONS_COLLECTION;

/**
 * Creates a geospatial index on the location field in the transactions collection.
 * <p>
 * Created by Ivaylo Sapunarov
 */
//@ChangeUnit(order = "002", id = "1.0.0__002", author = "Ivaylo Sapunarov")
//@RequiredArgsConstructor
public class ChangeLog1Dot1 {

//    private static final String LOCATION_INDEX = "location_index";

//    private final MongoDatabase database;

//    @RollbackExecution
//    public void rollback() {
//        database.getCollection(TRANSACTIONS_COLLECTION)
//                .dropIndex(LOCATION_INDEX);
//    }

//    @Execution
//    public void addLocationIndex(MongoDatabase mongoDatabase) {
//        MongoCollection<Document> collection = mongoDatabase.getCollection(TRANSACTIONS_COLLECTION);
//        IndexOptions indexOptions = new IndexOptions();
//        indexOptions.name(LOCATION_INDEX);
//        indexOptions.background(true);
//        collection.createIndex(Indexes.geo2dsphere("location"), indexOptions);
//    }
}
