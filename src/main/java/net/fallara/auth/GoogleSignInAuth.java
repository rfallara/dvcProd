package net.fallara.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import net.fallara.db.DBManager;
import net.fallara.db.DvcSqlOperations;
import net.fallara.dvc.DvcLoggedInUser;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class GoogleSignInAuth
 */
@WebServlet("/GoogleSignInAuth.do")
public class GoogleSignInAuth extends HttpServlet {
	
	protected static Logger log = Logger.getLogger(GoogleSignInAuth.class);
	private static final long serialVersionUID = 1L;
	
    /*
     * Default HTTP transport to use to make HTTP requests.
     */
    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    /*
     * Default JSON factory to use to deserialize JSON.
     */
    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

    /*
     * This is the Client ID that you generated in the API Console.
     */
    //private static final String CLIENT_ID = clientSecrets.getWeb().getClientId();
    private static final String CLIENT_ID = "1078652156481-cnabsdppfboi0cu74q1o3m98crk7gtpe.apps.googleusercontent.com";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleSignInAuth() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Only connect a user that is not already connected.
		String tokenData = (String) request.getSession().getAttribute("token");
		if (tokenData != null) {
			//log.debug("Token already exist in session no need to continue validation");
		    return;
		} else {
			log.debug("Token is null need to validate user");
		}
		
		
		String idTokenData = (String) request.getParameter("idtoken");
		log.debug(idTokenData);
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, JSON_FACTORY)
	    .setAudience(Arrays.asList(CLIENT_ID))
	    .build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken;
		try {
			idToken = verifier.verify(idTokenData);
			if (idToken != null) {
				Payload payload = idToken.getPayload();
				log.debug("User hosted domain: " + payload.getHostedDomain());
				log.debug("User ID: " + payload.getSubject());
				log.debug("User email: " + payload.getEmail());
				
				String gplusId = payload.getSubject();
			    String gplusEmail = payload.getEmail();
			    String gplusHostedDomain = payload.getHostedDomain();
			    tokenData = payload.toString();
			    
				if (validateLoginDetails(gplusId, gplusEmail, gplusHostedDomain, request)) {
					request.getSession().setAttribute("token", tokenData);
					response.getWriter().print("VALID");
				} else {
					log.info("Login not in correct domain");
					request.getSession().setAttribute("token", null);
				    request.getSession().setAttribute("gplusId", null);
				    request.getSession().setAttribute("gplusEmail", null);
				    request.getSession().setAttribute("gplusHostedDomain", null);
				    request.getSession().invalidate();
				    response.getWriter().print("INVALID_DOMAIN");
				}
				
				
				
			} else {
				log.debug("Invalid ID token. idToken is null");
				request.getSession().setAttribute("token", null);
			    request.getSession().setAttribute("gplusId", null);
			    request.getSession().setAttribute("gplusEmail", null);
			    request.getSession().setAttribute("gplusHostedDomain", null);
			}
		} catch (GeneralSecurityException e) {
			log.error("Token validation error", e);
			request.getSession().setAttribute("token", null);
		    request.getSession().setAttribute("gplusId", null);
		    request.getSession().setAttribute("gplusEmail", null);
		    request.getSession().setAttribute("gplusHostedDomain", null);
		}

	}
	
	
	
	
	 private boolean validateLoginDetails(String gplusId, String gplusEmail, String gplusHostedDomain, HttpServletRequest request) {
		if (gplusHostedDomain == null) {
		    return false;
		}

		if (gplusHostedDomain.equals("fallara.net")) {
			DvcLoggedInUser myUser = new DvcLoggedInUser();
			
			myUser.setGplusId(gplusId);
			myUser.setGplusEmail(gplusEmail);
			myUser.setGplusHostedDomain(gplusHostedDomain);
			
			DBManager dbm = (DBManager) getServletContext().getAttribute("dvcDBManager");
			
			try {
				
				myUser.setAccessLevel(DvcSqlOperations.getAccessLevelByEmail(dbm, gplusEmail));
				
				if ( myUser.getAccessLevel() == 7 ) {
				    myUser.setAllOwnerList(DvcSqlOperations.getAllDvcOwner(dbm));
				}
				
				myUser.setDvcOwner(DvcSqlOperations.getOwnerByEmail(dbm, gplusEmail));
			
				myUser = updateLoggedInUserPoints(dbm, myUser);
			} catch (Exception ex) {
				System.out.println("Error getting user access level");
				System.out.println(ex.getMessage());
			}

			request.getSession().setAttribute("loggedInUser", myUser);
			try {
				DvcSqlOperations.addEventLogEntry(dbm, gplusEmail, "User Logged In.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			    return true;
		
		} else {
		    request.getSession().removeAttribute("loggedInUser");
		    return false;
		}
	}
	 
	 
	 

	public static DvcLoggedInUser updateLoggedInUserPoints(DBManager dbm, DvcLoggedInUser myUser) throws SQLException {
		myUser.setBankedPersonalPoints(DvcSqlOperations.getOwnerPersonalPointCount(dbm,
			myUser.getDvcOwner().getOwnerId(),
			"banked"));
		myUser.setCurrentPersonalPoints(DvcSqlOperations.getOwnerPersonalPointCount(dbm,
			myUser.getDvcOwner().getOwnerId(),
			"current"));
		myUser.setBorrowPersonalPoints(DvcSqlOperations.getOwnerPersonalPointCount(dbm,
			myUser.getDvcOwner().getOwnerId(),
			"borrow"));
		
		myUser.setBankedActualPoints(DvcSqlOperations.getActualPointCount(dbm, "banked"));
		myUser.setCurrentActualPoints(DvcSqlOperations.getActualPointCount(dbm, "current"));
		myUser.setCurrentBankedActualPoints(DvcSqlOperations.getActualPointCount(dbm, "current-banked"));
		myUser.setBorrowActualPoints(DvcSqlOperations.getActualPointCount(dbm, "borrow"));
		    
		return myUser;
	}
	

}
