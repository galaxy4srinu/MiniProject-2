package in.ashokit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.bindings.UserRegForm;
import in.ashokit.service.UserMgmtService;

@RestController
public class RegistrationRestController {
	@Autowired
	private UserMgmtService service;

	@GetMapping("/email-check/{emailId}")
	public String emailCheck(@PathVariable("emailId") String email) {
		String status = service.emailCheck(email);
		return status;
	}

	@GetMapping("/country")
	public Map<Integer, String> getCountries() {
		Map<Integer, String> getCountries = service.loadCountries();
		return getCountries;
	}

	@GetMapping("/states/{countryId}")
	public Map<Integer, String> getStates(@PathVariable("countryId") Integer countrId) {
		Map<Integer, String> getStates = service.loadStates(countrId);
		return getStates;
	}

	@GetMapping("/cities/{stateId}")
	public Map<Integer, String> getCities(@PathVariable("stateId") Integer statesId) {
		Map<Integer, String> getCities = service.loadCities(statesId);
		return getCities;
	}

	@PostMapping("/register")
	public String registerUser(@RequestBody UserRegForm userRegForm) {
		String status = service.registerUser(userRegForm);
		return status;
	}

}
