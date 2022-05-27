package in.ashokit.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import in.ashokit.util.EmailUtils;

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

	@Autowired
	private EmailUtils emailUtils;

	private Logger logger = LoggerFactory.getLogger(UserMgmtServiceImpl.class);

	@Override
	public String login(LoginForm loginForm) {
		UserAccountEntity entity = userRepo.findByEmailAndPwd(loginForm.getEmail(), loginForm.getPwd());
		if (entity == null) {
			return "Invalid credentials";
		}
		if ("LOCKED".equals(entity.getAccStatus())) {
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

		UserAccountEntity savedEntity = userRepo.save(userEntity);

		String email = userRegForm.getEmail();
		String subject = "Welcome to Ashok IT";
		String fileName = "UNLOCK-ACC-EMAIL-BODY-TEMPLATE.txt";
		String body = readMailBodyContent(fileName, userEntity);

		boolean isSent = emailUtils.sendEmail(email, subject, body);

		if (savedEntity.getUserId() != null && isSent) {
			return "SUCCESS";
		}

		return "ERROR";
	}

	@Override
	public String unlockAccount(UnlockAccForm unlockAccForm) {
		if (!(unlockAccForm.getNewPwd().equals(unlockAccForm.getConfirmNewPwd()))) {
			return "NEW PASSWORD AND CONFIRM NEW PASSWORD ARE NOT SAME";
		}
		return "ACCOUNT UNLOCKED";
	}

	@Override
	public String forgotPassword(String email) {
		UserAccountEntity entity = userRepo.findByEmail(email);
		if (entity == null) {
			return "Invalid Email Id";
		}

		String fileName = "RECOVER-PASSWORD-EMAIL-BODY-TEMPLATE.txt";
		String body = readMailBodyContent(fileName, entity);
		String subject = "Recover Password-Ashok IT";

		boolean isSent = emailUtils.sendEmail(email, subject, body);
		if (isSent) {
			return "Password sent to registered email";
		}

		return "ERROR";
	}

	// Method to generate random string
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

	// Method to read mail body content
	private String readMailBodyContent(String fileName, UserAccountEntity entity) {
		String mailBody = null;
		try {
			StringBuilder sb = new StringBuilder();
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			mailBody = sb.toString();
			mailBody = mailBody.replace("{FNAME}", entity.getFname());
			mailBody = mailBody.replace("{LNAME}", entity.getLname());
			mailBody = mailBody.replace("{TEMP-PWD}", entity.getPwd());
			mailBody = mailBody.replace("{EMAIL}", entity.getEmail());
			br.close();
			fr.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return mailBody;
	}

}
