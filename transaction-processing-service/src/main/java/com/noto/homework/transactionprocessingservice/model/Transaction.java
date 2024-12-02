package com.noto.homework.transactionprocessingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.ZonedDateTime;

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
public class Transaction {

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("tran_id")
    private long transactionId;

    @JsonProperty("country")
    private String country;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("lat_coord")
    private double latCoord;

    @JsonProperty("long_coord")
    private double longCoord;

    @JsonProperty("timestamp")
    private ZonedDateTime timestamp;
}
