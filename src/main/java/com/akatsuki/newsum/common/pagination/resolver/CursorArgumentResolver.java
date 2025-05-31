package com.akatsuki.newsum.common.pagination.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.akatsuki.newsum.common.pagination.annotation.CursorParam;
import com.akatsuki.newsum.common.pagination.deserializer.CursorDeserializer;
import com.akatsuki.newsum.common.pagination.deserializer.registry.CursorDeserializerRegistry;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.cursor.strategy.OrderStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursorArgumentResolver implements HandlerMethodArgumentResolver {

	private final CursorDeserializerRegistry deserializerRegistry;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CursorParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) {

		CursorParam annotation = parameter.getParameterAnnotation(CursorParam.class);

		Class<? extends Cursor> cursorType = annotation.cursorType();
		OrderStrategy strategy = annotation.strategy();

		CursorDeserializer<? extends Cursor> deserializer = deserializerRegistry.resolve(cursorType);

		String rawCursor = webRequest.getParameter("cursor");
		if (rawCursor == null || rawCursor.isBlank()) {
			return deserializer.defaultCursor();
		}

		return deserializer.deserialize(rawCursor, strategy);
	}
}
