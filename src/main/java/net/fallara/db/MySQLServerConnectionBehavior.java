package net.fallara.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLServerConnectionBehavior
	extends DBUserInfo
	implements ServerConnectionBehavior
{
	public MySQLServerConnectionBehavior()
	{
		super();
	}
	
	public MySQLServerConnectionBehavior(String uid, String pwd, String cat, String hostname)
	{
		super(uid, pwd, cat, hostname);
	}

	@Override
	public Connection getConnection() {
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection cn = DriverManager.getConnection(getConnectionURL());
			return cn;
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public String getConnectionURL() {
		return String.format("jdbc:mysql://%s/%s?user=%s&password=%s&autoReconnect=true"
				, getHost()
				, getCatalog()
				, getUserID()
				, getPassword());
	}

	@Override
	public String getConnectionDetails() {
		return "MySQL Database Connection to " 
					+ getCatalog();
	}

	@Override
	public String getTablesSchemaQuery() {
		return "select table_name from information_schema.tables "
					+ "where table_schema = " + getCatalog();
	}
}
