package net.fallara.dvc;

import java.io.Serializable;
import java.util.Date;

public class Trip implements Serializable{

    private int tripId;
    private Date bookedDate;
    private Date checkInDate;
    private Date checkOutDate;
    private DvcOwner dOwner;
    private BookableRoom br;
    private String notes;
    private int pointsNeeded;

    public Trip() {
        super();
    }

    public Trip(int tripId, Date bookedDate, Date checkInDate,
            Date checkOutDate, DvcOwner dOwner, BookableRoom br, String notes,
            int pointsNeeded) {
        super();
        this.tripId = tripId;
        this.bookedDate = bookedDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.dOwner = dOwner;
        this.br = br;
        this.notes = notes;
        this.pointsNeeded = pointsNeeded;
    }

    @Override
    public String toString() {
	return "Trip{" 
		+ "tripId=" + tripId 
		+ ", bookedDate=" + bookedDate 
		+ ", checkInDate=" + checkInDate 
		+ ", checkOutDate=" + checkOutDate 
		+ ", dOwner=" + dOwner 
		+ ", br=" + br 
		+ ", notes=" + notes 
		+ ", pointsNeeded=" + pointsNeeded 
		+ '}';
    }

    
    
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public Date getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(Date bookedDate) {
        this.bookedDate = bookedDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public DvcOwner getdOwner() {
        return dOwner;
    }

    public void setdOwner(DvcOwner dOwner) {
        this.dOwner = dOwner;
    }

    public BookableRoom getBr() {
        return br;
    }

    public void setBr(BookableRoom br) {
        this.br = br;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String tripNotes) {
        notes = tripNotes;
    }

    public int getPointsNeeded() {
        return pointsNeeded;
    }

    public void setPointsNeeded(int pointsNeeded) {
        this.pointsNeeded = pointsNeeded;
    }

}
