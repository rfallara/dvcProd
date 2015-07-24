package net.fallara.sessionListerns;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.fallara.db.DBManager;

/**
 * Application Lifecycle Listener implementation class mySessionListners
 *
 */
@WebListener
public class mySessionListners implements HttpSessionListener {

    public int activeUsers;

    /**
     * Default constructor.
     */
    public mySessionListners() {
	//activeUsers = 0;

    }

    /**
     * @param arg0
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
    
	    String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	    
	    
		System.out.println("[" + timestamp + "] " + "A new session has been created - " + arg0.getSession().getId());
	
		activeUsers++;
	
		System.out.println("Active user count = " + activeUsers);
	
		arg0.getSession().setMaxInactiveInterval(20 * 60);
		
		
		
		String state = new BigInteger(130, new SecureRandom()).toString(32);
	        arg0.getSession().setAttribute("state", state);
		
		//System.out.println(arg0.getSession().getAttribute("state"));

    }

    /**
     * @param arg0
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
    
    	String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		System.out.println("[" + timestamp + "] " + "A session has been detroyed - " + arg0.getSession().getId());
	
		if (activeUsers > 0) {
		    activeUsers--;
		}
	
		System.out.println("Active user count = " + activeUsers);
	
		if (activeUsers == 0) {
		    System.out.println("[" + timestamp + "] " + "No more active sessions closing DB connection");
		    DBManager dbm = (DBManager) arg0.getSession().getServletContext().getAttribute("dvcDBManager");
		    dbm.closeConnection(false);
		}

    }

}
