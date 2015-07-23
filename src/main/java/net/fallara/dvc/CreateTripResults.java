package net.fallara.dvc;

public class CreateTripResults {

    private int newTripId;
    private int countBankedPersonalPoints;
    private int countCurrentPersonalPoints;
    private int countBorrowPersonalPoints;
    private int countBankedActualPoints;
    private int countCurrentActualPoints;
    private int countBorrowActualPoints;
    private String createTripError;

    public CreateTripResults() {
        countBankedPersonalPoints = -1;
        countCurrentPersonalPoints = -1;
        countBorrowPersonalPoints = -1;
        countBankedActualPoints = -1;
        countCurrentActualPoints = -1;
        countBorrowActualPoints = -1;
    }

    @Override
    public String toString() {
	return "CreateTripResults{" + "newTripId=" + newTripId 
		+ ", countBankedPersonalPoints=" + countBankedPersonalPoints 
		+ ", countCurrentPersonalPoints=" + countCurrentPersonalPoints 
		+ ", countBorrowPersonalPoints=" + countBorrowPersonalPoints 
		+ ", countBankedActualPoints=" + countBankedActualPoints 
		+ ", countCurrentActualPoints=" + countCurrentActualPoints 
		+ ", countBorrowActualPoints=" + countBorrowActualPoints 
		+ ", createTripError=" + createTripError 
		+ '}';
    }
    
    

    public int getNewTripId() {
        return newTripId;
    }

    public void setNewTripId(int newTripId) {
        this.newTripId = newTripId;
    }

    public int getCountBankedPersonalPoints() {
        return countBankedPersonalPoints;
    }

    public void setCountBankedPersonalPoints(int countBankedPersonalPoints) {
        this.countBankedPersonalPoints = countBankedPersonalPoints;
    }

    public int getCountCurrentPersonalPoints() {
        return countCurrentPersonalPoints;
    }

    public void setCountCurrentPersonalPoints(int countCurrentPersonalPoints) {
        this.countCurrentPersonalPoints = countCurrentPersonalPoints;
    }

    public int getCountBorrowPersonalPoints() {
        return countBorrowPersonalPoints;
    }

    public void setCountBorrowPersonalPoints(int countBorrowPersonalPoints) {
        this.countBorrowPersonalPoints = countBorrowPersonalPoints;
    }

    public int getCountBankedActualPoints() {
        return countBankedActualPoints;
    }

    public void setCountBankedActualPoints(int countBankedActualPoints) {
        this.countBankedActualPoints = countBankedActualPoints;
    }

    public int getCountCurrentActualPoints() {
        return countCurrentActualPoints;
    }

    public void setCountCurrentActualPoints(int countCurrentActualPoints) {
        this.countCurrentActualPoints = countCurrentActualPoints;
    }

    public int getCountBorrowActualPoints() {
        return countBorrowActualPoints;
    }

    public void setCountBorrowActualPoints(int countBorrowActualPoints) {
        this.countBorrowActualPoints = countBorrowActualPoints;
    }

    public String getCreateTripError() {
        return createTripError;
    }

    public void setCreateTripError(String createTripError) {
        this.createTripError = createTripError;
    }

}
