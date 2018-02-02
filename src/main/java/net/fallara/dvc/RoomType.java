package net.fallara.dvc;

import java.io.Serializable;

public class RoomType implements Serializable{

    private int rtId;
    private String roomTypeDesc;
    private int roomSleeps;

    public RoomType() {

    }

    public RoomType(int rtId, String roomTypeDesc, int roomSleeps) {
	super();
	this.rtId = rtId;
	this.roomTypeDesc = roomTypeDesc;
	this.roomSleeps = roomSleeps;
    }

    @Override
    public String toString() {
	return "RoomType{" + "rtId=" + rtId + ", roomTypeDesc=" + roomTypeDesc + ", roomSleeps=" + roomSleeps + '}';
    }

    public int getRtId() {
	return rtId;
    }

    public void setRtId(int rtId) {
	this.rtId = rtId;
    }

    public String getRoomTypeDesc() {
	return roomTypeDesc;
    }

    public void setRoomTypeDesc(String roomTypeDesc) {
	this.roomTypeDesc = roomTypeDesc;
    }

    public int getRoomSleeps() {
	return roomSleeps;
    }

    public void setRoomSleeps(int roomSleeps) {
	this.roomSleeps = roomSleeps;
    }

}
