package net.mckinneymail.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.mckinneymail.F5Http;

public class PoolMemberStatusApi {
	F5Http f5http;
	String poolName;

	public PoolMemberStatusApi(F5Http f5http, String poolName) {
		this.f5http = f5http;
		this.poolName = poolName;
	}

	public List<String> run() {
		String json = f5http.httpGet(getUrl());

		return parse(json);

	}
	
	private String getUrl() {
		return "/mgmt/tm/ltm/pool/" + poolName + "/members/";
	}

	private List<String> parse(String json) {
		//items array
		//filter by state != up or session != monitor-enabled
		//return name
		String path = "$.items.[?(@.state != 'up' || @.session !='monitor-enabled')].['name']";

		DocumentContext jsonContext = JsonPath.parse(json);
		ArrayList<String> names = jsonContext.read(path);
		return names;
	}

}


