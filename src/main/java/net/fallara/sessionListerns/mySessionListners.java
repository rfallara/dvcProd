package net.fallara.sessionListerns;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import net.fallara.db.DBManager;

/**
 * Application Lifecycle Listener implementation class mySessionListners
 *
 */
@WebListener
public class mySessionListners implements HttpSessionListener {
	
	protected static Logger log = Logger.getLogger(mySessionListners.class);
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

    	log.debug("A new session has been created - " + myHttpSessionEvent.getSession().getId());
	
		activeUsers++;
	
		log.debug("Active user count = " + activeUsers);
	
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
    
    	String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    	log.debug("A session has been detroyed - " + myHttpSessionEvent.getSession().getId());
	
		if (activeUsers > 0) {
		    activeUsers--;
		}
	
		log.debug("Active user count = " + activeUsers);
	
		if (activeUsers == 0) {
		    System.out.println("[" + timestamp + "] " + "No more active sessions closing DB connection");
		    DBManager dbm = (DBManager) myHttpSessionEvent.getSession().getServletContext().getAttribute("dvcDBManager");
		    dbm.closeConnection(false);
		}

    }

}
