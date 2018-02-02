package net.fallara.db;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//import org.apache.log4j.Logger;
import java.util.logging.Logger;
/**
 * Application Lifecycle Listener implementation class DBManagerSetup
 *
 */
//@WebListener
public class DBManagerSetup implements ServletContextListener {
	protected static Logger log = Logger.getLogger(DBManagerSetup.class.getName());

	private DBManager dbm = null;

    /**
     * @param sce
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext sc = sce.getServletContext();

        String uid = System.getProperty("db-user");
        String pwd = System.getProperty("db-password");
        String cat = System.getProperty("db-name");;
        String host = System.getProperty("db-server");

        //get db con info from context init params
        /*String uid = sc.getInitParameter("dbuserid");
        String pwd = sc.getInitParameter("dbuserpwd");
        String cat = sc.getInitParameter("dbinitcat");
        
        String host = sc.getInitParameter("dbhost-dvc");

        if (host == null) {
        	log.info("dbhost-dvc not available must be prod so using web.xml value");
        	host = sc.getInitParameter("dbhost");
        } else {
        	log.info("development DB value found in context.xml, using db at " + host);
        }
        */
        log.info("Current DB host - " + host);

        if (uid != null && pwd != null && cat != null && host != null) {
            //set the scb for mySQL
            ServerConnectionBehavior scb = new MySQLServerConnectionBehavior(uid, pwd, cat, host);
			//System.out.println(scb.getConnectionDetails());
            //System.out.println(scb.getConnectionURL());

            //create the manager
            dbm = new DBManager(scb);

            //add dbm to servlet context
            sc.setAttribute("dvcDBManager", dbm);
            
            log.info("dvcDBManager created and added to context");
        } else {
            log.warning("DB connection details are incorrect in servlet context!");
        }
    }

    /**
     * @param sce
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //cleanup the connection when the context is destroyed
        if (dbm != null) {
            if (dbm.isConnected()) {
                dbm.closeConnection(false);
            }
        }
        dbm = null;
        log.info("dvcDBManager has been destroyed.");
    }

}
