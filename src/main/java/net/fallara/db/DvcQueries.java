package net.fallara.db;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.fallara.dvc.Trip;

public class DvcQueries {

    public static String getTripDetails() {
	return "SELECT * from tripDetails";
    }

    public static String getRoomTypes() {
	return "SELECT * from Room_Type";
    }

    public static String getResorts() {
	return "SELECT * from Resort";
    }

    public static String getBookableRooms() {
	return "SELECT * from bookableRooms";
    }

    public static String getOwners() {
	return "SELECT * from Owner";
    }

    public static String getBankedPersonalPoints(int pointsNeeded, int ownerId,
	    Date checkIn) {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(checkIn); // set c to checkIn date
	c.add(Calendar.YEAR, -1); // set back one year
	String previousUseYear = sdf.format(c.getTime());

	return String.format(
		"SELECT Trip_ID, PP_ID, Use_Year, Point_Number, Owner_ID"
		+ " FROM Personal_Points"
		+ " WHERE ((Use_Year) < '%s' AND ((Owner_ID)='%s') AND ((Trip_ID) Is Null))"
		+ " ORDER BY Use_Year, Point_Number"
		+ " LIMIT %s", previousUseYear, ownerId, pointsNeeded);
    }

    public static String getCurrentPersonalPoints(int pointsNeeded, int ownerId,
	    Date checkIn) {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(checkIn); // set c to checkIn date
	String currentUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, -1); // set back one year
	String previousUseYear = sdf.format(c.getTime());

	return String.format(
		"SELECT Trip_ID, PP_ID, Use_Year, Point_Number, Owner_ID"
		+ " FROM Personal_Points"
		+ " WHERE ((Use_Year) < '%s' And (Use_Year) > '%s')"
		+ " AND ((Personal_Points.Owner_ID)= '%s') AND ((Trip_ID) Is Null)"
		+ " ORDER BY Use_Year, Point_Number"
		+ " LIMIT %s", currentUseYear, previousUseYear, ownerId,
		pointsNeeded);
    }

    public static String getBorrowPersonalPoints(int pointsNeeded, int ownerId,
	    Date checkIn) {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(checkIn); // set c to checkIn date
	String currentUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, 1); // set back one year
	String nextUseYear = sdf.format(c.getTime());

	return String.format(
		"SELECT Trip_ID, PP_ID, Use_Year, Point_Number, Owner_ID "
		+ " FROM Personal_Points "
		+ " WHERE (((Use_Year) < '%s' And (Use_Year) > '%s') AND ((Owner_ID) = '%s') AND ((Trip_ID) Is Null) ) "
		+ " ORDER BY Use_Year, Point_Number "
		+ " LIMIT %s", nextUseYear, currentUseYear, ownerId,
		pointsNeeded);
    }

    public static String getBankedActualPoints(int pointsNeeded, Date checkIn,
	    Date booked) {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(checkIn); // set c to checkIn date
	c.add(Calendar.YEAR, -1); // set back one year
	String previousUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, -1); // set back one year
	String previousTwoUseYear = sdf.format(c.getTime());

	c.setTime(booked);
	String bookedDate = sdf.format(c.getTime());

	return String.format("SELECT Trip_ID, AP_ID, Use_Year, Point_Number "
		+ " FROM Actual_Points "
		+ " WHERE (( Use_Year < '%s' And Use_Year > '%s') " //Find points from last year ONLY
		+ " AND (Trip_ID Is Null) "
		+ " AND (Banked_Date < '%s') ) " //Make sure it is being booked AFTER it has been marked as banked
		+ " ORDER BY Use_Year, Point_Number "
		+ " LIMIT %s", previousUseYear, previousTwoUseYear, bookedDate,
		pointsNeeded);
    }

    public static String getCurrentActualPoints(int pointsNeeded, Date checkIn,
	    Date booked) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(checkIn); // set c to checkIn date
	String currentUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, -1); // set back one year
	String previousUseYear = sdf.format(c.getTime());

	c.setTime(booked);
	String bookedDate = sdf.format(c.getTime());

	return String.format("SELECT Trip_ID, AP_ID, Use_Year, Point_Number"
		+ " FROM Actual_Points"
		+ " WHERE (((Use_Year) < '%s' And (Use_Year) > '%s') "
		+ " AND ((Trip_ID) Is Null) AND (Banked_Date Is Null Or Banked_Date > '%s')  )"
		+ " ORDER BY Use_Year, Point_Number"
		+ " LIMIT %s", currentUseYear, previousUseYear, bookedDate,
		pointsNeeded);
    }

    public static String getBorrowActualPoints(int pointsNeeded, Date checkIn) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(checkIn); // set c to checkIn date
	String currentUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, 1); // set back one year
	String nextUseYear = sdf.format(c.getTime());

	return String.format("SELECT Trip_ID, AP_ID, Use_Year, Point_Number"
		+ " FROM Actual_Points"
		+ " WHERE (((Use_Year) < '%s' And (Use_Year) > '%s') "
		+ " AND ((Trip_ID) Is Null) )"
		+ " ORDER BY Use_Year, Point_Number"
		+ " LIMIT %s", nextUseYear, currentUseYear, pointsNeeded);
    }

    public static String allocatePersonalPoint(int personalPointId, int tripId) {
	return String.format("UPDATE Personal_Points"
		+ " SET Trip_ID = '%s'"
		+ " WHERE PP_ID = '%s' AND Trip_ID is null", tripId,
		personalPointId);
    }

    public static String allocateActualPoint(int actualPointId, int tripId) {
	return String.format("UPDATE Actual_Points"
		+ " SET Trip_ID = '%s'"
		+ " WHERE AP_ID = '%s' AND Trip_ID is null", tripId,
		actualPointId);
    }

    public static String allocateNewTrip(Trip newTrip) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	return String.format("INSERT INTO Trips "
		+ "(Booked_Date, CheckIn_Date, CheckOut_Date, BR_ID, Owner_ID, Notes, Points_Needed) "
		+ "Values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", sdf.
		format(newTrip.getBookedDate()), sdf.format(newTrip.
			getCheckInDate()), sdf.format(newTrip.getCheckOutDate()),
		newTrip.getBr().getBrId(), newTrip.getdOwner().getOwnerId(),
		newTrip.getNotes(), newTrip.getPointsNeeded()
	);
    }

    public static String releasePersonalPoints(int tripId) {

	return String.format("UPDATE Personal_Points"
		+ " SET Trip_ID = null"
		+ " WHERE Trip_ID = '%s'", tripId);
    }

    public static String releaseActualPoints(int tripId) {

	return String.format("UPDATE Actual_Points"
		+ " SET Trip_ID = null"
		+ " WHERE Trip_ID = '%s'", tripId);
    }

    public static String findBankablePoints(Date bankDate) {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(bankDate);
	String currentBankDate = sdf.format(c.getTime());
	c.add(Calendar.YEAR, -1); // set back one year
	String previousBankDate = sdf.format(c.getTime());

	return String.format(
		"SELECT * "
		+ " FROM Actual_Points"
		+ " WHERE ((Use_Year) < '%s' And (Use_Year) > '%s')"
		+ " AND ((Trip_ID) Is Null) AND ((Banked_Date) Is Null) "
		+ " ORDER BY Use_Year, Point_Number", currentBankDate,
		previousBankDate);

    }

    public static String markPointsAsBanked(Date bankDate, int countOfPoints) {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(bankDate);
	String currentBankDate = sdf.format(c.getTime());
	c.add(Calendar.YEAR, -1); // set back one year
	String previousBankDate = sdf.format(c.getTime());

	return String.format("UPDATE Actual_Points"
		+ " SET Banked_Date = '%s'"
		+ " Where Use_Year < '%s' and Use_Year > '%s'"
		+ " AND Trip_ID is null and Banked_Date is null"
		+ " ORDER BY AP_ID"
		+ " LIMIT %s", currentBankDate, currentBankDate,
		previousBankDate, countOfPoints);

    }

    /**
     *
     * @param qryDate	Date used for query, this is usually todays date
     * @param ownerId	User Id to query points for.
     * @param pointType A String of the type of points requested. May be
     * [banked, current, borrow]
     * @return
     */
    public static String getOwnerPersonalPointCount(Date qryDate, int ownerId,
	    String pointType) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(qryDate);
	String currentUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, -1); // set back one year
	String previousUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, 2); // set ahead 1 year
	String nextUseYear = sdf.format(c.getTime());

	switch (pointType) {
	    case "banked":
		return String.format("SELECT Count(PP_ID) as Point_Count"
			+ " FROM Personal_Points"
			+ " WHERE ((Use_Year) < '%s' AND ((Owner_ID)='%s') AND ((Trip_ID) Is Null))",
			previousUseYear, ownerId);

	    case "current":
		return String.format("SELECT Count(PP_ID) as Point_Count"
			+ " FROM Personal_Points"
			+ " WHERE ((Use_Year) < '%s' And (Use_Year) > '%s')"
			+ " AND ((Personal_Points.Owner_ID)= '%s') AND ((Trip_ID) Is Null)",
			currentUseYear, previousUseYear, ownerId);

	    case "borrow":
		return String.format("SELECT Count(PP_ID) as Point_Count"
			+ " FROM Personal_Points "
			+ " WHERE (((Use_Year) < '%s' And (Use_Year) > '%s') AND ((Owner_ID) = '%s') AND ((Trip_ID) Is Null) ) ",
			nextUseYear, currentUseYear, ownerId);

	    default:
		return "";
	}
    }

    /**
     *
     * @param qryDate	Date used for query, this is usually todays date
     * @param pointType A String of the type of points requested. May be
     * [banked, current, current-banked, borrow]
     * @return
     */
    public static String getActualPointCount(Date qryDate, String pointType) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(qryDate);
	String currentUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, -1); // set back one year
	String previousUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, -1); // set back two year
	String previousTwoUseYear = sdf.format(c.getTime());
	c.add(Calendar.YEAR, 3); // set ahead 1 year
	String nextUseYear = sdf.format(c.getTime());

	switch (pointType) {
	    case "banked":
		return String.format("SELECT Count(AP_ID) as Point_Count"
			+ " FROM Actual_Points"
			+ " WHERE Use_Year < '%s' AND  Use_Year > '%s'"
			+ " AND Trip_ID Is Null AND Banked_Date IS NOT NULL",
			previousUseYear, previousTwoUseYear);

	    case "current":
		return String.format("SELECT Count(AP_ID) as Point_Count"
			+ " FROM Actual_Points"
			+ " WHERE Use_Year < '%s' AND  Use_Year > '%s'"
			+ " AND Trip_ID Is Null AND Banked_Date IS NULL",
			currentUseYear, previousUseYear);

	    case "current-banked":
		return String.format("SELECT Count(AP_ID) as Point_Count"
			+ " FROM Actual_Points"
			+ " WHERE Use_Year < '%s' AND  Use_Year > '%s'"
			+ " AND Trip_ID Is Null AND Banked_Date IS NOT NULL",
			currentUseYear, previousUseYear);

	    case "borrow":
		return String.format("SELECT Count(AP_ID) as Point_Count"
			+ " FROM Actual_Points"
			+ " WHERE Use_Year < '%s' AND  Use_Year > '%s'"
			+ " AND Trip_ID Is Null AND Banked_Date IS NULL",
			nextUseYear, currentUseYear);

	    default:
		return "";
	}
    }

    public static String getOwnerByEmail(String ownerEmail) {
	return String.format("SELECT Owner.Owner_ID, Owner.Owner"
		+ " FROM Owner "
		+ " JOIN Owner_Email "
		+ " ON Owner.Owner_ID = Owner_Email.Owner_ID "
		+ " WHERE Owner_Email.Owner_Email = '%s'",
		ownerEmail);
    }

    public static String getAccessLevelByEmail(String emailAddress) {
	return String.format(
		"SELECT Access_Level FROM Owner_Email "
		+ "WHERE Owner_Email = '%s'",
		emailAddress
	);
    }

    public static String insertEventLog(String gplusEmail, String eventDesc) {
	return String.format("INSERT INTO dvc.Event_Log (GPlus_ID, Event_Desc)"
		+ "VALUES('%s','%s')", gplusEmail, eventDesc);
    }

}
