package com.noto.homework.mockclient.beans.countryconfiguration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

/**
 * Represents the mappings of countries and their respective coordinates.
 * <p>
 * Created by Ivaylo Sapunarov - Delta Source Bulgaria on 12/1/24.
 */
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "country.location")
public class CountryLocationConfiguration {

	private static final Coordinates DEFAULT_COORDINATES = new Coordinates(0, 0);

	private final Map<String, Coordinates> mappings;

	/**
	 * Since it's a mock application we can safely return default coordinates if the country is not found in
	 * the configuration mappings.
	 */
	public Coordinates getCoordinates(String country) {
		return mappings.getOrDefault(country, DEFAULT_COORDINATES);
	}
}
