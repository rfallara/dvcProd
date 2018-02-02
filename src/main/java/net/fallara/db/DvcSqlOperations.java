package net.fallara.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

//import org.apache.log4j.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.fallara.dvc.BookableRoom;
import net.fallara.dvc.CreateTripResults;
import net.fallara.dvc.DvcOwner;
import net.fallara.dvc.Resort;
import net.fallara.dvc.RoomType;
import net.fallara.dvc.Trip;

public class DvcSqlOperations {
	
	protected static Logger log = Logger.getLogger(DvcSqlOperations.class.getName());


    public static int addRoomType(DBManager dbm, String roomType, int sleeps, String gplusEmail) throws Exception {

	String qry = String.format("INSERT INTO Room_Type (Room_Type, Sleeps) VALUES ('%s', '%s')", roomType, sleeps);
	try {
	    ResultSet rs = dbm.ExecuteNonQueryWithKeys(qry);
	    rs.next();
	    int newRtId = rs.getInt(1);

	    RoomType newRt = DvcSqlOperations.getRoomType(dbm, newRtId);
	    DvcSqlOperations.addEventLogEntry(dbm, gplusEmail, "ADD RoomType: " + newRt);

	    return newRtId;
	} catch (Exception e) {
        log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static boolean deleteRoomType(DBManager dbm, int roomId, String gplusEmail) throws Exception {

	String qry = String.format("DELETE FROM Room_Type WHERE RT_ID='%s'", roomId);
	try {
	    RoomType thisRt = DvcSqlOperations.getRoomType(dbm, roomId);
	    if (dbm.ExecuteNonQuery(qry)) {
		DvcSqlOperations.addEventLogEntry(dbm, gplusEmail, "DELETE RoomType: " + thisRt);
		return true;
	    } else {
		return false;
	    }
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static RoomType getRoomType(DBManager dbm, int roomTypeId) throws Exception {
	String qry = String.format("SELECT * from Room_Type WHERE RT_ID = '%s'", String.valueOf(roomTypeId));

	try {
	    ResultSet rs = dbm.ExecuteResultSet(qry);
	    if (rs.next()) { // Make sure at least one row exists
		RoomType rt = new RoomType();
		rt.setRtId(rs.getInt("RT_ID"));
		rt.setRoomTypeDesc(rs.getString("Room_Type"));
		rt.setRoomSleeps(rs.getInt("Sleeps"));
		if (!rs.next()) { // Make sure there are no more rows
		    rs.close();
		    return rt;
		} else {
		    return null;
		}
	    } else { // If no rows found return null
		return null;
	    }
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    /**
     * Find Actual points that have not been banked AND have not been used by a
     * trip
     *
     * @param dbm DBManger
     * @param bankDate Date to use for the date the points were marked as banked
     * with DVC
     * @return List of AP_IDs
     * @throws Exception
     */
    public static ArrayList<Integer> getBankablePoints(DBManager dbm, Date bankDate) throws Exception {

	String qry = DvcQueries.findBankablePoints(bankDate);

	try {
	    ResultSet rs = dbm.ExecuteResultSet(qry);
	    ArrayList<Integer> x = new ArrayList<>();
	    while (rs.next()) {
		x.add(rs.getInt("AP_ID"));
	    }
	    return x;
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}

    }

    public static int markPointsAsBanked(DBManager dbm, Date bankDate, int countOfPoints) throws Exception {

	String qry = DvcQueries.markPointsAsBanked(bankDate, countOfPoints);

	return dbm.ExecuteUpdate(qry);

    }

    public static ArrayList<RoomType> getAllRoomType(DBManager dbm) throws Exception {
	ArrayList<RoomType> allRT;
	try (ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getRoomTypes())) {
	    allRT = new ArrayList<>();
	    while (rs.next()) {
		allRT.add(new RoomType(Integer.parseInt(rs.getString("RT_ID")), rs.getString("Room_Type"), Integer
			.parseInt(rs.getString("Sleeps"))));
	    }
	}
	return allRT;
    }

    public static int addResort(DBManager dbm, String resortName, String gplusEmail) throws Exception {
	String qry = String.format("INSERT INTO Resort (Resort_Name) Values ('%s')", resortName);
	try {
	    ResultSet rs = dbm.ExecuteNonQueryWithKeys(qry);
	    rs.next();
	    int newResortId = rs.getInt(1);
	    Resort newResort = DvcSqlOperations.getResort(dbm, newResortId);
	    DvcSqlOperations.addEventLogEntry(dbm, gplusEmail, "ADD Resort: " + newResort);

	    return newResortId;
	    //return dbm.ExecuteNonQuery(qry);
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static boolean deleteResort(DBManager dbm, int resortId, String gplusEmail) throws Exception {

	String qry = String.format("DELETE FROM Resort WHERE Resort_ID='%s'", resortId);
	try {
	    Resort newResort = DvcSqlOperations.getResort(dbm, resortId);
	    if (dbm.ExecuteNonQuery(qry)) {
		DvcSqlOperations.addEventLogEntry(dbm, gplusEmail, "DELETE Resort: " + newResort);
		return true;
	    } else {
		return false;
	    }
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static Resort getResort(DBManager dbm, int resortId) throws Exception {
	String qry = String.format("SELECT * from Resort WHERE Resort_ID = '%s'", String.valueOf(resortId));

	try {
	    ResultSet rs = dbm.ExecuteResultSet(qry);
	    if (rs.next()) { // Make sure at least one row exists
		Resort resort = new Resort();
		resort.setResortId(rs.getInt("Resort_ID"));
		resort.setResortName(rs.getString("Resort_Name"));
		if (!rs.next()) { // Make sure there are no more rows
		    rs.close();
		    return resort;
		} else {
		    return null;
		}
	    } else { // If no rows found return null
		return null;
	    }
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static ArrayList<Resort> getAllResort(DBManager dbm) throws Exception {
	ArrayList<Resort> allResort;
	try (ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getResorts())) {
	    allResort = new ArrayList<>();
	    while (rs.next()) {
		allResort.add(new Resort(Integer.parseInt(rs.getString("Resort_ID")), rs.getString("Resort_Name")));
	    }
	    rs.close();
	}
	return allResort;
    }

    public static int addBookableRoom(DBManager dbm, int resortId, int rtId, String gplusEmail) throws Exception {
	String qry = String.format("INSERT INTO Bookable_Room (Resort_ID, RT_ID) Values ('%s', '%s')", resortId, rtId);
	try {
	    ResultSet rs = dbm.ExecuteNonQueryWithKeys(qry);
	    rs.next();
	    int newBrId = rs.getInt(1);
	    BookableRoom newBr = DvcSqlOperations.getBookableRoom(dbm, newBrId);

	    DvcSqlOperations.addEventLogEntry(dbm, gplusEmail, "ADD BookableRoom: " + newBr);

	    return newBrId;
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static boolean deleteBookableRoom(DBManager dbm, int bookableRoomId, String gplusEmail) throws Exception {
	String qry = String.format("DELETE FROM Bookable_Room WHERE BR_ID='%s'", bookableRoomId);
	try {
	    BookableRoom thisBr = DvcSqlOperations.getBookableRoom(dbm, bookableRoomId);

	    if (dbm.ExecuteNonQuery(qry)) {
		DvcSqlOperations.addEventLogEntry(dbm, gplusEmail, "DELETE BookableRoom: " + thisBr);
		return true;
	    } else {
		return false;
	    }
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static BookableRoom getBookableRoom(DBManager dbm, int bookableRoomId) throws Exception {
	String qry = String.format("SELECT * from bookableRooms WHERE BR_ID = '%s'", String.valueOf(bookableRoomId));

	try {
	    ResultSet rs = dbm.ExecuteResultSet(qry);
	    if (rs.next()) { // Make sure at least one row exists
		BookableRoom br = new BookableRoom();
		br.setBrId(rs.getInt("BR_ID"));

		RoomType rt = new RoomType();
		rt.setRtId(rs.getInt("RT_ID"));
		rt.setRoomTypeDesc(rs.getString("Room_Type"));
		rt.setRoomSleeps(rs.getInt("Sleeps"));
		br.setRoomType(rt);

		Resort resort = new Resort();
		resort.setResortId(rs.getInt("Resort_ID"));
		resort.setResortName(rs.getString("Resort_Name"));

		br.setResort(resort);

		if (!rs.next()) { // Make sure there are no more rows
		    rs.close();
		    return br;
		} else {
		    return null;
		}
	    } else { // If no rows found return null
		return null;
	    }
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static ArrayList<BookableRoom> getAllBookableRoom(DBManager dbm) throws Exception {
	ArrayList<BookableRoom> allBr;
	try (ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getBookableRooms())) {
	    allBr = new ArrayList<>();
	    while (rs.next()) {
		allBr.add(new BookableRoom(Integer.parseInt(rs.getString("BR_ID")), new Resort(Integer.parseInt(rs
			.getString("Resort_ID")), rs.getString("Resort_Name")), new RoomType(Integer.parseInt(rs
					.getString("RT_ID")), rs.getString("Room_Type"), Integer.parseInt(rs.getString("Sleeps")))));
	    }
	    rs.close();
	}
	return allBr;
    }

    public static CreateTripResults createTripRecord(DBManager dbm, Trip newTrip, int pointsNeeded, String gplusEmail) throws Exception {

	CreateTripResults myCreateTripResults = new CreateTripResults();

	int personalPointsNeeded = pointsNeeded;
	int countBankedPersonalPoints = 0;
	int countCurrentPersonalPoints = 0;
	int countBorrowPersonalPoints = 0;

	ResultSet rsBankedPersonalPoints;
	ResultSet rsCurrentPersonalPoints;
	ResultSet rsBorrowPersonalPoints;
	ArrayList<Integer> locatedPersonalPointIds = new ArrayList<>();

	rsBankedPersonalPoints = dbm.ExecuteUpdateableResultSet(DvcQueries.getBankedPersonalPoints(
		personalPointsNeeded, newTrip.getdOwner().getOwnerId(), newTrip.getCheckInDate()));

	while (rsBankedPersonalPoints.next()) {
	    countBankedPersonalPoints++;
	    locatedPersonalPointIds.add(rsBankedPersonalPoints.getInt("PP_ID"));
	}
	rsBankedPersonalPoints.close();

	personalPointsNeeded -= countBankedPersonalPoints;

	if (personalPointsNeeded > 0) {
	    rsCurrentPersonalPoints = dbm.ExecuteUpdateableResultSet(DvcQueries.getCurrentPersonalPoints(
		    personalPointsNeeded, newTrip.getdOwner().getOwnerId(), newTrip.getCheckInDate()));

	    while (rsCurrentPersonalPoints.next()) {
		countCurrentPersonalPoints++;
		locatedPersonalPointIds.add(rsCurrentPersonalPoints.getInt("PP_ID"));
	    }
	    rsCurrentPersonalPoints.close();

	    personalPointsNeeded -= countCurrentPersonalPoints;
	}

	if (personalPointsNeeded > 0) {
	    rsBorrowPersonalPoints = dbm.ExecuteUpdateableResultSet(DvcQueries.getBorrowPersonalPoints(
		    personalPointsNeeded, newTrip.getdOwner().getOwnerId(), newTrip.getCheckInDate()));

	    while (rsBorrowPersonalPoints.next()) {
		countBorrowPersonalPoints++;
		locatedPersonalPointIds.add(rsBorrowPersonalPoints.getInt("PP_ID"));
	    }

	    rsBorrowPersonalPoints.close();

	    personalPointsNeeded -= countBorrowPersonalPoints;
	}
	myCreateTripResults.setCountBankedPersonalPoints(countBankedPersonalPoints);
	myCreateTripResults.setCountCurrentPersonalPoints(countCurrentPersonalPoints);
	myCreateTripResults.setCountBorrowPersonalPoints(countBorrowPersonalPoints);

	if (personalPointsNeeded > 0) {
	    myCreateTripResults.setNewTripId(-1);
	    myCreateTripResults.setCreateTripError("Shortage of Personal Points");

	    return myCreateTripResults;
	}

	int actualPointsNeeded = pointsNeeded;

	int countBankedActualPoints = 0;
	int countCurrentActualPoints = 0;
	int countBorrowActualPoints = 0;

	ResultSet rsBankedActualPoints;
	ResultSet rsCurrentActualPoints;
	ResultSet rsBorrowActualPoints;

	ArrayList<Integer> locatedActualPointIds = new ArrayList<>();

	String x = DvcQueries.getBankedActualPoints(actualPointsNeeded, newTrip.getCheckInDate(),
		newTrip.getBookedDate());

	//log.debug(x);

	rsBankedActualPoints = dbm.ExecuteUpdateableResultSet(DvcQueries.getBankedActualPoints(actualPointsNeeded,
		newTrip.getCheckInDate(), newTrip.getBookedDate()));

	while (rsBankedActualPoints.next()) {
	    countBankedActualPoints++;
	    locatedActualPointIds.add(rsBankedActualPoints.getInt("AP_ID"));
	}
	rsBankedActualPoints.close();

	actualPointsNeeded -= countBankedActualPoints;

	if (actualPointsNeeded > 0) {
	    rsCurrentActualPoints = dbm.ExecuteUpdateableResultSet(DvcQueries.getCurrentActualPoints(
		    actualPointsNeeded, newTrip.getCheckInDate(), newTrip.getBookedDate()));

	    while (rsCurrentActualPoints.next()) {
		countCurrentActualPoints++;
		locatedActualPointIds.add(rsCurrentActualPoints.getInt("AP_ID"));
	    }
	    rsCurrentActualPoints.close();

	    actualPointsNeeded -= countCurrentActualPoints;
	}

	if (actualPointsNeeded > 0) {
	    rsBorrowActualPoints = dbm.ExecuteUpdateableResultSet(DvcQueries.getBorrowActualPoints(actualPointsNeeded,
		    newTrip.getCheckInDate()));

	    while (rsBorrowActualPoints.next()) {
		countBorrowActualPoints++;
		locatedActualPointIds.add(rsBorrowActualPoints.getInt("AP_ID"));
	    }
	    rsBorrowActualPoints.close();

	    actualPointsNeeded -= countBorrowActualPoints;
	}

	myCreateTripResults.setCountBankedActualPoints(countBankedActualPoints);
	myCreateTripResults.setCountCurrentActualPoints(countCurrentActualPoints);
	myCreateTripResults.setCountBorrowActualPoints(countBorrowActualPoints);

	if (actualPointsNeeded > 0) {
	    myCreateTripResults.setNewTripId(-2);
	    myCreateTripResults.setCreateTripError("Shortage of Actual Points");

	    return myCreateTripResults;
	}

	String qry = DvcQueries.allocateNewTrip(newTrip);

	ResultSet rsCreateTrip = null;

	//Create new trip in trips table and get back new Trip ID
	int newTripId = -1;
	try {
	    rsCreateTrip = dbm.ExecuteNonQueryWithKeys(qry);
	    rsCreateTrip.next();
	    newTripId = rsCreateTrip.getInt(1);
	    rsCreateTrip.close();
	    myCreateTripResults.setNewTripId(newTripId);
	} catch (Exception e) {
	    if (rsCreateTrip != null) {
		if (!rsCreateTrip.isClosed()) {
		    rsCreateTrip.close();
		}
	    }
		log.warning("Error in SQL query " + e);
	    throw e;
	}

        // At this point we have all the points we need and we created a trip in
	// the trips table...now allocate points
	Statement stmt = dbm.cn.createStatement();
	dbm.cn.setAutoCommit(false);

	for (int i : locatedPersonalPointIds) {
	    stmt.addBatch(DvcQueries.allocatePersonalPoint(i, myCreateTripResults.getNewTripId()));
	}

	for (int i : locatedActualPointIds) {
	    stmt.addBatch(DvcQueries.allocateActualPoint(i, myCreateTripResults.getNewTripId()));
	}

	dbm.validateConnected();
	int[] updateCounts = stmt.executeBatch();
	dbm.cn.setAutoCommit(true);

	for (int i : updateCounts) {
	    if (i != 1) {
                // TODO there was an issue during DB update destroy trip and
		// return error
	    }
	}

	Trip logTrip = DvcSqlOperations.getTrip(dbm, newTripId);
	addEventLogEntry(dbm, gplusEmail, "ADDED Trip: " + logTrip.toString().replace("'", "''"));

	String pointAllocationLog = String.format(
		"TRIP %s points allocations [Personal Banked-%s Current-%s Borrow-%s] "
		+ "[DVC Actual Banked-%s Current-%s Borrow-%s]", logTrip.getTripId(), myCreateTripResults.getCountBankedPersonalPoints(), myCreateTripResults.getCountCurrentPersonalPoints(), myCreateTripResults.getCountBorrowPersonalPoints(), myCreateTripResults.getCountBankedActualPoints(), myCreateTripResults.getCountCurrentActualPoints(), myCreateTripResults.getCountBorrowActualPoints()
	);

	addEventLogEntry(dbm, gplusEmail, pointAllocationLog);

	return myCreateTripResults;

    }

    public static boolean deleteTripRecord(DBManager dbm, int tripId, String gplusEmail) throws Exception {
	Trip thisTrip = getTrip(dbm, tripId);

	try {

	    if (dbm.ExecuteNonQuery(DvcQueries.releasePersonalPoints(tripId))) {
		log.fine("Released Personal Points for trip ID " + tripId);
	    } else {
		log.warning("ERROR during release personal points");
		return false;
	    }

	    if (dbm.ExecuteNonQuery(DvcQueries.releaseActualPoints(tripId))) {
		log.fine("Released Actual Points for trip ID " + tripId);
	    } else {
		log.warning("ERROR during release actual points");
		return false;
	    }

	    String qry = String.format("DELETE from Trips WHERE Trip_ID = '%s'", tripId);

	    if (dbm.ExecuteNonQuery(qry)) {
		addEventLogEntry(dbm, gplusEmail, "DELETE Trip: " + thisTrip.toString().replace("'", "''"));
		log.fine("DELETE TRIP ENTRY " + thisTrip);
	    } else {
		log.warning("ERROR during DELETE TRIP ENTRY " + thisTrip);
		return false;
	    }

	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}

	return true;
    }

    public static DvcOwner getDvcOwner(DBManager dbm, int ownerId) throws Exception {
	String qry = String.format("SELECT * from Owner WHERE Owner_ID = '%s'", String.valueOf(ownerId));

	try {
	    ResultSet rs = dbm.ExecuteResultSet(qry);
	    if (rs.next()) { // Make sure at least one row exists

		DvcOwner dOwner = new DvcOwner();
		dOwner.setOwnerId(rs.getInt("Owner_ID"));
		dOwner.setOwnerName(rs.getString("Owner"));

		if (!rs.next()) { // Make sure there are no more rows
		    rs.close();
		    return dOwner;
		} else {
		    return null;
		}
	    } else { // If no rows found return null
		return null;
	    }
	} catch (Exception e) {
		log.warning("Error in SQL query " + e);
	    throw e;
	}
    }

    public static ArrayList<DvcOwner> getAllDvcOwner(DBManager dbm) throws Exception {
	ArrayList<DvcOwner> allOwner;
	try (ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getOwners())) {
	    allOwner = new ArrayList<>();
	    while (rs.next()) {
		allOwner.add(new DvcOwner(Integer.parseInt(rs.getString("Owner_ID")), rs.getString("Owner")));
	    }
	    rs.close();
	}
	return allOwner;
    }

    public static Trip getTrip(DBManager dbm, int TripId) throws Exception {
	String qry = String.format("SELECT * from tripDetails"
		+ " WHERE Trip_ID = %s", TripId);
	ResultSet rs = dbm.ExecuteResultSet(qry);
	rs.next();

	Trip thisTrip = new Trip(rs.getInt("Trip_ID"), rs.getDate("Booked_Date"), rs.getDate("CheckIn_Date"),
		rs.getDate("CheckOut_Date"), getDvcOwner(dbm, rs.getInt("Owner_ID")),
		getBookableRoom(dbm, rs.getInt("BR_ID")), rs.getString("Notes"), rs.getInt("Points_Needed"));

	return thisTrip;
    }

    public static ArrayList<Trip> getAllTrip(DBManager dbm) throws Exception {
	ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getTripDetails());

	ArrayList<Trip> allTrip = new ArrayList<>();
	while (rs.next()) {
	    allTrip.add(new Trip(rs.getInt("Trip_ID"), rs.getDate("Booked_Date"), rs.getDate("CheckIn_Date"), rs
		    .getDate("CheckOut_Date"), getDvcOwner(dbm, rs.getInt("Owner_ID")), getBookableRoom(dbm,
			    rs.getInt("BR_ID")), rs.getString("Notes"), rs.getInt("Points_Needed")));
	}

	return allTrip;
    }

    public static DvcOwner getOwnerByEmail(DBManager dbm, String ownerEmail) throws SQLException {
	DvcOwner myOwner = new DvcOwner();

	ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getOwnerByEmail(ownerEmail));

	if (rs.next()) {
	    myOwner.setOwnerId(rs.getInt("Owner_ID"));
	    myOwner.setOwnerName(rs.getString("Owner"));
	}
	return myOwner;
    }

    public static int getAccessLevelByEmail(DBManager dbm, String emailAddress) throws SQLException {
	ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getAccessLevelByEmail(emailAddress));

	if (rs.next()) {
	    return rs.getInt(1);
	}
	return 0;

    }

    /**
     * Get the number of available personal points for an owner based on today's
     * date
     *
     * @param dbm	The DBManager to DVC DB
     * @param ownerId	DVC DB Owner Id
     * @param pointType	A String for the type of points to search [banked,
     * current, borrow]
     *
     * @return	Number of points found
     *
     * @throws SQLException
     */
    public static int getOwnerPersonalPointCount(DBManager dbm, int ownerId, String pointType) throws SQLException {
	return getOwnerPersonalPointCount(dbm, ownerId, pointType, new Date());
    }

    /**
     * Get the number of available personal points for an owner
     *
     * @param dbm	The DBManager to DVC DB
     * @param ownerId	DVC DB Owner Id
     * @param pointType	A String for the type of points to search [banked,
     * current, borrow]
     * @param qryDate	The Date to base the search off, usually today's date
     *
     * @return	Number of points found
     *
     * @throws SQLException
     */
    public static int getOwnerPersonalPointCount(DBManager dbm, int ownerId, String pointType, Date qryDate) throws SQLException {

	ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getOwnerPersonalPointCount(qryDate, ownerId, pointType));

	if (rs.next()) {
	    return rs.getInt("Point_Count");
	}

	return 0;
    }

    /**
     * Get the number of available actual points
     *
     * @param dbm	The DBManager to DVC DB
     * @param pointType	A String for the type of points to search [banked,
     * current, current-banked, borrow]
     *
     * @return	Number of points found
     *
     * @throws SQLException
     */
    public static int getActualPointCount(DBManager dbm, String pointType) throws SQLException {

	ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getActualPointCount(new Date(), pointType));

	if (rs.next()) {
	    return rs.getInt("Point_Count");
	}

	return 0;
    }

    /**
     * Get the number of available actual points
     *
     * @param dbm	The DBManager to DVC DB
     * @param pointType	A String for the type of points to search [banked,
     * current, current-banked, borrow]
     * @param qryDate	The Date to base the search off, usually today's date
     *
     * @return	Number of points found
     *
     * @throws SQLException
     */
    public static int getActualPointCount(DBManager dbm, String pointType, Date qryDate) throws SQLException {

	ResultSet rs = dbm.ExecuteResultSet(DvcQueries.getActualPointCount(qryDate, pointType));

	if (rs.next()) {
	    return rs.getInt("Point_Count");
	}

	return 0;
    }

    public static boolean addEventLogEntry(DBManager dbm, String gplusEmail, String eventDesc) throws Exception {

	int x = dbm.ExecuteUpdate(DvcQueries.insertEventLog(gplusEmail,
		eventDesc));

	return x > 0;

    }

}
