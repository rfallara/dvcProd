package net.fallara.dvc;

import java.io.Serializable;

public class DvcOwner implements Serializable {

    private int ownerId;
    private String ownerName;

    public DvcOwner() {
        super();
    }

    @Override
    public String toString() {
	return "DvcOwner{" + "ownerId=" + ownerId + ", ownerName=" + ownerName + '}';
    }
    
    

    public DvcOwner(int ownerId, String ownerName) {
        super();
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

}
