package com.akatsuki.newsum.batchtest;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduledTestLogger {

	@Scheduled(cron = "*/10 * * * * *")
	public void testScheduledLog() {
		log.info("⏰ 스케줄러 테스트: 현재 시각 = {}", LocalDateTime.now());
	}
}
