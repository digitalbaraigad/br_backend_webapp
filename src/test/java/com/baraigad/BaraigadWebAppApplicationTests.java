package com.baraigad;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class BaraigadWebAppApplicationTests {

	@Autowired
	private Environment environment;

	@Test
	void contextLoads() {
	}

	@Test
	void loadsReadableMarathiNotificationTextAsUtf8() {
		assertEquals(
				"बा रायगड परिवार - सहभागी नोंदणी प्राप्त",
				environment.getProperty("baraigad.participant-notification.email-subject"));
	}

}
