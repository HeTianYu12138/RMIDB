package my.rmi.share;

import java.rmi.*;
import java.util.*;

import my.database.*;

public interface RMIInterface extends Remote {
	public UUID createConnection(String params) throws Exception;

	public boolean destoryConnection(UUID uuid) throws Exception;

	public String getExecResult(UUID uuid, String params) throws Exception;
}
