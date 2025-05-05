package com.akatsuki.newsum.common.pagination;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.pagination.generator.CursorGenerator;
import com.akatsuki.newsum.common.pagination.generator.registry.CursorGeneratorRegistry;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.common.pagination.model.page.PageInfo;
import com.akatsuki.newsum.common.pagination.model.page.PageItemList;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursorPaginationService {

	private final CursorGeneratorRegistry cursorGeneratorRegistry;

	public <T> CursorPage<T> create(List<T> results, int size, Cursor cursor) {

		if (results == null || results.isEmpty()) {
			return CursorPage.empty();
		}

		boolean hasNext = results.size() > size;
		PageItemList<T> content = hasNext ? PageItemList.of(results.subList(0, size)) : PageItemList.of(results);

		if (!hasNext) {
			return new CursorPage<>(content, PageInfo.end());
		}

		T lastContent = results.get(size);
		CursorGenerator extractor = cursorGeneratorRegistry.resolve(cursor);
		Cursor nextCursor = extractor.generate(lastContent);

		PageInfo pageInfo = PageInfo.from(nextCursor);

		return new CursorPage<>(content, pageInfo);
	}
}
