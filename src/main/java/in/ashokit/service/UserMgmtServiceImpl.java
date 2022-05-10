package in.ashokit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.bindings.LoginForm;
import in.ashokit.bindings.UnlockAccForm;
import in.ashokit.bindings.UserRegForm;
import in.ashokit.entity.CityMasterEntity;
import in.ashokit.entity.CountryMasterEntity;
import in.ashokit.entity.StateMasterEntity;
import in.ashokit.entity.UserAccountEntity;
import in.ashokit.repository.CityRepository;
import in.ashokit.repository.CountryRepository;
import in.ashokit.repository.StateRepository;
import in.ashokit.repository.UserAccountRepository;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {
	@Autowired
	private UserAccountRepository userRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private CityRepository cityRepo;

	@Override
	public String login(LoginForm loginForm) {
		UserAccountEntity entity = userRepo.findByEmailAndPwd(loginForm.getEmail(), loginForm.getPwd());
		if (entity == null) {
			return "Invalid credentials";
		}
		if (entity.getAccStatus().equals("LOCKED")) {
			return "Your account is locked";
		}
		return "SUCCESS";
	}

	@Override
	public String emailCheck(String email) {

		UserAccountEntity entity = userRepo.findByEmail(email);
		if (entity == null) {
			return "UNIQUE";
		}
		return "DUPLICATE";
	}

	@Override
	public Map<Integer, String> loadCountries() {
		List<CountryMasterEntity> countries = countryRepo.findAll();
		Map<Integer, String> countryMap = new HashMap<>();

		for (CountryMasterEntity country : countries) {
			countryMap.put(country.getCountryId(), country.getCountryName());
		}
		return countryMap;
	}

	@Override
	public Map<Integer, String> loadStates(Integer countryId) {
		List<StateMasterEntity> states = stateRepo.findAllByCountryId(countryId);
		Map<Integer, String> stateMap = new HashMap<>();
		for (StateMasterEntity state : states) {
			stateMap.put(state.getStateId(), state.getStateName());
		}

		return stateMap;
	}

	@Override
	public Map<Integer, String> loadCities(Integer stateId) {
		List<CityMasterEntity> cities = cityRepo.findAllByStateId(stateId);
		Map<Integer, String> cityMap = new HashMap<>();
		for (CityMasterEntity city : cities) {
			cityMap.put(city.getCityId(), city.getCityName());
		}
		return cityMap;
	}

	@Override
	public String registerUser(UserRegForm userRegForm) {
		UserAccountEntity userEntity = new UserAccountEntity();
		BeanUtils.copyProperties(userRegForm, userEntity);
		userEntity.setAccStatus("LOCKED");
		userEntity.setPwd(generateRandomString());

		userRepo.save(userEntity);

		// TODO: send email to user

		return "SUCCESS";
	}

	@Override
	public String unlockAccount(UnlockAccForm unlockAccForm) {
		if(!(unlockAccForm.getNewPwd().equals(unlockAccForm.getConfirmNewPwd()))) {
			return "NEW PASSWORD AND CONFIRM NEW PASSWORD ARE NOT SAME";
		}
	
		return "ACCOUNT UNLOCKED";
	}

	@Override
	public String forgotPassword(String email) {
		UserAccountEntity entity = userRepo.findByEmail(email);
		if(entity==null) {
			return "EMAIL NOT REGISTERED";
		}
		// Send email to user with temp pwd.
		return "EMAIL SENT TO USER";
	}

	public String generateRandomString() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 6;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;

	}

}
