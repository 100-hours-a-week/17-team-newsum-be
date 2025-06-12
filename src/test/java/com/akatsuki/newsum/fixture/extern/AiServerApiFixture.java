package com.akatsuki.newsum.fixture.extern;

import com.akatsuki.newsum.extern.dto.CreateWebtoonApiRequest;

public class AiServerApiFixture {

	public static CreateWebtoonApiRequest createBasicWebtoonRequest() {
		return new CreateWebtoonApiRequest("1", createBasicImagePrompts());
	}

	public static String createBasicImagePrompts() {
		return """
			{
				"scene1": "주인공이 학교에 가는 모습",
				"scene2": "친구들과 대화하는 장면"
			}
			""";
	}
}
