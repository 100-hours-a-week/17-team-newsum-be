package com.akatsuki.newsum.common.pagination.model.cursor;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatedAtIdCursor implements Cursor {
	private LocalDateTime createdAt;
	private Long id;

	public CreatedAtIdCursor(LocalDateTime createdAt, Long id) {
		this.createdAt = createdAt;
		this.id = id;
	}

	@Override
	public String getCursor() {
		return createdAt.toString() + "_" + id;
	}
}
