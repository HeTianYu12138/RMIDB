package my.database;

import java.util.*;
import org.json.*;

public abstract class DatabaseConnection {

	protected static DatabaseType dbtype = null;

	public DatabaseType getDBtype() {
		return dbtype;
	}

	public abstract String getSQLStatement(JSONObject params) throws Exception;

	public abstract JSONObject getExecResult(JSONObject params) throws Exception;

	public abstract boolean close();

}
