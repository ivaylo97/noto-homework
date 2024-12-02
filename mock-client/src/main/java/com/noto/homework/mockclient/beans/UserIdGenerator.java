package com.noto.homework.mockclient.beans;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Used to generate new user ids based on the day the mock client was run.
 * <p>
 * Created by Ivaylo Sapunarov
 */
@Component
public class UserIdGenerator {

	private final DateTimeFormatter USER_ID_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public int generateUserId() {
		return Integer.parseInt(USER_ID_FORMATTER.format(ZonedDateTime.now()));
	}
}
