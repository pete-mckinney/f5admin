package net.mckinneymail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

public class F5Config {
	
	private final String SERVER_PROP = "server";
	private final String USER_PROP = "user";
	private final String POOL_PROP = "poolPattern";
	private final String MEMBER_PROP = "memberPattern";
	
	private String server = "";
	private String user = "";
	private String poolPattern = "";
	private String memberPattern = "";
	
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPoolPattern() {
		return poolPattern;
	}

	public void setPoolPattern(String poolPattern) {
		this.poolPattern = poolPattern;
	}

	public String getMemberPattern() {
		return memberPattern;
	}

	public void setMemberPattern(String memberPattern) {
		this.memberPattern = memberPattern;
	}

	public void read() {
		try(InputStream inputStream = new FileInputStream(getConfigPath().toString()) ) {
			Properties prop = new Properties();
			prop.load(inputStream);
			if(prop.containsKey(SERVER_PROP))
				server = prop.getProperty(SERVER_PROP);
			if(prop.containsKey(USER_PROP))
				user = prop.getProperty(USER_PROP);
			if(prop.containsKey(POOL_PROP))
				poolPattern = prop.getProperty(POOL_PROP);
			if(prop.containsKey(MEMBER_PROP))
				memberPattern = prop.getProperty(MEMBER_PROP);
		} catch (Exception err) {
			//no-op If we don't find property file, no big deal
		}
	}
	
	public void write() throws IOException {
		try(OutputStream outputStream = new FileOutputStream(getConfigPath().toString()) ) {
			Properties prop = new Properties();
			prop.setProperty(SERVER_PROP, server);
			prop.setProperty(USER_PROP, user);
			prop.setProperty(POOL_PROP, poolPattern);
			prop.setProperty(MEMBER_PROP, memberPattern);
			prop.store(outputStream, "");
		}
	}
	
	public Path getConfigPath() {
		Path path = FileSystems.getDefault().getPath( System.getProperty("user.home"), ".f5admin.properties");
		return path;
	}

}
