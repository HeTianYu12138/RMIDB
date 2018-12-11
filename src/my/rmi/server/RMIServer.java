package my.rmi.server;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import org.json.*;

import my.database.*;
import my.rmi.share.*;
import my.database.*;

public class RMIServer extends UnicastRemoteObject implements RMIInterface {

	private Hashtable<UUID, DatabaseConnection> dbcontable = new Hashtable<UUID, DatabaseConnection>();

	public RMIServer(String ip, int port, String service) throws Exception {
		String rmi = "//" + ip + ":" + port + "/" + service;
		LocateRegistry.createRegistry(port);
		Naming.rebind(rmi, this);
	}

	public UUID createConnection(String params) throws Exception {
		JSONObject paramsjson=new JSONObject(params);
		UUID uuid = null;
		DatabaseConnection dbcon = null;
		switch (paramsjson.getString("dbtype").toUpperCase()) {
		case "MYSQL":
			dbcon = new MySQLConnection(paramsjson);
			break;
		default:
			break;
		}
		if (dbcon != null)
			dbcontable.put((uuid = UUID.randomUUID()), dbcon);
		return uuid;
	}

	public boolean destoryConnection(UUID uuid) throws Exception {
		DatabaseConnection dbcon = dbcontable.remove(uuid);
		if (dbcon == null)
			return false;
		dbcon.close();
		return true;
	}

	public String getExecResult(UUID uuid, String params)
			throws Exception {
		DatabaseConnection dbcon = dbcontable.get(uuid);
		if (dbcon == null)
			return null;
		return dbcon.getExecResult(new JSONObject(params)).toString();
	}

	public String getSQLStatement(UUID uuid,String params)
			throws Exception {
		DatabaseConnection dbcon = dbcontable.get(uuid);
		if (dbcon == null)
			return null;
		return dbcon.getSQLStatement(new JSONObject(params));
	}

	public static void main(String[] args) throws Exception {
		new RMIServer("localhost", 3000, "test");
	}

}
