package utilities;

import model.abstracts.DBObject;

public class DBObjectUtil {
	public static boolean verifyDBObject(DBObject obj) {
		if (obj == null)
			return false;
		if (StringUtil.isNullOrEmpty(obj.getDBRowKey()))
			return false;
		if (StringUtil.isNullOrEmpty(obj.getDBObjType()))
			return false;
		return true;
	}
}
