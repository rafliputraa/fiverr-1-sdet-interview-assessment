package com.interview.assessment.party;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.assessment.player.Player;
import com.interview.assessment.player.PlayerService;
import com.interview.assessment.player.Role;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PartyTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private PlayerService playerService;

	@BeforeAll
	public static void setUp() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		
	}

	@Test
	public void test_getParty() throws Exception {
		final var partyId = 1;
		final var url = getUrl("/party/" + partyId);
		ResponseEntity<Party> responseEntity = restTemplate.getForEntity(url, Party.class);
		Party party = responseEntity.getBody();
		List<Player> allPlayers = party.getPlayers();

		assertThat(party).isNotNull();
		// TODO TEST-1: add extra assertions
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertThat(party.getId()).isNotNull();
		assertThat(party.getId()).isEqualTo(partyId);
		assertThat(party.getPlayers()).isNotNull();
		assertThat(party.getPlayers().size()).isLessThanOrEqualTo(5);
		assertThat(allPlayers.get(0).getName()).isInstanceOf(String.class);
		assertThat(allPlayers.get(0).getRole()).isInstanceOf(Role.class);
		assertThat(allPlayers.get(0).getAge()).isInstanceOf(Integer.class);
	}

	@Test
	public void test_getParties() throws Exception {
		ResponseEntity<List<Party>> responseEntity = restTemplate.exchange(
				getUrl("/party"),
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {
				});
		List<Party> parties = responseEntity.getBody();

		// TODO TEST-2: add extra assertions
		assertThat(parties).isNotNull();
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertThat(parties.size()).isGreaterThan(0);
		assertThat(parties.get(0).getId()).isNotEqualTo(parties.get(1).getId());
		assertThat(parties.get(0).getPlayers().size()).isLessThanOrEqualTo(5);
		assertThat(parties.get(1).getPlayers().size()).isLessThanOrEqualTo(5);
	}

	@Test
	public void test_updateParty() throws Exception {
		// TODO TEST-3: open, test PUT endpoint
		
		List<Player> listOfNewPlayers = new ArrayList<Player>();
		
		Player newPlayerOne = new Player("Zero", Role.TANK);
		Player newPlayerTwo = new Player("Mecha", Role.HEALER);
		Player newPlayerThree = new Player("Veemon", Role.DPS);
		Player newPlayerFour = new Player("Agumon", Role.DPS);
		Player newPlayerFive = new Player("Palette", Role.DPS);
		newPlayerOne.setAge(playerService.getAge(newPlayerOne.getName()));
		newPlayerTwo.setAge(playerService.getAge(newPlayerTwo.getName()));
		newPlayerThree.setAge(playerService.getAge(newPlayerThree.getName()));
		newPlayerFour.setAge(playerService.getAge(newPlayerFour.getName()));
		newPlayerFive.setAge(playerService.getAge(newPlayerFive.getName()));
		
		Collections.addAll(listOfNewPlayers, 
				newPlayerOne,
				newPlayerTwo,
				newPlayerThree,
				newPlayerFour,
				newPlayerFive);
		
		var newParty = new Party(listOfNewPlayers);
				
		HttpEntity<Party> entity = new HttpEntity<Party>(newParty);
		ResponseEntity<Void> responseEntity = restTemplate.exchange(getUrl("/party/"), HttpMethod.PUT, entity, Void.class);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

	}

	@Test
	public void test_addPlayerToParty() throws Exception {
		// TODO TEST-4: open, test PUT endpoint
		final var partyId = 2;
		restTemplate.delete(getUrl("/party/"+ partyId + "/player/" + "John"));
		
		Player newPlayer = new Player("Axel", Role.DPS);
		newPlayer.setAge(playerService.getAge(newPlayer.getName()));
		ResponseEntity<Void> responseEntity = restTemplate.postForEntity(getUrl("/party/"+ partyId + "/player"), newPlayer, Void.class);
		
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void test_removePlayerToParty() throws Exception {
		// TODO TEST-5: open, test PUT endpoint
		final var partyId = 1;
		final var name = "Joe";
		
		ResponseEntity<Void> ResponseEntity = restTemplate.exchange(getUrl("/party/" + partyId + "/player/" + name), HttpMethod.DELETE, null, Void.class);
		assertThat(ResponseEntity.getStatusCodeValue()).isEqualTo(200);
	}


	@Test
	public void test_getParties_party_integrity() throws Exception {


		// TODO TEST-6: assert each party has 1 tank, 1 healer and 3 DPS,
		//  if not use the client (restTemplate) to call the api to fix the data,
		//  then assert the party integrity again
		boolean result = false;
		for (int index = 0; index < 6; index++) {
			ResponseEntity<List<Party>> responseEntity = restTemplate.exchange(
					getUrl("/party"),
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<>() {
					});
			List<Party> parties = responseEntity.getBody();
			assertThat(parties).isNotNull();
			result = integrityChecker(parties.size(), parties);
			if (result == true) {
				assertThat(result).isTrue();
				break;
			}
		}
		assertThat(result).isTrue();
	}
	
	private boolean integrityChecker(int partySize, List<Party> parties) {
		
		for (int index = 0; index < partySize; index++) {
			List<Player> party1 = parties.get(index).getPlayers();
			int partyId = parties.get(index).getId();
			
			long tankCounter = party1.stream().filter(tank -> tank.getRole() == Role.TANK).count();
			long healerCounter = party1.stream().filter(tank -> tank.getRole() == Role.HEALER).count();
			long dpsCounter = party1.stream().filter(tank -> tank.getRole() == Role.DPS).count();
			
			if (tankCounter < 1L || healerCounter < 1L || dpsCounter < 3L) {
				if (tankCounter < 1L) { 
					Player newPlayer = new Player("Test_" + partyId, Role.TANK);
					newPlayer.setAge(playerService.getAge(newPlayer.getName()));
					ResponseEntity<Void> addResponseEntity = restTemplate.postForEntity(getUrl("/party/"+ partyId + "/player"), newPlayer, Void.class);
				}
				if (healerCounter < 1L) {
					Player newPlayer = new Player("Test_" + partyId, Role.HEALER);
					newPlayer.setAge(playerService.getAge(newPlayer.getName()));
					ResponseEntity<Void> addResponseEntity = restTemplate.postForEntity(getUrl("/party/"+ partyId + "/player"), newPlayer, Void.class);
				}
				if (dpsCounter < 3L) {
					Player newPlayer = new Player("Test_" + partyId, Role.DPS);
					newPlayer.setAge(playerService.getAge(newPlayer.getName()));
					ResponseEntity<Void> addResponseEntity = restTemplate.postForEntity(getUrl("/party/"+ partyId + "/player"), newPlayer, Void.class);
				}
				return false;
			}
			
		}
		return true;
	}

	private String getUrl(String extra) {
		return "http://localhost:" + port + extra;
	}
}