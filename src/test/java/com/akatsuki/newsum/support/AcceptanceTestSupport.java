package com.akatsuki.newsum.support;

import org.junit.jupiter.api.BeforeEach;

import io.restassured.RestAssured;

public abstract class AcceptanceTestSupport extends IntegrationTestSupport {

	@BeforeEach
	public void setUpAcceptanceTest() {
		RestAssured.port = port;
	}
}
