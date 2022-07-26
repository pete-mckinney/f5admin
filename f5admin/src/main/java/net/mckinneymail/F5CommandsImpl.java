package net.mckinneymail;

import java.util.List;

import net.mckinneymail.api.PoolEnableApi;
import net.mckinneymail.api.PoolEnableApi.State;
import net.mckinneymail.api.PoolMemberConnectionsApi;
import net.mckinneymail.api.PoolMemberStatusApi;
import net.mckinneymail.api.PoolsApi;



public class F5CommandsImpl implements F5Commands {

	String hostName;
	String userName;
	char[] password;
	boolean verbose;
	F5Http f5http;

	public F5CommandsImpl(String hostName, String userName, char[] password, boolean verbose) {
		this.hostName = hostName;
		this.userName = userName;
		this.password = password;
		this.verbose = verbose;
		f5http = new F5Http(hostName, userName, password, verbose);
	}

	@Override
	public int getPoolMemberConnections(String poolName, String memberName) {
		
		PoolMemberConnectionsApi api = new PoolMemberConnectionsApi(f5http, poolName, memberName);
		return api.run();
	}

	@Override
	public List<String> pools() {
		PoolsApi pools = new PoolsApi(f5http);
		return pools.run();
	}
	
	public boolean setPoolState(String poolName, String memberName, State state) {
		PoolEnableApi poolEnable = new PoolEnableApi(f5http, poolName, memberName, state);
		return poolEnable.run();
	}
	
	public List<String> getDisabledPoolMembers(String poolName) {
		PoolMemberStatusApi poolStatus = new PoolMemberStatusApi(f5http, poolName);
		return poolStatus.run();
	}

}
