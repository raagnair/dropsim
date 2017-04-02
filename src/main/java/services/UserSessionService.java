package services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import database.ElephantDriver;

public class UserSessionService {
	private static UserSessionService INSTANCE;
	private static Object lock = new Object();

	@SuppressWarnings("unused")
	// TODO - Do we need to load anything from DB? If not, remove driver.
	private ElephantDriver driver;
	private SecureRandom random;
	private Map<String, String> activeUsers;

	private UserSessionService(ElephantDriver driver) {
		this.activeUsers = new HashMap<>();
		this.driver = driver;
		this.random = new SecureRandom();
	}

	public static UserSessionService getInstance(ElephantDriver driver) {
		if (INSTANCE == null) {
			synchronized (lock) {
				if (INSTANCE == null) {
					INSTANCE = new UserSessionService(driver);
				}
			}
		}
		return INSTANCE;
	}

	public String insertNewSession(String email) {
		String session = new BigInteger(130, this.random).toString(32);
		while (this.activeUsers.containsKey(session)) {
			session = new BigInteger(130, this.random).toString(32);
		}

		this.activeUsers.put(session, email);
		return session;
	}

	public String getEmailForSession(String sid) {
		return this.activeUsers.get(sid);
	}
}
