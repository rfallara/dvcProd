package net.fallara.db;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.fallara.dvc.Trip;

public class DvcQueries {

	public static String getTripDetails() {
		return "SELECT * from trip_details";
	}

	public static String getRoomTypes() {
		return "SELECT * from room_type";
	}

	public static String getResorts() {
		return "SELECT * from resort";
	}

	public static String getBookableRooms() {
		return "SELECT * from bookable_rooms";
	}

	public static String getOwners() {
		return "SELECT * from owner";
	}

	public static String getBankedPersonalPoints(int pointsNeeded, int ownerId,
												 Date checkIn) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(checkIn); // set c to checkIn date
		c.add(Calendar.YEAR, -1); // set back one year
		String previousUseYear = sdf.format(c.getTime());

		return String.format(
				"SELECT trip_id, id, use_year, point_number, owner_id"
						+ " FROM personal_point"
						+ " WHERE ((use_year) < '%s' AND ((owner_id)='%s') AND ((trip_id) Is Null))"
						+ " ORDER BY use_year, point_number"
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
				"SELECT trip_id, id, use_year, point_number, owner_id"
						+ " FROM personal_point"
						+ " WHERE ((use_year) < '%s' And (use_year) > '%s')"
						+ " AND ((personal_point.owner_id)= '%s') AND ((trip_id) Is Null)"
						+ " ORDER BY use_year, point_number"
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
				"SELECT trip_id, id, use_year, point_number, owner_id"
						+ " FROM personal_point "
						+ " WHERE (((use_year) < '%s' And (use_year) > '%s') AND ((owner_id) = '%s') AND ((trip_id) Is Null) ) "
						+ " ORDER BY use_year, point_number "
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

		return String.format("SELECT trip_id, id, use_year, point_number "
						+ " FROM actual_point "
						+ " WHERE (( use_year < '%s' And use_year > '%s') " //Find points from last year ONLY
						+ " AND (trip_id Is Null) "
						+ " AND (banked_date < '%s') ) " //Make sure it is being booked AFTER it has been marked as banked
						+ " ORDER BY use_year, point_number "
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

		return String.format("SELECT trip_id, id, use_year, point_number"
						+ " FROM actual_point"
						+ " WHERE (((use_year) < '%s' And (use_year) > '%s') "
						+ " AND ((trip_id) Is Null) AND (banked_date Is Null Or banked_date > '%s')  )"
						+ " ORDER BY use_year, point_number"
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

		return String.format("SELECT trip_id, id, use_year, point_number"
				+ " FROM actual_point"
				+ " WHERE (((use_year) < '%s' And (use_year) > '%s') "
				+ " AND ((trip_id) Is Null) )"
				+ " ORDER BY use_year, point_number"
				+ " LIMIT %s", nextUseYear, currentUseYear, pointsNeeded);
	}

	public static String allocatePersonalPoint(int personalPointId, int tripId) {
		return String.format("UPDATE personal_point"
						+ " SET trip_id = '%s'"
						+ " WHERE id = '%s' AND trip_id is null", tripId,
				personalPointId);
	}

	public static String allocateActualPoint(int actualPointId, int tripId) {
		return String.format("UPDATE actual_point"
						+ " SET trip_id = '%s'"
						+ " WHERE id = '%s' AND trip_id is null", tripId,
				actualPointId);
	}

	public static String allocateNewTrip(Trip newTrip) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		//Escape apostrophe in notes field
		newTrip.setNotes(newTrip.getNotes().replace("'", "''"));

		return String.format("INSERT INTO trip "
						+ "(booked_date, check_in_date, check_out_date, bookable_room_id, owner_id, notes, points_needed) "
						+ "Values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", sdf.
						format(newTrip.getBookedDate()), sdf.format(newTrip.
						getCheckInDate()), sdf.format(newTrip.getCheckOutDate()),
				newTrip.getBr().getBrId(), newTrip.getdOwner().getOwnerId(),
				newTrip.getNotes(), newTrip.getPointsNeeded()
		);
	}

	public static String releasePersonalPoints(int tripId) {

		return String.format("UPDATE personal_point"
				+ " SET trip_id = null"
				+ " WHERE trip_id = '%s'", tripId);
	}

	public static String releaseActualPoints(int tripId) {

		return String.format("UPDATE actual_point"
				+ " SET trip_id = null"
				+ " WHERE trip_id = '%s'", tripId);
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
						+ " FROM actual_point"
						+ " WHERE ((use_year) < '%s' And (use_year) > '%s')"
						+ " AND ((trip_id) Is Null) AND ((banked_date) Is Null) "
						+ " ORDER BY use_year, point_number", currentBankDate,
				previousBankDate);

	}

	public static String markPointsAsBanked(Date bankDate, int countOfPoints) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(bankDate);
		String currentBankDate = sdf.format(c.getTime());
		c.add(Calendar.YEAR, -1); // set back one year
		String previousBankDate = sdf.format(c.getTime());

		return String.format("UPDATE actual_point"
						+ " SET banked_date = '%s'"
						+ " Where use_year < '%s' and use_year > '%s'"
						+ " AND trip_id is null and banked_date is null"
						+ " ORDER BY id"
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
				return String.format("SELECT Count(id) as Point_Count"
								+ " FROM personal_point"
								+ " WHERE ((use_year) < '%s' AND ((owner_id)='%s') AND ((trip_id) Is Null))",
						previousUseYear, ownerId);

			case "current":
				return String.format("SELECT Count(id) as Point_Count"
								+ " FROM personal_point"
								+ " WHERE ((use_year) < '%s' And (use_year) > '%s')"
								+ " AND ((personal_point.owner_id)= '%s') AND ((trip_id) Is Null)",
						currentUseYear, previousUseYear, ownerId);

			case "borrow":
				return String.format("SELECT Count(id) as Point_Count"
								+ " FROM personal_point "
								+ " WHERE (((use_year) < '%s' And (use_year) > '%s') AND ((owner_id) = '%s') AND ((trip_id) Is Null) ) ",
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
				return String.format("SELECT Count(id) as Point_Count"
								+ " FROM actual_point"
								+ " WHERE use_year < '%s' AND  use_year > '%s'"
								+ " AND trip_id Is Null AND banked_date IS NOT NULL",
						previousUseYear, previousTwoUseYear);

			case "current":
				return String.format("SELECT Count(id) as Point_Count"
								+ " FROM actual_point"
								+ " WHERE use_year < '%s' AND  use_year > '%s'"
								+ " AND trip_id Is Null AND banked_date IS NULL",
						currentUseYear, previousUseYear);

			case "current-banked":
				return String.format("SELECT Count(id) as Point_Count"
								+ " FROM actual_point"
								+ " WHERE use_year < '%s' AND  use_year > '%s'"
								+ " AND trip_id Is Null AND banked_date IS NOT NULL",
						currentUseYear, previousUseYear);

			case "borrow":
				return String.format("SELECT Count(id) as Point_Count"
								+ " FROM actual_point"
								+ " WHERE use_year < '%s' AND  use_year > '%s'"
								+ " AND trip_id Is Null AND banked_date IS NULL",
						nextUseYear, currentUseYear);

			default:
				return "";
		}
	}

	public static String getOwnerByEmail(String ownerEmail) {
		return String.format("SELECT owner.id, owner.name"
						+ " FROM owner "
						+ " JOIN owner_email "
						+ " ON owner.id = owner_email.owner_id "
						+ " WHERE owner_email.owner_email = '%s'",
				ownerEmail);
	}

	public static String getAccessLevelByEmail(String emailAddress) {
		return String.format(
				"SELECT access_level FROM owner_email "
						+ "WHERE owner_email = '%s'",
				emailAddress
		);
	}

	public static String insertEventLog(String gplusEmail, String eventDesc) {
		return String.format("INSERT INTO event_log (google_id, description)"
				+ "VALUES('%s','%s')", gplusEmail, eventDesc);
	}

}
