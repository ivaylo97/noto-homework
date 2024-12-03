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
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;

import static com.noto.homework.transactionprocessingservice.repositories.MongoRepository.TRANSACTIONS_COLLECTION;

/**
 * A migration adding a compound index for user_id and timestamp fields in the transactions collection.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@ChangeUnit(order = "001", id = "1.0.0__001", author = "Ivaylo Sapunarov")
@RequiredArgsConstructor
public class ChangeLog1Dot0 {

    private static final String USER_ID_TIMESTAMP_INDEX = "user_id_timestamp_compound_index";

    private final MongoDatabase database;

    @RollbackExecution
    public void rollback() {
        database.getCollection(TRANSACTIONS_COLLECTION)
                .dropIndex(USER_ID_TIMESTAMP_INDEX);
    }

    @Execution
    public void addUserIdTimestampIndex(MongoDatabase mongoDatabase) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(TRANSACTIONS_COLLECTION);
        IndexOptions indexOptions = new IndexOptions();
        indexOptions.name(USER_ID_TIMESTAMP_INDEX);
        indexOptions.background(true);
        List<Bson> compoundIndexFields = Arrays.asList(
                Indexes.ascending("user_id"),
                Indexes.descending("timestamp")
        );
        collection.createIndex(Indexes.compoundIndex(compoundIndexFields), indexOptions);
    }
}
