package com.interview.assessment.party;

import java.util.List;

import org.springframework.stereotype.Service;

import com.interview.assessment.player.Player;

@Service
public interface PartyService {

	Party getParty(int id);

	List<Party> getParties();

	void updateParty(Party party);

	void addPlayerToParty(int partyId, Player player);

	void removePlayerFromParty(int partyId, String playerName);
}
