package com.interview.assessment.player;

import com.interview.assessment.external.AgifyClient;

public class PlayerServiceImpl implements PlayerService {

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
