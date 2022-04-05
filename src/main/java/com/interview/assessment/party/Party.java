package com.interview.assessment.party;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.interview.assessment.player.Player;

public class Party {

	private final static AtomicInteger counter = new AtomicInteger();

	private int id;
	private List<Player> players;


	public Party() {
	}

	public Party(List<Player> players) {
		this.id = counter.incrementAndGet();
		this.players = players;
	}

	public int getId() {
		return id;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		if (this.players.size() >= 5) throw new RuntimeException("max players: 5");
		this.players.add(player);
	}

	public void removePlayerFromParty(String playerName) {
		this.players.removeIf(p -> p.getName().equals((playerName)));
	}
}
