package net.mckinneymail;

import java.util.List;

import net.mckinneymail.api.PoolEnableApi.State;

public interface F5Commands {

	int getPoolMemberConnections(String poolName, String memberName);
	List<String> pools();
	boolean setPoolState(String poolName, String memberName, State state);
	List<String> getDisabledPoolMembers(String poolName);

}
