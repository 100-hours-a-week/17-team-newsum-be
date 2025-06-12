package com.akatsuki.newsum.extern.dto;

import java.util.List;

public record ImageGenerationCallbackRequest(
	Long requestId,
	List<String> imagelink
) {
}
