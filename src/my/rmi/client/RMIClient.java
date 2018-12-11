package my.rmi.client;

import java.io.*;
import java.rmi.*;
import java.util.*;
import org.json.*;

import my.database.*;
import my.rmi.share.*;

public class RMIClient {

	public static RMIInterface getRMIInterface(String ip, int port, String service) throws Exception {
		RMIInterface rif = null;
		String rmi = "//" + ip + ":" + port + "/" + service;
		rif = (RMIInterface) Naming.lookup(rmi);
		return rif;
	}

	public static void main(String[] args) throws Exception {

		//Get RMIInterface
		RMIInterface rif = getRMIInterface("localhost", 3000, "test");

		//Get Connection Parameters
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream("src/my/config/config.properties");
		prop.load(fis);
		fis.close();

		//Construct Connection Parameters JSON
		JSONObject conparams=new JSONObject();
		String[] paramkeys = { "dbtype","dbaddr", "dbname", "dbusername", "dbpassword" };
		for (String paramkey : paramkeys)
			conparams.put(paramkey, prop.getProperty(paramkey));

		//Create Connection
		UUID uuid = rif.createConnection(conparams.toString());
		
		//Execute Operation
		JSONObject exeparams = new JSONObject();
		
		//Construct Operation Parameters JSON
		exeparams.put("opr", "SELECT");
		exeparams.put("table", "goods");
		
		//Get Operation Result 
		String result = rif.getExecResult(uuid,exeparams.toString());
		JSONObject resjson = new JSONObject(result);
		
		System.out.println(resjson);
		
		//Destory Connection
		rif.destoryConnection(uuid);
	}

}
