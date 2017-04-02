package engine;

import model.user.User;
import utilities.StringUtil;

public class UserLE {
	public static boolean checkLogin(LogicEngine engine, String email, String password) {
		if (StringUtil.isNullOrEmpty(email))
			return false;

		User us = engine.users.get(email);

		if (us == null)
			return false;

		try {
			String hash = StringUtil.hashPassword(password, us.getSalt());
			if (StringUtil.equals(hash, us.getPassword(), true))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public enum REG_RESULT {
		NULLS("Empty email or password"), DUP("Email is already registered"), OK("Success"), ERR("Error");

		public final String reason;

		REG_RESULT(String reason) {
			this.reason = reason;
		}
	}

	public static REG_RESULT registerUser(LogicEngine engine, String email, String password) {
		if (StringUtil.isNullOrEmpty(email) || StringUtil.isNullOrEmpty(password))
			return REG_RESULT.NULLS;

		if (engine.users.containsKey(email))
			return REG_RESULT.DUP;

		User reg = new User();
		reg.setEmail(email);
		byte[] salt = StringUtil.getSalt();
		reg.setSalt(salt);
		reg.setPassword(StringUtil.hashPassword(password, salt));

		try {
			engine.driver.insert(reg);
			engine.users.put(email, reg);
			return REG_RESULT.OK;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return REG_RESULT.ERR;
	}

	public static User getUser(LogicEngine engine, String email) {
		return engine.users.get(email);
	}

}
