package com.akatsuki.newsum.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

@Testcontainers
public abstract class TestContainerSupport implements InitializingBean {

	private static final String POSTGRESQL_IMAGE = "postgres:15-alpine";
	private static final String REDIS_IMAGE = "redis:7-alpine";

	private static final String POSTGRES_DATABASE = "integration_test_db";
	private static final String POSTGRES_USER = "test_user";
	private static final String POSTGRES_PASSWORD = "<PASSWORD>";
	private static final int REDIS_PORT = 6379;

	@Container
	protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
		new PostgreSQLContainer<>(DockerImageName.parse(POSTGRESQL_IMAGE))
			.withDatabaseName(POSTGRES_DATABASE)
			.withUsername(POSTGRES_USER)
			.withPassword(POSTGRES_PASSWORD)
			.withReuse(true)
			.withCommand("postgres", "-c", "fsync=off", "-c", "synchronous_commit=off");

	@Container
	protected static final RedisContainer REDIS_CONTAINER =
		new RedisContainer(DockerImageName.parse(REDIS_IMAGE))
			.withExposedPorts(REDIS_PORT)
			.withReuse(true)
			.withCommand("redis-server", "--appendonly", "no", "--save", "");

	@DynamicPropertySource
	static void configureTestContainers(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
		registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);

		registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT).toString());
	}

	@Override
	public void afterPropertiesSet() {
		verifyContainersAreRunning();
	}

	protected void verifyContainersAreRunning() {
		if (!isPostgresRunning()) {
			throw new IllegalStateException("PostgreSQL 컨테이너가 실행되지 않았습니다.");
		}

		if (!isRedisRunning()) {
			throw new IllegalStateException("Redis 컨테이너가 실행되지 않았습니다.");
		}
	}

	protected static boolean isPostgresRunning() {
		return POSTGRES_CONTAINER.isRunning();
	}

	protected static boolean isRedisRunning() {
		return REDIS_CONTAINER.isRunning();
	}
}
