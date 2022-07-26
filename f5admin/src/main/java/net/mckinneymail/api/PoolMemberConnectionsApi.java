package net.mckinneymail.api;

import java.util.HashMap;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.mckinneymail.F5Http;

public class PoolMemberConnectionsApi {
	F5Http f5http;
	String poolName;
	String memberName;

	public PoolMemberConnectionsApi(F5Http f5http, String poolName, String memberName) {
		this.f5http = f5http;
		this.poolName = poolName;
		this.memberName = memberName;
	}

	public int run() {
		String json = f5http.httpGet(getUrl());

		return parse(json);

	}
	
	private String getUrl() {
		return "/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName + "/stats";
	}

	private int parse(String json) {
		String entry = "https://localhost/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName + "/~Common~" + poolName + "/stats";
		String members = "https://localhost/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName + "/~Common~" + poolName + "/members/stats";
		String member = "https://localhost/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName + "/~Common~" + poolName + "/members/~Common~" + memberName + "/stats";
		String path = "$['entries']['" + entry + "']['nestedStats']['entries']['" + members
				+ "']['nestedStats']['entries']['" + member + "']['nestedStats']['entries']['serverside.curConns']";

		DocumentContext jsonContext = JsonPath.parse(json);
		HashMap<String, Integer> currentSessionsJson = jsonContext.read(path);
		int currentSessions = currentSessionsJson.get("value");
		return currentSessions;
	}

}


