package net.fallara.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import org.apache.log4j.Logger;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import com.mysql.jdbc.Driver;

public class MySQLServerConnectionBehavior extends DBUserInfo implements ServerConnectionBehavior
{
	protected static Logger log = Logger.getLogger(MySQLServerConnectionBehavior.class.getName());
	
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
		log.info("Starting getConnection to MySQL");
		try
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//Class.forName("com.mysql.jdbc.GoogleDriver").newInstance();


			String url = getConnectionURL();
			Connection cn = DriverManager.getConnection(url);
			log.info("connecting to: " + url);
			return cn;
			//try {
			//	// Load the class that provides the new "jdbc:google:mysql://" prefix.
			//	Class.forName("com.mysql.jdbc.GoogleDriver");
			//} catch (ClassNotFoundException e) {
			//	throw new ServletException("Error loading Google JDBC Driver", e);
			//}

			//Connection conn = DriverManager.getConnection(url);
		}
		//catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
		catch (  SQLException e)
		{
			log.warning("Issue during MySQL DB connection setup -- " + e.toString());

			return null;
		}
	}

	@Override
	public String getConnectionURL() {
		String url =  String.format("jdbc:mysql://%s/%s?user=%s&password=%s&autoReconnect=true"
				, getHost()
				, getCatalog()
				, getUserID()
				, getPassword());
		log.info("DB connection string = " + url);

		return url;
		/*
		String url;
		if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
			// Check the System properties to determine if we are running on appengine or not
			// Google App Engine sets a few system properties that will reliably be present on a remote
			// instance.
			log.info("Found Google App Engine Runtime environment");
			url = System.getProperty("ae-cloudsql.cloudsql-database-url");
			try {
				// Load the class that provides the new "jdbc:google:mysql://" prefix.
				Class.forName("com.mysql.jdbc.GoogleDriver");
				log.info("Created class for com.mysql.jdbc.GoogleDriver");
			} catch (ClassNotFoundException e) {
				log.warning ("Error loading Google JDBC Driver -- " + e.toString());
			}
			log.info("db url set to: " + url);

		} else {
			// Set the url with the local MySQL database connection url when running locally
			log.info("Not running in Google App Engine Cloud");
			try {
				// Load the class that provides the new "jdbc:google:mysql://" prefix.
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				log.info("Created class for com.mysql.jdbc.GoogleDriver");
			} catch (ClassNotFoundException| InstantiationException | IllegalAccessException e) {
				log.warning ("Error loading Google JDBC Driver -- " + e.toString());
			}


			url = System.getProperty("ae-cloudsql.local-database-url");

			log.info("MySQL Connection String -- " + url);
		}
		return url;
		*/
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
