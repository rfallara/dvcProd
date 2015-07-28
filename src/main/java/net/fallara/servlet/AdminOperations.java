/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.fallara.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.fallara.db.DBManager;
import net.fallara.db.DvcSqlOperations;
import net.fallara.dvc.DvcLoggedInUser;
import static net.fallara.auth.GoogleSignInAuth.updateLoggedInUserPoints;
import org.apache.log4j.Logger;


/**
 *
 * @author rofallar
 */
@WebServlet("/AdminOperations.do")
public class AdminOperations extends HttpServlet {

	protected static Logger log = Logger.getLogger(DBManager.class);
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	if (request.getParameter("action").equals("overrideUser")) {

	    DvcLoggedInUser myUser = (DvcLoggedInUser) request.getSession().getAttribute("loggedInUser");
	    if (myUser.getAccessLevel() == 7) {
		try {
		    DBManager dbm = (DBManager) getServletContext().getAttribute("dvcDBManager");

		    int newId =  Integer.parseInt(request.getParameter("newOwnerId") );

		    myUser.setDvcOwner(DvcSqlOperations.getDvcOwner(dbm, newId));

		    myUser = updateLoggedInUserPoints(dbm, myUser);

		    request.getSession().setAttribute("loggedInUser", myUser);

		    response.setStatus(HttpServletResponse.SC_OK);
		    response.setContentType("text/plain");
		    response.getWriter().print("OK");

		} catch (Exception ex) {
			log.error("Error during override user ", ex);
		    response.setStatus(HttpServletResponse.SC_OK);
		    response.setContentType("text/plain");
		    response.getWriter().print("An error occured");
		}
	    }

	}

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
	return "Short description";
    }// </editor-fold>

}
