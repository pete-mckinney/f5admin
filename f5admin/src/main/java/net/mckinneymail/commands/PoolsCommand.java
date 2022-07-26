package net.mckinneymail.commands;

import net.mckinneymail.F5Admin;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name="pools")
public class PoolsCommand implements Runnable {

	@ParentCommand
	F5Admin f5admin;
	
	@Option(names={"--show-disabled"}, description="Show disabled members", required = false)
	boolean showDisabled;
	
	@Option(names={"--show-members"}, description="Show all members", required = false)
	boolean showMembers;
	
	@Option(names= {"-f", "--filter"}, description="Filter pool names by this", required=false)
	String filter;
	
	@Override
	public void run() {
		
		f5admin.getF5Commands()
			.pools()
			.stream()
			.filter(this::filterName)
			.forEach(this::poolAction);
	}
	
	private boolean filterName(String name) {
		return filter == null || name == null || name.toLowerCase().contains(filter.toLowerCase());
	}
	
	private void poolAction(String name) {
		System.out.println("pool: " + name);
		if(showDisabled) {
			f5admin.getF5Commands().getDisabledPoolMembers(name).forEach(member -> System.out.println("  " + member));
		}
	}

}
