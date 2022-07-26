package net.mckinneymail.commands;

import net.mckinneymail.F5Admin;
import net.mckinneymail.api.PoolEnableApi.State;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name="pool-status")
public class PoolStatusCommand implements Runnable {
	@ParentCommand
	F5Admin f5admin;
	
	@Option(names={"-p", "--pool"}, description="Pool name", required=true)
	String pool;
	
	@Option(names= {"-m", "--member"}, description="Member name", required=true)
	String member;
	
	@Option(names= {"--enable"}, description="Enable pool member")
	boolean enabled;
	
	@Option(names= {"--disable"}, description="Disable pool member")
	boolean disabled;
	
	@Option(names= {"--force-offline"}, description="Force pool member offline")
	boolean forceOffline;
	
	@Override
	public void run() {
		checkParameters();
		
		State state = checkParameters();
		boolean success = f5admin.getF5Commands().setPoolState(pool, member, state);
		if(success)
			System.out.println("Success!");
		else
			System.out.println("Failure!");
	}

	private State checkParameters() {
		int count = 0;
		if( enabled )
			count++;
		if(disabled)
			count++;
		if(forceOffline)
			count++;
		if( count != 1) {
			System.err.println("Must only one of --enabled, --disabled, --force-offline");
			System.exit(1);
		}
		
		if(enabled)
			return State.ENABLED;
		if(disabled)
			return State.DISABLED;
		if(forceOffline)
			return State.FORCE_OFFLINE;
		return null;	//should never get to this one
	}

}
