package com.noto.homework.transactionprocessingservice.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;

/**
 * Created by Ivaylo Sapunarov 
 */
@ChangeUnit(order = "001", id = "1.0.0__001", author = "Ivaylo Sapunarov")
@RequiredArgsConstructor
public class ChangeLog1Dot0 {

    private final MongoDatabase database;

    @RollbackExecution
    public void rollback() {
        //TODO rollback the changes
    }

    @Execution
    public void addIndex(MongoDatabase mongoDatabase) {
        //TODO add the index
    }
}
