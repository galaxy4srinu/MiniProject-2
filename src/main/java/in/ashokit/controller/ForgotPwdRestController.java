package in.ashokit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.service.UserMgmtService;

@RestController
public class ForgotPwdRestController {
	@Autowired
	private UserMgmtService service;

	@GetMapping("/forgotpassword/{emailId}")
	public String forgotPassword(@PathVariable("emailId") String email) {
		String status = service.forgotPassword(email);
		return status;
	}
}
