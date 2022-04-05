package com.interview.assessment.external;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AgifyClientTest {

	@Test
	void test_getAge() {
		var client = new AgifyClient();
		final var name = "Eli";

		var agifyResponse = client.getAge(name);

		assertThat(agifyResponse).isNotNull();
		assertThat(agifyResponse.getAge()).isNotNull();
		assertThat(agifyResponse.getName()).isEqualTo(name);

	}
}
