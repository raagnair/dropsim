package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import model.interfaces.DBObject;
import utilities.DBObjectUtil;

public class ElephantDriver {
	private static String DB_ENV_VAR = "JDBC_DATABASE_URL";
	private static ElephantDriver INSTANCE;
	private static Object lock = new Object();
	private Connection connection;

	private static ObjectMapper MAPPER;
	private static ObjectWriter WRITER;
	private static Map<Class<? extends DBObject>, ObjectReader> READER_MAP;

	static {
		MAPPER = new ObjectMapper();
		WRITER = MAPPER.writer();
		READER_MAP = new HashMap<>();
	}

	public static ObjectWriter WRITER() {
		return WRITER;
	}

	public static ObjectReader READER(Class<? extends DBObject> cls) {
		if (READER_MAP.get(cls) == null) {
			synchronized (lock) {
				if (READER_MAP.get(cls) == null) {
					READER_MAP.put(cls, MAPPER.readerFor(cls));
				}
			}
		}

		return READER_MAP.get(cls);
	}

	public static ElephantDriver getInstance() {
		if (INSTANCE == null) {
			synchronized (lock) {
				if (INSTANCE == null) {
					INSTANCE = new ElephantDriver();
				}
			}
		}

		return INSTANCE;
	}

	private ElephantDriver() {
		try {
			String dbConnection = System.getenv(DB_ENV_VAR);
			if (dbConnection == null) {
				throw new Exception("No $JDBC_DATABASE_URL environment variable found.");
			}
			this.connection = DriverManager.getConnection(dbConnection);
			this.connection.setAutoCommit(false);
		} catch (Exception e) {
			System.err.println(e.getStackTrace());
		}
	}

	public ResultSet runQuery(String query) throws Exception {
		if (query == null || query.length() == 0) {
			return null;
		}
		Statement stmt = this.connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		stmt.closeOnCompletion();

		return rs;
	}

	public int insert(DBObject obj) throws Exception {
		if (!DBObjectUtil.verifyDBObject(obj))
			return -1;

		Statement stmt = this.connection.createStatement();
		stmt.executeUpdate("delete from objects where rowkey = '" + obj.getDBRowKey() + "' and objtype = '"
				+ obj.getDBObjType() + "';");

		int returnVal = stmt.executeUpdate("insert into objects(rowkey, objtype, code) values ('" + obj.getDBRowKey()
				+ "','" + obj.getDBObjType() + "','" + obj.getDBCode().replace("\'", "\'\'") + "');");

		this.connection.commit();

		stmt.close();
		return returnVal;
	}

	public List<DBObject> read(Class<? extends DBObject> cls) throws Exception {
		List<DBObject> returnedList = new LinkedList<>();

		if (cls == null)
			return returnedList;

		Statement stmt = this.connection.createStatement();
		ResultSet rs = stmt.executeQuery("select code from objects where objtype = '" + cls.getName() + "';");

		while (rs.next()) {
			returnedList.add(DBObject.inflate(rs.getString(1), cls));
		}

		return returnedList;
	}
}
