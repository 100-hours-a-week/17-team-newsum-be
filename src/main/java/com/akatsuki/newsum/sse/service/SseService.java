package com.akatsuki.newsum.sse.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.akatsuki.newsum.sse.repository.SseEmitterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

	private final SseEmitterRepository sseEmitterRepository;

	public SseEmitter subscribe(String id) {
		SseEmitter emitter = sseEmitterRepository.save(id);
		try {
			emitter.send(SseEmitter.event().name("connect").data("SSE Connect Success"));
		} catch (IOException e) {
			emitter.completeWithError(e);
		}
		return emitter;
	}

	public void sendData(String id, Object data) {
		sseEmitterRepository.get(id).ifPresent(emitter -> {
			try {
				emitter.send(SseEmitter.event().name("data").data(data));
			} catch (Exception e) {
				sseEmitterRepository.remove(id);
			}
		});
	}
}
