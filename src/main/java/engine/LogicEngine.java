package engine;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import database.ElephantDriver;
import model.abstracts.DBObject;
import model.user.User;

public class LogicEngine {
	private static LogicEngine INSTANCE;
	private static Object lock = new Object();

	ElephantDriver driver;
	Map<String, User> users;

	private LogicEngine(ElephantDriver driver) throws Exception {
		this.driver = driver;
		this.users = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

		populateStructures();
	}

	private void populateStructures() throws Exception {
		List<DBObject> users = this.driver.read(User.class);
		for (DBObject u : users) {
			User usr = (User) u;
			this.users.put(usr.getEmail(), usr);
		}
	}

	public static LogicEngine getInstance() throws Exception {
		return getInstance(ElephantDriver.getInstance());
	}

	public static LogicEngine getInstance(ElephantDriver driver) throws Exception {
		if (INSTANCE == null) {
			synchronized (lock) {
				if (INSTANCE == null) {
					INSTANCE = new LogicEngine(driver);
				}
			}
		}

		return INSTANCE;
	}
}
