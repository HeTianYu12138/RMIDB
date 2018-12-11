package my.database;

import java.sql.*;
import java.util.*;
import org.json.*;

public class MySQLConnection extends DatabaseConnection {

	private static Connection conn = null;

	public MySQLConnection(JSONObject params) throws Exception {
		dbtype = DatabaseType.MYSQL;

		String driverName = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://";
		String addr = params.getString("dbaddr");
		String dbname = params.getString("dbname");
		String[] options = { "serverTimezone=GMT" };
		url = url + addr + "/" + dbname;
		if (options.length > 0) {
			url += "?";
			for (String op : options)
				url += "&" + op;
		}
		String username = params.getString("dbusername");
		String password = params.getString("dbpassword");

		Class.forName(driverName);
		conn = (Connection) DriverManager.getConnection(url, username, password);
	}

	public String getSQLStatement(JSONObject params) throws Exception {
		String sql = null;
		System.out.println(params);
		switch (params.getString("opr").toUpperCase()) {
		/*TO BE COMPLETED*/
		case "SELECT":
			sql = "SELECT ";
			String columns = null;
			if (params.has("column"))
				columns = params.getString("column");
			if (columns == null)
				sql += "* ";
			else
				sql += columns + " ";
			sql += "FROM ";
			String tables = null;
			if (params.has("table"))
				tables = params.getString("table");
			if (tables == null)
				sql += "* ";
			else
				sql += tables + " ";
			String conds = null;
			if (params.has("condition"))
				conds = params.getString("condition");
			if (conds != null) {
				sql += "WHERE ";
				sql += conds + " ";
			}
			sql += ";";
			break;
		case "UPDATE":
			break;
		case "INSERT":
			break;
		case "DELETE":
			break;
		default:
			break;
		}
		System.out.println(sql);
		return sql;
	}

	public JSONObject getExecResult(JSONObject params) throws Exception {
		String sql = getSQLStatement(params);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		JSONObject jsonres=new JSONObject();
		switch(params.getString("opr")){
		case "SELECT":
			ResultSet rs = pstmt.executeQuery();
			jsonres.put("status", true);
			JSONArray queryres=new JSONArray();
			ResultSetMetaData rsmd = rs.getMetaData();
			int colcount = rsmd.getColumnCount();
			String[] colnames = new String[colcount];
			for (int i = 1; i <= colcount; i++)
				colnames[i - 1] = rsmd.getColumnName(i);
			while (rs.next()) {
				JSONObject queryrow=new JSONObject();
				for (String colname : colnames)
					queryrow.put(colname, rs.getObject(colname));
				queryres.put(queryrow);
			}
			jsonres.put("res",queryres);
			rs.close();
			break;
		default:
			boolean result=pstmt.execute();
			jsonres.put("status", result);
			break;
		}		
		pstmt.close();
		return jsonres;
	}

	public boolean close() {
		boolean result = true;
		try {
			conn.close();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

}
