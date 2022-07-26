package net.mckinneymail.api;

import java.util.List;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.mckinneymail.F5Http;

public class PoolsApi {
	F5Http f5http;

	public PoolsApi(F5Http f5http) {
		this.f5http = f5http;
	}

	public List<String> run() {
		String json = f5http.httpGet(getUrl());

		return parse(json);

	}
	
	private String getUrl() {
		return "/mgmt/tm/ltm/pool";
	}

	private List<String> parse(String json) {
		String path = "$['items'].*.['name']";

		DocumentContext jsonContext = JsonPath.parse(json);
		List<String> pools = jsonContext.read(path);
		return pools;

	}
}