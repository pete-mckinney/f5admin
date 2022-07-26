package net.mckinneymail.commands;

import net.mckinneymail.F5Admin;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name="drain")
public class DrainCommand implements Runnable {

	@ParentCommand
	F5Admin f5admin;
	
	@Option(names={"-p", "--pool"}, description="Pool name", required=true)
	String pool;
	
	@Option(names= {"-m", "--member"}, description="Member name", required=true)
	String member;
	
	@Option(names= {"-t", "--target"}, description="Number of connections to consider drained, defaults to 0", defaultValue="0")
	int target;
	
	@Option(names = {"-d", "--delay"}, description="Delay in seconds between poling requests, defaults to 10", defaultValue="10")
	int delay;
	
	@Option(names = {"--bell"}, description="Ring bell when done")
	boolean ringBell;

	@Override
	public void run() {
		int currentSessions = Integer.MAX_VALUE;	//set non-zero number
		int wait = delay * 1000;	//options is in seconds, thread waits in millis
		try {
			while(currentSessions > target ) {
				currentSessions = f5admin.getF5Commands().getPoolMemberConnections(pool, member);
				if( currentSessions > target ) {
					System.out.print("Remaining sessions: " + currentSessions + "              \r");
					Thread.sleep(wait);
				}
			}			
		} catch (InterruptedException e) {
			System.err.println("Interrupted!                     ");
			System.exit(1);
		}
		if( currentSessions != Integer.MAX_VALUE)
			System.out.println("currentSessions: " + currentSessions + "                              ");
		if(ringBell) {
			System.out.println("\007\007\007");
		}
	}

}
