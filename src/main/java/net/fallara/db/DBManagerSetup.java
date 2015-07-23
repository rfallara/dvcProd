package net.fallara.db;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class DBManagerSetup
 *
 */
@WebListener
public class DBManagerSetup implements ServletContextListener {

    private DBManager dbm = null;

    /**
     * @param sce
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext sc = sce.getServletContext();

        //get db con info from context init params
        String uid = sc.getInitParameter("dbuserid");
        String pwd = sc.getInitParameter("dbuserpwd");
        String cat = sc.getInitParameter("dbinitcat");
        String host = sc.getInitParameter("dbhost");

        if (uid != null && pwd != null && cat != null && host != null) {
            //set the scb for mySQL
            ServerConnectionBehavior scb = new MySQLServerConnectionBehavior(uid, pwd, cat, host);
			//System.out.println(scb.getConnectionDetails());
            //System.out.println(scb.getConnectionURL());

            //create the manager
            dbm = new DBManager(scb);

            //add dbm to session context
            sc.setAttribute("dvcDBManager", dbm);
            System.out.println("dvcDBManager created and added to context");
        } else {
            System.out.println("DB connection details are incorrect in servlet context!");
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
        System.out.println("dvcDBManager has been destroyed.");
    }

}
