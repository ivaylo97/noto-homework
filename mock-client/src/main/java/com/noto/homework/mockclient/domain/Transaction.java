package com.noto.homework.mockclient.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a transaction that will be sent to the server application.
 * <p>
 * Created by Ivaylo Sapunarov
 */
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
