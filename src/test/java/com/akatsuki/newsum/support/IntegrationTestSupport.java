package com.akatsuki.newsum.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTestSupport extends TestContainerSupport {

	@LocalServerPort
	protected int port;

	@Autowired
	private DatabaseConfigurator databaseConfigurator;

	@BeforeEach
	public void setUpIntegrationTest() {
		reloadTestData();

		verifyTestData();
	}

	private void reloadTestData() {
		databaseConfigurator.clear();
		databaseConfigurator.loadData();
	}

	private void verifyTestData() {
		databaseConfigurator.verifyTestEnvironment();
	}
}
