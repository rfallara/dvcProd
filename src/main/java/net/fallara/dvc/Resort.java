package net.fallara.dvc;

import java.io.Serializable;

public class Resort implements Serializable {

    private int resortId;
    private String resortName;

    public Resort() {

    }

    @Override
    public String toString() {
	return "Resort{" + "resortId=" + resortId + ", resortName=" + resortName + '}';
    }
    
    

    public Resort(int resortId, String resortName) {
        super();
        this.resortId = resortId;
        this.resortName = resortName;
    }

    public int getResortId() {
        return resortId;
    }

    public void setResortId(int resortId) {
        this.resortId = resortId;
    }

    public String getResortName() {
        return resortName;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

}
