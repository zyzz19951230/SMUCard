package com.jsutech.zyzz.smucard.db.models;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zyzz on 3/22/18.
 *
 */

public class UserProfile extends DataSupport {
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDeposit() {
        return deposit;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getNationality() {
        return nationality;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public String getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getClassification() {
        return classification;
    }

    public String getSUID() {
        return SUID;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getDepartment() {
        return department;
    }

    public String getPID() {
        return PID;
    }

    public String getEffectiveArea() {
        return effectiveArea;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setSUID(String SUID) {
        this.SUID = SUID;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public void setEffectiveArea(String effectiveArea) {
        this.effectiveArea = effectiveArea;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    private String name;
    private String gender;
    private String deposit;
    private String accountStatus;
    private String createdDate;
    private String nationality;
    private String creditCard;
    private String role;
    private String address;
    private String classification;
    private String SUID;
    private String effectiveDate;
    private String department;
    private String PID;
    private String effectiveArea;
    private byte[] photo;
    private Date lastLogin;

}
