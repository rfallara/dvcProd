package net.fallara.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import net.fallara.db.DBManager;
import net.fallara.db.DvcSqlOperations;
import net.fallara.dvc.DvcLoggedInUser;

/**
 * Servlet implementation class GoogleAuth
 */
@WebServlet("/GoogleAuth.do")
public class GoogleAuth extends HttpServlet {


	protected static Logger log = Logger.getLogger(GoogleAuth.class);
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
     * Gson object to serialize JSON responses to requests to this servlet.
     */
    private static final Gson GSON = new Gson();

    /*
     * Creates a client secrets object from the client_secrets.json file.
     */
	  //private static GoogleClientSecrets clientSecrets;
    //static {
    //  try {
    //    Reader reader = new FileReader("client_secrets.json");
    //    clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
    //  } catch (IOException e) {
    //    throw new Error("No client_secrets.json found", e);
    //  }
    //}

    /*
     * This is the Client ID that you generated in the API Console.
     */
    //private static final String CLIENT_ID = clientSecrets.getWeb().getClientId();
    private static final String CLIENT_ID = "624350122436-f3h0e16docp6p0ivhstiq5r7oi0m5rf1.apps.googleusercontent.com";

    /*
     * This is the Client Secret that you generated in the API Console.
     */
    //private static final String CLIENT_SECRET = clientSecrets.getWeb().getClientSecret();
    private static final String CLIENT_SECRET = "ewDvRL-nQFBegqXiNpTTczj4";

    /*
     * Optionally replace this with your application's name.
     */
    //private static final String APPLICATION_NAME = "dvcWeb";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleAuth() {
	super();
    }

    /**
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("application/json");

	// Only connect a user that is not already connected.
	String tokenData = (String) request.getSession().getAttribute("token");
	
	if (tokenData != null) {
		response.setStatus(HttpServletResponse.SC_OK);
	    response.getWriter().print(GSON.toJson("Current user is already connected."));
	    return;
	} else {
		log.debug("Token is null");
	}
	      // Ensure that this is no request forgery going on, and that the user
	// sending us this connect request is the user that was supposed to.
	//System.out.println("Current Request CSFRToken = " + request.getParameter("state"));
	//System.out.println("Current Session CSFRToken = " + request.getSession().getAttribute("state"));
	if (!request.getParameter("state").equals(request.getSession().getAttribute("state"))) {
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    response.getWriter().print(GSON.toJson("Invalid state parameter."));
	    return;
	}

	// Normally the state would be a one-time use token, however in our
	// simple case, we want a user to be able to connect and disconnect
	// without reloading the page.  Thus, for demonstration, we don't
	// implement this best practice.
	//request.getSession().removeAttribute("state");
	BufferedReader requestContent = request.getReader();
	String code = requestContent.readLine();
	//getContent(request.getInputStream(), resultStream);
	//String code = new String(resultStream.toByteArray(), "UTF-8");

	try {
	    // Upgrade the authorization code into an access and refresh token.
	    GoogleTokenResponse tokenResponse
		    = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JSON_FACTORY,
			    CLIENT_ID, CLIENT_SECRET, code, "postmessage").execute();

	    // You can read the Google user ID in the ID token.
	    // This sample does not use the user ID.
	    GoogleIdToken idToken = tokenResponse.parseIdToken();
	    String gplusId = idToken.getPayload().getSubject();
	    String gplusEmail = idToken.getPayload().getEmail();
	    String gplusHostedDomain = idToken.getPayload().getHostedDomain();
	    tokenData = tokenResponse.toString();
	    
	    log.debug("Token data: Subject-"+gplusId + " Email-" + gplusEmail + " HostedDomain-" + gplusHostedDomain);

	    //****This block uses the token to access a google service*****
	    //try {
	    // Build credential from stored token data.
	    //GoogleCredential credential = new GoogleCredential.Builder()
	    //    .setJsonFactory(JSON_FACTORY)
	    //    .setTransport(TRANSPORT)
	    //    .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
	    //    .setFromTokenResponse(JSON_FACTORY.fromString(
	    //        tokenData, GoogleTokenResponse.class));
	    // Create a new authorized API client.
	    //Plus service = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential)
	    //    .setApplicationName(APPLICATION_NAME)
	    //    .build();
	    // Get a list of people that this user has shared with this app.
	    //Person myself = service.people().get("me").execute();
	    //PeopleFeed people = service.people().list("me", "visible").execute();
	    //response.setStatus(HttpServletResponse.SC_OK);
	    //response.getWriter().print(GSON.toJson(people));
	    //} catch (IOException e) {
	    //  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    //  response.getWriter().print(GSON.toJson("Failed to read data from Google. " +
	    //      e.getMessage()));
	    //}
	    if (validateLoginDetails(gplusId, gplusEmail, gplusHostedDomain, request)) {
		request.getSession().setAttribute("token", tokenResponse.toString());
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().print(GSON.toJson("VALID"));
	    } else {

		GoogleCredential credential = new GoogleCredential.Builder()
			.setJsonFactory(JSON_FACTORY)
			.setTransport(TRANSPORT)
			.setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
			.setFromTokenResponse(JSON_FACTORY.fromString(
					tokenData, GoogleTokenResponse.class));
		// Execute HTTP GET request to revoke current token.
		HttpResponse revokeResponse = TRANSPORT.createRequestFactory()
			.buildGetRequest(new GenericUrl(
					String.format(
						"https://accounts.google.com/o/oauth2/revoke?token=%s",
						credential.getAccessToken()))).execute();

		request.getSession().setAttribute("token", null);

		if (revokeResponse.getStatusCode() == 200) {
		    response.setStatus(HttpServletResponse.SC_OK);
		} else {
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		response.getWriter().print(GSON.toJson("INVALID_DOMAIN"));
	    }

	} catch (TokenResponseException e) {
	    request.getSession().setAttribute("token", null);
	    request.getSession().setAttribute("gplusId", null);
	    request.getSession().setAttribute("gplusEmail", null);
	    request.getSession().setAttribute("gplusHostedDomain", null);
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    response.getWriter().print(GSON.toJson("Failed to upgrade the authorization code."));
	} catch (IOException e) {
	    request.getSession().setAttribute("token", null);
	    request.getSession().setAttribute("gplusId", null);
	    request.getSession().setAttribute("gplusEmail", null);
	    request.getSession().setAttribute("gplusHostedDomain", null);
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    response.getWriter().print(GSON.toJson("Failed to read token data from Google. "
		    + e.getMessage()));
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

	    if (myUser.getDvcOwner() != null) {

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
