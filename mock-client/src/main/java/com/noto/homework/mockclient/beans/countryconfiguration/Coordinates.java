package com.noto.homework.mockclient.beans.countryconfiguration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Represents pairs of latitude and longitude coordinates.
 * <p>
 * Created by Ivaylo Sapunarov - Delta Source Bulgaria on 12/1/24.
 */
@Getter
@ToString
@RequiredArgsConstructor
public class Coordinates {

	private final double latitude;

	private final double longitude;
}
