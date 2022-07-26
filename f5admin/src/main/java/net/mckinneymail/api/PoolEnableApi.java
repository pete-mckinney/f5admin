package net.mckinneymail.api;

import net.mckinneymail.F5Http;
import net.mckinneymail.Response;

public class PoolEnableApi {
	F5Http f5http;
	String poolName;
	String memberName;
	State state;

	public enum State { ENABLED, DISABLED, FORCE_OFFLINE };

	public PoolEnableApi(F5Http f5http, String poolName, String memberName, State state) {
			this.f5http = f5http;
			this.poolName = poolName;
			this.memberName = memberName;
			this.state = state;
		}

	public boolean run() {
		String patchJson = "";
		if( state == State.ENABLED )
			patchJson = "{\"session\":\"user-enabled\", \"state\": \"user-up\"}";
		if( state == State.DISABLED )
			patchJson = "{\"session\":\"user-disabled\", \"state\": \"user-up\"}";
		if( state == State.FORCE_OFFLINE )
			patchJson = "{\"session\":\"user-disabled\", \"state\": \"user-down\"}";
		Response response = f5http.httpPatch(getUrl(), patchJson);

		return response.code == 200;
	}

	private String getUrl() {
		return "/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName;
	}

	/*
	 from postman
	 enabled
	 {"session":"user-enabled", "state": "user-up"}
	 
	 disabled
	 {"session":"user-disabled", "state": "user-up"}
	 
	 force-offline
	 {"session":"user-disabled", "state": "user-down"}
	 */
}
