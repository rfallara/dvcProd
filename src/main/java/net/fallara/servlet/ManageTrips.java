package net.fallara.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.fallara.auth.GoogleAuth;

import net.fallara.db.DBManager;
import net.fallara.db.DvcSqlOperations;
import net.fallara.dvc.BookableRoom;
import net.fallara.dvc.CreateTripResults;
import net.fallara.dvc.DvcLoggedInUser;
import net.fallara.dvc.DvcOwner;
import net.fallara.dvc.Trip;

/**
 * Servlet implementation class ManageTrips
 */
@WebServlet("/ManageTrips.do")
public class ManageTrips extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageTrips() {
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

	try {

	    ArrayList<BookableRoom> allBr = DvcSqlOperations.getAllBookableRoom(dbm);
	    request.getSession().setAttribute("bookableRoomArray", allBr);

	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}

	try {

	    ArrayList<DvcOwner> allDvcOwner = DvcSqlOperations.getAllDvcOwner(dbm);
	    request.getSession().setAttribute("dvcOwnerArray", allDvcOwner);

	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}

	if (request.getParameter("deleteID") != null) {

	    System.out.println("Delete trip requested");

	    try {
		DvcSqlOperations.deleteTripRecord(dbm, Integer.parseInt(request.getParameter("deleteID"))
                        ,currentUser.getGplusEmail());
	    } catch (Exception e) {
		request.setAttribute("sqlError", e.getMessage());
	    }
            
            try {
                currentUser = GoogleAuth.updateLoggedInUserPoints(dbm, currentUser);
                request.getSession().setAttribute("loggedInUser", currentUser);
            } catch (SQLException ex) {
                Logger.getLogger(ManageTrips.class.getName()).log(Level.SEVERE, null, ex);
            }

	}

	try {
	    ArrayList<Trip> allTrip = DvcSqlOperations.getAllTrip(dbm);
	    request.getSession().setAttribute("tripArray", allTrip);
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}

	RequestDispatcher rd = request.getRequestDispatcher("/manageTrip.jsp");
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
	if (action.equals("addTrip")) {

	    Trip newTrip = new Trip();

	    try {
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Date bookedDate = sdf.parse(request.getParameter("addDateBooked"));
		Date checkInDate = sdf.parse(request.getParameter("addDateCheckIn"));
		Date checkOutDate = sdf.parse(request.getParameter("addDateCheckOut"));

		newTrip.setBookedDate(bookedDate);
		newTrip.setCheckInDate(checkInDate);
		newTrip.setCheckOutDate(checkOutDate);

		newTrip.setdOwner(new DvcOwner(Integer.parseInt(request.getParameter("addOwner")), ""));

		BookableRoom br = new BookableRoom();
		br.setBrId(Integer.parseInt(request.getParameter("addBookableRoom")));
		newTrip.setBr(br);

		newTrip.setNotes(request.getParameter("addNotes"));

		newTrip.setPointsNeeded(Integer.parseInt(request.getParameter("addPointsNeeded")));

	    } catch (ParseException | NumberFormatException e) {
		String myError = e.getMessage();
		if (!myError.equals("null")) {
		    request.setAttribute("sqlError", myError);
		} else {
		    request.setAttribute("sqlError", e.toString());
		}
		newTrip = null;
	    }

	    if (newTrip != null) {
		try {
		    CreateTripResults createdTripDetails = DvcSqlOperations.createTripRecord(dbm, newTrip, 
                            Integer.parseInt(request.getParameter("addPointsNeeded")), currentUser.getGplusEmail());
		    request.setAttribute("createdTripDetails", createdTripDetails);
		} catch (Exception e) {
		    System.out.println(e.getMessage());
		}
	    }
            
            try {
                currentUser = GoogleAuth.updateLoggedInUserPoints(dbm, currentUser);
                request.getSession().setAttribute("loggedInUser", currentUser);
            } catch (SQLException ex) {
                Logger.getLogger(ManageTrips.class.getName()).log(Level.SEVERE, null, ex);
            }
	    this.doGet(request, response);
	}
    }
}
