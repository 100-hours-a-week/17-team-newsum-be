package com.akatsuki.newsum.common.pagination.model.cursor;

import java.time.LocalDateTime;

import com.akatsuki.newsum.common.pagination.model.cursor.strategy.OrderStrategy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatedAtIdCursor extends Cursor {
	private LocalDateTime createdAt;
	private Long id;

	public CreatedAtIdCursor(LocalDateTime createdAt, Long id) {
		this.createdAt = createdAt;
		this.id = id;
	}

	public CreatedAtIdCursor(LocalDateTime createdAt, Long id, OrderStrategy strategy) {
		this.createdAt = createdAt;
		this.id = id;
		this.strategy = strategy;
	}

	@Override
	public String getCursor() {
		return createdAt.toString() + "_" + id;
	}
}
