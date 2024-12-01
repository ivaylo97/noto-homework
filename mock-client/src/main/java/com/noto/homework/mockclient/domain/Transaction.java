package com.noto.homework.mockclient.domain;

import lombok.Builder;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * Represents a transaction that will be sent to the server application.
 * <p>
 * Created by Ivaylo Sapunarov - Delta Source Bulgaria on 12/1/24.
 */
@Builder
@ToString
public class Transaction {

    private final long userId;

    private final long transactionId;

    private final String country;

    private final String amount;

    private final double latCoord;

    private final double longCoord;

	private final ZonedDateTime timestamp;
}
