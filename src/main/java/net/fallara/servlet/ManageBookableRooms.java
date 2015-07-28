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
import net.fallara.dvc.BookableRoom;
import net.fallara.dvc.DvcLoggedInUser;
import net.fallara.dvc.Resort;
import net.fallara.dvc.RoomType;

/**
 * Servlet implementation class ManageBookableRooms
 */
@WebServlet("/ManageBookableRooms.do")
public class ManageBookableRooms extends HttpServlet {
	
	protected static Logger log = Logger.getLogger(ManageBookableRooms.class);

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageBookableRooms() {
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

	    log.debug("Delete bookable room requested");

	    try {
		DvcSqlOperations.deleteBookableRoom(dbm, Integer.parseInt(request.getParameter("deleteID")), currentUser.getGplusEmail());
	    } catch (Exception e) {
		request.setAttribute("sqlError", e.getMessage());
		log.error("Error during delete", e);
	    }

	}

	try {
	    ArrayList<Resort> allResort = DvcSqlOperations.getAllResort(dbm);
	    request.getSession().setAttribute("resortArray", allResort);

	} catch (Exception e) {
	    log.error("Error during get resorts", e);
	}

	try {
	    ArrayList<RoomType> allRT = DvcSqlOperations.getAllRoomType(dbm);
	    request.getSession().setAttribute("roomTypeArray", allRT);
	} catch (Exception e) {
		log.error("Error during get room types", e);
	}

	try {

	    ArrayList<BookableRoom> allBr = DvcSqlOperations.getAllBookableRoom(dbm);
	    request.getSession().setAttribute("bookableRoomArray", allBr);

	} catch (Exception e) {
		log.error("Error during get bookable rooms", e);
	}

	RequestDispatcher rd = request.getRequestDispatcher("/manageBookableRoom.jsp");
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
	    if (action.equals("addBookableRoom")) {
		if (Integer.parseInt(request.getParameter("addResortId")) > 0
			&& Integer.parseInt(request.getParameter("addRoomTypeId")) > 0) {
		    try {
			DvcSqlOperations.addBookableRoom(dbm,
				Integer.parseInt(request.getParameter("addResortId")),
				Integer.parseInt(request.getParameter("addRoomTypeId")),
				currentUser.getGplusEmail()
			);

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
	}

	this.doGet(request, response);
    }

}
