package in.ashokit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.bindings.UserRegForm;
import in.ashokit.service.UserMgmtService;

@RestController
public class RegistrationRestController {
	@Autowired
	private UserMgmtService userMgmtService;

	@PostMapping("/createuser")
	public ResponseEntity<String> createUser(@RequestBody UserRegForm user) {

		return null;
	}

}
