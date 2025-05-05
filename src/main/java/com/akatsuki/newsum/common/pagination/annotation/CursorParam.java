package com.akatsuki.newsum.common.pagination.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CursorParam {
	Class<? extends Cursor> cursorType();
}
