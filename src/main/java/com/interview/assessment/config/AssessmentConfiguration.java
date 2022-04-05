package com.interview.assessment.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.interview.assessment.external.AgifyClient;
import com.interview.assessment.party.Party;
import com.interview.assessment.party.PartyService;
import com.interview.assessment.party.PartyServiceImpl;
import com.interview.assessment.player.Player;
import com.interview.assessment.player.PlayerService;
import com.interview.assessment.player.PlayerServiceImpl;
import com.interview.assessment.player.Role;

/*
 * Updated by:
 * Updated date:
 * Description: 
 * 1. These business logics are immutable which impact to runtime error (java.lang.UnsupportedOperationException) 
 * when trying to hit the APIs that would make to changes (Add, Update, and Delete) to the data
*/
@Configuration
public class AssessmentConfiguration {

	@Bean
	public PartyService partyService(PlayerService playerService) {
		
		/* #1 Before:
		 * 
		 * var party1 = new Party(Arrays.asList( new Player("Ana", Role.TANK), new
		 * Player("Cris", Role.HEALER), new Player("Joe", Role.DPS), new Player("Alex",
		 * Role.DPS), new Player("Emma", Role.DPS) ));
		 * 
		 * var party2 = new Party(Arrays.asList( new Player("Beatrice", Role.TANK), new
		 * Player("James", Role.HEALER), new Player("Henry", Role.DPS), new
		 * Player("Alice", Role.DPS), new Player("John", Role.DPS) ));
		 * 
		 * Map<Integer, Party> parties = Map.of( party1.getId(), party1, party2.getId(),
		 * party2 );
		 * 
		 */

		/*
		 * #1 After:
		 * ============= START =============
		 */
		
		List<Player> listOfPlayers1 = new ArrayList<Player>();
		List<Player> listOfPlayers2 = new ArrayList<Player>();
		
		Collections.addAll(listOfPlayers1, 
				new Player("Ana", Role.TANK),
				new Player("Cris", Role.HEALER),
				new Player("Joe", Role.DPS),
				new Player("Alex", Role.DPS),
				new Player("Emma", Role.DPS));
		Collections.addAll(listOfPlayers2, 
				new Player("Beatrice", Role.TANK),
				new Player("James", Role.HEALER),
				new Player("Henry", Role.DPS),
				new Player("Alice", Role.DPS),
				new Player("John", Role.DPS));
		
		var party1 = new Party(listOfPlayers1);
		var party2 = new Party(listOfPlayers2);
		
		Map<Integer, Party> parties = new HashMap<Integer, Party>();
		parties.put(party1.getId(), party1);
		parties.put(party2.getId(), party2);
		
		/*
		 * ============= END =============
		 */

		return new PartyServiceImpl(parties, playerService);
	}

	@Bean
	public PlayerService playerService() {
		var agifyClient = new AgifyClient();
		return new PlayerServiceImpl(agifyClient);
	}
}
