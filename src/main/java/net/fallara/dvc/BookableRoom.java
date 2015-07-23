package net.fallara.dvc;

public class BookableRoom {

    private int brId;
    private Resort resort;
    private RoomType roomType;

    public BookableRoom() {

    }

    public BookableRoom(int brId, Resort resort, RoomType roomType) {
        super();
        this.brId = brId;
        this.resort = resort;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
	return "BookableRoom{" 
		+ "brId=" + brId 
		+ ", resort=" + resort 
		+ ", roomType=" + roomType 
		+ '}';
    }
    
    

    public int getBrId() {
        return brId;
    }

    public void setBrId(int brId) {
        this.brId = brId;
    }

    public Resort getResort() {
        return resort;
    }

    public void setResort(Resort resort) {
        this.resort = resort;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

}
