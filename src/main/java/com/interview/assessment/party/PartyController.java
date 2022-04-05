package com.interview.assessment.party;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.assessment.player.Player;

@RestController
@RequestMapping("/party")
public class PartyController {

	private final PartyService partyService;

	public PartyController(PartyService partyService) {
		this.partyService = partyService;
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public Party getParty(@PathVariable int id) {
		return partyService.getParty(id);
	}

	@GetMapping(value = "", produces = "application/json")
	public List<Party> getParties() {
		return partyService.getParties();
	}

	@PutMapping(value = "", produces = "application/json")
	public void updateParty(@RequestBody Party party) {
		partyService.updateParty(party);
	}

	@PostMapping(value = "/{id}/player", produces = "application/json")
	public void addPlayerToParty(@PathVariable int id, @RequestBody Player player) {
		partyService.addPlayerToParty(id, player);
	}

	@DeleteMapping(value = "/{id}/player/{playerName}", produces = "application/json")
	public void removePlayerToParty(@PathVariable int id, @PathVariable String playerName) {
		partyService.removePlayerFromParty(id, playerName);
	}
}
