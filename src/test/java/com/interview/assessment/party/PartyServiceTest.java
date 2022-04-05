package com.interview.assessment.party;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

import com.interview.assessment.player.Player;
import com.interview.assessment.player.PlayerService;
import com.interview.assessment.player.Role;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Updated by:
 * Updated date:
 * Description: 
 * 1. These business logics are immutable which impact to runtime error (java.lang.UnsupportedOperationException) 
 * when trying to hit the APIs that would make to changes (Add, Update, and Delete) to the data
*/

public class PartyServiceTest {
	
	@LocalServerPort
	private int port;
	
	private PartyService partyService = getPartyService();

	@Test
	public void test_getParty() {
		var party = partyService.getParty(10);
		List<Player> allPlayers = party.getPlayers();
		assertThat(party).isNotNull();

		// TODO: add extra assertions
		assertThat(party.getId()).isEqualTo(10);
		assertThat(party.getPlayers()).isNotNull();
		assertThat(party.getPlayers().size()).isLessThanOrEqualTo(5);
		assertThat(allPlayers.get(0).getName()).isInstanceOf(String.class);
		assertThat(allPlayers.get(0).getRole()).isInstanceOf(Role.class);
		assertThat(allPlayers.get(0).getAge()).isInstanceOf(Integer.class);
	}

	// TODO: implement unit tests for PartyService to cover the functionality
	
	@Test
	public void test_getParties() {
		var parties = partyService.getParties();
		
		assertThat(parties).isNotNull();
		assertThat(parties.size()).isGreaterThan(0);
		assertThat(parties.get(0).getPlayers().size()).isLessThanOrEqualTo(5);
		assertThat(parties.get(1).getPlayers().size()).isLessThanOrEqualTo(5);
	}

	@Test
	public void test_updateParty() {
		
		var agifyClient = new AgifyClient();
		var playerService = new PlayerServiceImpl(agifyClient);
		
		List<Player> listOfPlayers = new ArrayList<Player>();
		Player newPlayerOne = new Player("Zero", Role.TANK);
		Player newPlayerTwo = new Player("Mecha", Role.HEALER);
		Player newPlayerThree = new Player("Veemon", Role.DPS);
		playerService.getAge(newPlayerOne.getName());
		playerService.getAge(newPlayerTwo.getName());
		playerService.getAge(newPlayerThree.getName());
		
		Collections.addAll(listOfPlayers, 
				newPlayerOne,
				newPlayerTwo,
				newPlayerThree);
		
		var newParty = new Party(listOfPlayers);
		int partyId = newParty.getId();
		partyService.updateParty(newParty);
		var party = partyService.getParty(partyId);
		
		assertThat(party).isNotNull();
		assertThat(party.getId()).isEqualTo(partyId);
		assertThat(party.getPlayers().size()).isEqualTo(3);
		assertThat(party.getPlayers().get(0).getName()).isEqualTo("Zero");
		assertThat(party.getPlayers().get(1).getName()).isEqualTo("Mecha");
		assertThat(party.getPlayers().get(2).getName()).isEqualTo("Veemon");
	}
	
	@Test
	public void test_addPlayerToParty() {
		var agifyClient = new AgifyClient();
		var playerService = new PlayerServiceImpl(agifyClient);
		int partyId = 4;
		Player newPlayer = new Player("Axel", Role.DPS);
		playerService.getAge(newPlayer.getName());
		var party = partyService.getParty(partyId);
		partyService.removePlayerFromParty(partyId, "John");
		partyService.addPlayerToParty(partyId, newPlayer);
		assertThat(party.getId()).isEqualTo(partyId);
		assertThat(party.getPlayers().size()).isEqualTo(5);
		assertThat(party.getPlayers().get(4).getName()).isEqualTo("Axel");
		assertThat(party.getPlayers().get(4).getRole()).isEqualTo(Role.DPS);
	}
	
	@Test
	public void test_removePlayerFromParty() {
		int partyId = 9;
		partyService.removePlayerFromParty(partyId, "Beatrice");
		var party = partyService.getParty(partyId);
		boolean result = party.getPlayers().stream().allMatch(data -> data.getName().equalsIgnoreCase("Beatrice"));
		assertThat(party.getPlayers().size()).isEqualTo(4);
		assertThat(result).isFalse();
	}

	private PartyService getPartyService() {
		// TODO: mock agify api
		
		var agifyClient = new AgifyClient();

		var playerService = new PlayerServiceImpl(agifyClient);

		/* #1 Before:
		 * 
		 * var party1 = new Party(Arrays.asList( new Player("Ana", Role.TANK), new
		 * Player("Cris", Role.HEALER), new Player("Joe", Role.DPS), new Player("Alex",
		 * Role.DPS), new Player("Emma", Role.DPS) ));
		 * 
		 * var party2 = new Party(Arrays.asList( new Player("Beatrice", Role.DPS), new
		 * Player("James", Role.HEALER), new Player("Henry", Role.DPS), new
		 * Player("Alice", Role.DPS), new Player("John", Role.DPS) ));
		 * 
		 * Map<Integer, Party> parties = Map.of(party1.getId(), party1,party2.getId(), party2
		 * );
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
		
		Party party1 = new Party(listOfPlayers1);
		Party party2 = new Party(listOfPlayers2);
		
		Map<Integer, Party> parties = new HashMap<Integer, Party>();
		parties.put(party1.getId(), party1);
		parties.put(party2.getId(), party2);
		
		/*
		 * ============= END =============
		 */

		return new PartyServiceImpl(parties, playerService);
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
