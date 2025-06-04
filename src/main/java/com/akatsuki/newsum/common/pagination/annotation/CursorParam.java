package com.akatsuki.newsum.common.pagination.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.cursor.strategy.OrderStrategy;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CursorParam {
	Class<? extends Cursor> cursorType() default CreatedAtIdCursor.class;

	OrderStrategy strategy() default OrderStrategy.DESC;
}
