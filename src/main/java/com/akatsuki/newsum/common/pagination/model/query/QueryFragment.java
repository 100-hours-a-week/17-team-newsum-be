package com.akatsuki.newsum.common.pagination.model.query;

public record QueryFragment(
	Object whereClause,
	Object orderByClause
) {

}
