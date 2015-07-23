/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.fallara.dvc;

import java.util.ArrayList;

/**
 *
 * @author rofallar
 */
public class DvcLoggedInUser {

    private String gplusId;
    private String gplusEmail;
    private String gplusHostedDomain;
    private DvcOwner dvcOwner;
    private int accessLevel;
    private ArrayList<DvcOwner> allOwnerList;
    private int bankedPersonalPoints;
    private int currentPersonalPoints;
    private int borrowPersonalPoints;
    private int bankedActualPoints;
    private int currentActualPoints;
    private int currentBankedActualPoints;
    private int borrowActualPoints;

    public DvcLoggedInUser() {
	bankedPersonalPoints = 0;
	currentPersonalPoints = 0;
	borrowPersonalPoints = 0;
    }

    @Override
    public String toString() {
	return "DvcLoggedInUser{" + "gplusId=" + gplusId + ", gplusEmail=" + gplusEmail + ", gplusHostedDomain=" + gplusHostedDomain + ", dvcOwner=" + dvcOwner + ", bankedPersonalPoints=" + bankedPersonalPoints + ", currentPersonalPoints=" + currentPersonalPoints + ", borrowPersonalPoints=" + borrowPersonalPoints + '}';
    }

    /**
     * @return the gplusId
     */
    public String getGplusId() {
	return gplusId;
    }

    /**
     * @param gplusId the gplusId to set
     */
    public void setGplusId(String gplusId) {
	this.gplusId = gplusId;
    }

    /**
     * @return the gplusEmail
     */
    public String getGplusEmail() {
	return gplusEmail;
    }

    /**
     * @param gplusEmail the gplusEmail to set
     */
    public void setGplusEmail(String gplusEmail) {
	this.gplusEmail = gplusEmail;
    }

    /**
     * @return the gplusHostedDomain
     */
    public String getGplusHostedDomain() {
	return gplusHostedDomain;
    }

    /**
     * @param gplusHostedDomain the gplusHostedDomain to set
     */
    public void setGplusHostedDomain(String gplusHostedDomain) {
	this.gplusHostedDomain = gplusHostedDomain;
    }

    /**
     * @return the dvcOwner
     */
    public DvcOwner getDvcOwner() {
	return dvcOwner;
    }

    /**
     * @param dvcOwner the dvcOwner to set
     */
    public void setDvcOwner(DvcOwner dvcOwner) {
	this.dvcOwner = dvcOwner;
    }

    
    public int getAccessLevel() {
	return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
	this.accessLevel = accessLevel;
    }

    public ArrayList<DvcOwner> getAllOwnerList() {
	return allOwnerList;
    }

    public void setAllOwnerList(ArrayList<DvcOwner> allOwnerList) {
	this.allOwnerList = allOwnerList;
    }

    /**
     * @return the bankedPersonalPoints
     */
    public int getBankedPersonalPoints() {
	return bankedPersonalPoints;
    }

    /**
     * @param bankedPersonalPoints the bankedPersonalPoints to set
     */
    public void setBankedPersonalPoints(int bankedPersonalPoints) {
	this.bankedPersonalPoints = bankedPersonalPoints;
    }

    /**
     * @return the currentPersonalPoints
     */
    public int getCurrentPersonalPoints() {
	return currentPersonalPoints;
    }

    /**
     * @param currentPersonalPoints the currentPersonalPoints to set
     */
    public void setCurrentPersonalPoints(int currentPersonalPoints) {
	this.currentPersonalPoints = currentPersonalPoints;
    }

    /**
     * @return the borrowPersonalPoints
     */
    public int getBorrowPersonalPoints() {
	return borrowPersonalPoints;
    }

    /**
     * @param borrowPersonalPoints the borrowPersonalPoints to set
     */
    public void setBorrowPersonalPoints(int borrowPersonalPoints) {
	this.borrowPersonalPoints = borrowPersonalPoints;
    }

    public int getBankedActualPoints() {
        return bankedActualPoints;
    }

    public void setBankedActualPoints(int bankedActualPoints) {
        this.bankedActualPoints = bankedActualPoints;
    }

    public int getCurrentActualPoints() {
        return currentActualPoints;
    }

    public void setCurrentActualPoints(int currentActualPoints) {
        this.currentActualPoints = currentActualPoints;
    }

    public int getCurrentBankedActualPoints() {
        return currentBankedActualPoints;
    }

    public void setCurrentBankedActualPoints(int currentBankedActualPoints) {
        this.currentBankedActualPoints = currentBankedActualPoints;
    }

    public int getBorrowActualPoints() {
        return borrowActualPoints;
    }

    public void setBorrowActualPoints(int borrowActualPoints) {
        this.borrowActualPoints = borrowActualPoints;
    }

}
