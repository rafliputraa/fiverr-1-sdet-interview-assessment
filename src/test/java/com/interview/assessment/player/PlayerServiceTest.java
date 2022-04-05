package com.interview.assessment.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlayerServiceTest {

	@Test
	public void test_getAge() {
		// TODO: mock agify api, feel free to add a new mocking library
		var agifyClient = new AgifyClient();

		var playerService = new PlayerServiceImpl(agifyClient);
		Integer age = playerService.getAge("Anna");
		Assertions.assertNotNull(age);
	}
	
	private static class PlayerServiceImpl implements PlayerService {

		private final AgifyClient agifyClient;

		public PlayerServiceImpl(AgifyClient agifyClient) {
			this.agifyClient = agifyClient;
		}

		@Override
		public Integer getAge(String name) {
			var agifyResponse = agifyClient.getAge(name);
			return agifyResponse.getAge();
		}

	}
	
	private static class AgifyClient {

		public AgifyResponse getAge(String name) {
			var response = new AgifyResponse(name);
			return response;
		}

		public static class AgifyResponse {
			
			public AgifyResponse(String name) {
				setName(name);
				setAge((int) Math.floor(Math.random() * 70));
				setCount((long) Math.floor(Math.random() * 100));
			}
			
			private String name;
			private Integer age;
			private Long count;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public Integer getAge() {
				return age;
			}

			public void setAge(Integer age) {
				this.age = age;
			}

			public Long getCount() {
				return count;
			}

			public void setCount(Long count) {
				this.count = count;
			}
		}
	}
}
