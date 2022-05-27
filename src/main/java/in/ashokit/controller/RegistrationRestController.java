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
		return service.emailCheck(email);
	}

	@GetMapping("/country")
	public Map<Integer, String> getCountries() {
		return service.loadCountries();
	}

	@GetMapping("/states/{countryId}")
	public Map<Integer, String> getStates(@PathVariable("countryId") Integer countrId) {
		return service.loadStates(countrId);
	}

	@GetMapping("/cities/{stateId}")
	public Map<Integer, String> getCities(@PathVariable("stateId") Integer statesId) {
		return service.loadCities(statesId);
	}

	@PostMapping("/register")
	public String registerUser(@RequestBody UserRegForm userRegForm) {
		return service.registerUser(userRegForm);
	}

}
