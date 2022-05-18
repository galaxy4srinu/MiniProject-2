package in.ashokit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.bindings.UnlockAccForm;
import in.ashokit.service.UserMgmtService;

@RestController
public class UnlockRestController {
	@Autowired
	private UserMgmtService service;

	@PostMapping("/unlock-account")
	public String unlockUser(@RequestBody UnlockAccForm unlockAccForm) {
		String status = service.unlockAccount(unlockAccForm);
		return status;
	}
}
