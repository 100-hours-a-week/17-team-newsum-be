package com.akatsuki.newsum.common.pagination.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CursorFieldMapping {

	String DEFAULT_CREATED_AT_FIELD_NAME = "createdAt";
	String DEFAULT_ID_FIELD_NAME = "id";

	String createdAt() default DEFAULT_CREATED_AT_FIELD_NAME;

	String id() default DEFAULT_ID_FIELD_NAME;
}
