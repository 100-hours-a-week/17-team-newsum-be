package com.akatsuki.newsum.extern.dto;

import java.util.Map;

public record ImageGenerationRequest(
	String workId,
	Long personaId,
	String keyword,
	String category,
	String title,
	String reportUrl,
	String content,
	String referenceUrl,
	String description1,
	String description2,
	String description3,
	String description4,
	Map<String, Object> imagePrompts
) {
}
