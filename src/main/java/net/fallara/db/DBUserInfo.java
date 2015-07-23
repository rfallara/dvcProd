package net.fallara.db;

public abstract class DBUserInfo 
{
	private String uid;
	private String pwd;
	private String cat;
	private String host;
	
	

	public DBUserInfo()
	{
		
	}
	
	public DBUserInfo(String userID, String password, String catalog, String hostname)
	{
		uid = userID;
		pwd = password;
		cat = catalog;
		host = hostname;
	}
	
	public String getUserID()
	{
		return uid;
	}
	
	public void setUserID(String value)
	{
		uid = value;
	}
	
	public String getPassword()
	{
		return pwd;
	}
	
	public void setPassword(String value)
	{
		pwd = value;
	}
	
	public String getCatalog()
	{
		return cat;
	}
	
	public void setCatalog(String catalog)
	{
		cat = catalog;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String hostname) {
		host = hostname;
	}
}
