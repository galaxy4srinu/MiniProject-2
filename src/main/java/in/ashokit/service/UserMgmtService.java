package in.ashokit.service;

import java.util.Map;

import in.ashokit.bindings.LoginForm;
import in.ashokit.bindings.UnlockAccForm;
import in.ashokit.bindings.UserRegForm;

public interface UserMgmtService {

	// Methods related to login functionality
	public String login(LoginForm loginForm);

	// Methods related to registration functionality
	public String emailCheck(String email);

	public Map<Integer, String> loadCountries();

	public Map<Integer, String> loadStates(Integer countryId);

	public Map<Integer, String> loadCities(Integer stateId);

	public String registerUser(UserRegForm userRegForm);

	// Methods related to unlock account functionality
	public String unlockAccount(UnlockAccForm unlockAccForm);
	
	//Methods related to forgot password functionality
	
	public String forgotPassword(String email);

}
