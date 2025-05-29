package com.akatsuki.newsum.common.pagination.model.cursor;

import com.akatsuki.newsum.common.pagination.model.cursor.strategy.OrderStrategy;

public abstract class Cursor {

	OrderStrategy strategy;

	public OrderStrategy getStrategy() {
		return strategy;
	}

	public abstract String getCursor();
}
