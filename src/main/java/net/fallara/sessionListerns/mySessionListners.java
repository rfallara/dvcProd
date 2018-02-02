package net.fallara.sessionListerns;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

//import org.apache.log4j.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;

import net.fallara.db.DBManager;

/**
 * Application Lifecycle Listener implementation class mySessionListners
 *
 */
//@WebListener
public class mySessionListners implements HttpSessionListener {
	
	protected static Logger log = Logger.getLogger(mySessionListners.class.getName());
    public int activeUsers;

    /**
     * Default constructor.
     */
    public mySessionListners() {
	//activeUsers = 0;

    }

    /**
     * @param myHttpSessionEvent
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent myHttpSessionEvent) {

    	String hnt = myHttpSessionEvent.getSession().getServletContext().getInitParameter("dev-dvc");
    	
    	if (hnt != null){
        	log.info("THIS IS A DEVELOPMENT ENVIRONMENT - adding tag " + hnt);
        	myHttpSessionEvent.getSession().setAttribute("headerNameTag", hnt);
        }

    	log.info("A new session has been created - " + myHttpSessionEvent.getSession().getId());
	
		activeUsers++;
	
		log.info("Active user count = " + activeUsers);
	
		myHttpSessionEvent.getSession().setMaxInactiveInterval(20 * 60);
		
		
		
		String state = new BigInteger(130, new SecureRandom()).toString(32);
	        myHttpSessionEvent.getSession().setAttribute("state", state);
		
		//System.out.println(arg0.getSession().getAttribute("state"));

    }

    /**
     * @param myHttpSessionEvent
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent myHttpSessionEvent) {
    
    	log.info("A session has been detroyed - " + myHttpSessionEvent.getSession().getId());
	
		if (activeUsers > 0) {
		    activeUsers--;
		}
	
		log.info("Active user count = " + activeUsers);
	
		if (activeUsers == 0) {
		    log.info("No more active sessions closing DB connection");
		    DBManager dbm = (DBManager) myHttpSessionEvent.getSession().getServletContext().getAttribute("dvcDBManager");
		    dbm.closeConnection(false);
		}

    }

}
