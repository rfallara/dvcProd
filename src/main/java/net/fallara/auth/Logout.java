package net.fallara.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.fallara.db.DBManager;
import net.fallara.db.DvcSqlOperations;
import net.fallara.dvc.DvcLoggedInUser;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout.do")
public class Logout extends HttpServlet {

	
	protected static Logger log = Logger.getLogger(Logout.class);
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
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
    	DBManager dbm = (DBManager) getServletContext().getAttribute("dvcDBManager");
    	DvcLoggedInUser myUser = (DvcLoggedInUser) request.getSession().getAttribute("loggedInUser");
    	
    	try {
			DvcSqlOperations.addEventLogEntry(dbm, myUser.getGplusEmail(), "User Logged Out.");
		} catch (Exception e) {
			log.error("Error adding to event log", e);
		}
    	log.debug("User Logged Out." + myUser.getGplusEmail());
        request.getSession().invalidate();
        response.getWriter().print("<html>"
        		+ "<head>"
        		+ "<meta http-equiv='Content-Type' content='text/html; charset=US-ASCII'>"
        		+ "<title>DVC Vacation Points Management</title>"
        		+ "</head>"
        		+ "<body>"
        		+ "<div style='margin: 50px'>"
        		+ "<div style='text-align: center;'>"
        		+ "<H1>You have been logged out of the DVC Vacation Points Management System</H1>"
        		+ "<br>"
        		+ "</div>"
        		+ "</div>"
        		+ "</body>"
        		+ "</html>");
        
    }

}
