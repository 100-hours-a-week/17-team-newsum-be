package com.akatsuki.newsum.sse.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterRepository {

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private static final Long DEFAULT_TIMEOUT = 60 * 60 * 1000L; // 1시간

	public SseEmitter save(String id) {
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

		emitter.onCompletion(() -> emitters.remove(id));
		emitter.onTimeout(() -> emitters.remove(id));
		emitter.onError(e -> emitters.remove(id));

		emitters.put(id, emitter);
		return emitter;
	}

	public Optional<SseEmitter> get(String userId) {
		return Optional.ofNullable(emitters.get(userId));
	}

	public void remove(String userId) {
		emitters.remove(userId);
	}
}
