package com.interview.assessment.external;

import org.springframework.web.client.RestTemplate;

public class AgifyClient {

	RestTemplate restTemplate = new RestTemplate();
	final String url = "https://api.agify.io/?name=";

	public AgifyResponse getAge(String name) {
		var response = restTemplate.getForEntity(url + name, AgifyResponse.class);
		return response.getBody();
	}

	public static class AgifyResponse {
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
