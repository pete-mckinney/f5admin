package net.mckinneymail;

import net.mckinneymail.commands.DrainCommand;
import net.mckinneymail.commands.PoolStatusCommand;
import net.mckinneymail.commands.PoolsCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;
import org.jboss.weld.environment.se.*;


@Command(subcommands = { PoolsCommand.class, DrainCommand.class, PoolStatusCommand.class })
public class F5Admin implements Runnable {
	public static void main(String[] args) {
		new CommandLine(new F5Admin()).execute(args);
	}
	
	private F5Commands f5commands;


	@Option(names = "-v", scope = ScopeType.INHERIT, description = "Verbose output")
	public boolean verbose;

	@Option(names = {"-h", "--host"}, scope = ScopeType.INHERIT, description = "F5 Host name")
	public String host;
	
	@Option(names = {"-u", "--user"}, scope = ScopeType.INHERIT, description = "User login for F5")
	public String user;
	
	@Option(names = {"--password" }, scope = ScopeType.INHERIT, description = "Password", interactive = true)
	private char[] password;
	
	@Option(names = "--password:env", scope = ScopeType.INHERIT, description = "Environment variable that contains password" )
	private String passwordEnvironmentVariable;

	public char[] getPassword() {
		if(password != null) {
			return password;
		}
		
		if(passwordEnvironmentVariable != null) {
			String envValue = System.getenv(passwordEnvironmentVariable);
			if(envValue != null)
				return envValue.toCharArray();
		}
		
		throw new IllegalStateException("Password required: use --password or --password:env");
	}
	
	
	public F5Commands getF5Commands() {
		if( f5commands == null) {
			f5commands = new F5CommandsImpl(host, user, getPassword(), verbose);
		}
		return f5commands;
	}

	
	@Override
	public void run() {
		Weld weld = new Weld();
	    WeldContainer container = weld.initialize();
	    
		F5Config config = new F5Config();
		config.read();
		System.out.println("here i am in the root");
		System.out.print("pwd: " + new String(getPassword()) );
	}
}
