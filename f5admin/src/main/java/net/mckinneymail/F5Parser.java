package net.mckinneymail;

import java.util.HashMap;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class F5Parser {

	private final String poolName;
	private final String memberName;
	
	public F5Parser(String poolName, String memberName) {
		this.poolName = poolName;
		this.memberName = memberName;
	}
	
	public int parsePoolCurrentSessions(String jsonData) {
		String entry = "https://localhost/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName + "/~Common~" + poolName + "/stats";
		String members = "https://localhost/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName + "/~Common~" + poolName + "/members/stats";
		String member = "https://localhost/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName + "/~Common~" + poolName + "/members/~Common~" + memberName + "/stats";
		String path = "$['entries']['" + entry + "']['nestedStats']['entries']['" + members
				+ "']['nestedStats']['entries']['" + member + "']['nestedStats']['entries']['serverside.curConns']";

		DocumentContext jsonContext = JsonPath.parse(jsonData);
//				System.out.println("path: " + path);
		HashMap<String, Integer> currentSessionsJson = jsonContext.read(path);
		int currentSessions = currentSessionsJson.get("value");
		return currentSessions;
	}

}
