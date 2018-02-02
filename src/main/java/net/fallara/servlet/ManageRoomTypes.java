package net.fallara.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.fallara.db.DBManager;
import net.fallara.db.DvcSqlOperations;
import net.fallara.dvc.DvcLoggedInUser;
import net.fallara.dvc.RoomType;

/**
 * Servlet implementation class RoomTypes
 */
//@WebServlet(urlPatterns = {"/ManageRoomTypes.do"})
public class ManageRoomTypes extends HttpServlet {
	protected static Logger log = Logger.getLogger(ManageRoomTypes.class);
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageRoomTypes() {
	super();
    }

    /**
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	DvcLoggedInUser currentUser = (DvcLoggedInUser)request.getSession().getAttribute("loggedInUser");
	DBManager dbm = (DBManager) getServletContext().getAttribute("dvcDBManager");

	if (request.getParameter("deleteID") != null) {

	    log.debug("Delete room requested");

	    try {
		DvcSqlOperations.deleteRoomType(dbm, Integer.parseInt(request.getParameter("deleteID")), currentUser.getGplusEmail() );
	    } catch (Exception e) {
	    	log.error("Error during delete room type ", e);
		request.setAttribute("sqlError", e.getMessage());
	    }

	}

	try {
	    ArrayList<RoomType> allRT = DvcSqlOperations.getAllRoomType(dbm);
	    request.getSession().setAttribute("roomTypeArray", allRT);
	} catch (Exception e) {
		log.error("Error getting all room types ", e);
	}

	RequestDispatcher rd = request.getRequestDispatcher("/manageRoomType.jsp");
	rd.forward(request, response);

    }

    /**
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	DvcLoggedInUser currentUser = (DvcLoggedInUser)request.getSession().getAttribute("loggedInUser");
	DBManager dbm = (DBManager) getServletContext().getAttribute("dvcDBManager");

	String action = request.getParameter("action");
	if (action != null) {
	    if (action.equals("addRoomType")) {

		try {
		    String rtDesc = request.getParameter("txtRoomTypeDesc");
		    int roomSleeps = Integer.parseInt(request.getParameter("txtSleeps"));

		    DvcSqlOperations.addRoomType(dbm, rtDesc, roomSleeps, currentUser.getGplusEmail());

		} catch (Exception e) {
		    String myError = e.getMessage();
		    if (!myError.equals("null")) {
			request.setAttribute("sqlError", myError);
		    } else {
			request.setAttribute("sqlError", e.toString());
		    }
		}

	    }
	}

	this.doGet(request, response);
    }

}
