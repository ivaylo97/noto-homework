package com.noto.homework.transactionprocessingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Represents a transaction that will be sent to the server application.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDocument {

    @JsonProperty("userId")
    private long userId;

    @JsonProperty("transactionId")
    private long transactionId;

    @JsonProperty("country")
    private String country;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("latCoord")
    private double latCoord;

    @JsonProperty("longCoord")
    private double longCoord;

    @JsonProperty("timestamp")
    private String timestamp;
}
