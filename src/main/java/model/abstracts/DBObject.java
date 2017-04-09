package model.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;

import database.ElephantDriver;

public abstract class DBObject {
	@JsonIgnore
	public abstract String getDBRowKey();

	@JsonIgnore
	public String getDBObjType() {
		return this.getClass().getName();
	}

	@JsonIgnore
	public String getDBCode() throws Exception {
		return ElephantDriver.WRITER().writeValueAsString(this);
	}

	@JsonIgnore
	public static DBObject inflate(String json, Class<? extends DBObject> cls) throws Exception {
		return ElephantDriver.READER(cls).readValue(json);
	}

	@JsonIgnore
	@Override
	public String toString() {
		try {
			return getDBCode();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
