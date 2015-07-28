package net.fallara.servlet;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fallara.auth.GoogleSignInAuth;
import net.fallara.db.DBManager;
import net.fallara.db.DvcSqlOperations;
import net.fallara.dvc.DvcLoggedInUser;

/**
 * Servlet implementation class BankPoints
 */
@WebServlet("/BankPoints.do")
public class BankPoints extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Gson GSON = new Gson();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BankPoints() {
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

        RequestDispatcher rd = request.getRequestDispatcher("/bankPoints.jsp");
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

        String action = request.getParameter("action");

        if (action.equals("getBankablePoints")) {
            DBManager dbm = (DBManager) getServletContext().getAttribute("dvcDBManager");
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            try {
                Date bankedDate = sdf.parse(request.getParameter("bankDate"));
                ArrayList<Integer> bankablePoints = DvcSqlOperations.getBankablePoints(dbm, bankedDate);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("text/plain");
                response.getWriter().print(bankablePoints.size());

            } catch (Exception ex) {
                Logger.getLogger(BankPoints.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (action.equals("markPointsAsBanked")) {

            DBManager dbm = (DBManager) getServletContext().getAttribute("dvcDBManager");
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            try {
                Date bankedDate = sdf.parse(request.getParameter("bankDate"));
                int pointsToBank = Integer.parseInt(request.getParameter("bankCount"));
                int countOfBanked = DvcSqlOperations.markPointsAsBanked(dbm, bankedDate, pointsToBank);
                Map<String, String> x = new HashMap<>();

                if (countOfBanked == pointsToBank) {
                    try {
                        DvcLoggedInUser currentUser = (DvcLoggedInUser)request.getSession().getAttribute("loggedInUser");
                        currentUser = GoogleSignInAuth.updateLoggedInUserPoints(dbm, currentUser);
                        request.getSession().setAttribute("loggedInUser", currentUser);
                    } catch (SQLException ex) {
                        Logger.getLogger(ManageTrips.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("text/plain");
                    x.put("status", "OK");
                    x.put("msg", "Successfully banked " + countOfBanked + " points");
                    response.getWriter().print(GSON.toJson(x));
                }

            } catch (Exception ex) {
                Logger.getLogger(BankPoints.class.getName()).log(Level.SEVERE, null, ex);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("text/plain");
                response.getWriter().print("An error occured, points have not been properly banked.");
            }

        }

    }

}
