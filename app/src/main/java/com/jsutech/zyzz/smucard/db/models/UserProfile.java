package com.jsutech.zyzz.smucard.db.models;

import org.litepal.annotation.Column;
import org.litepal.annotation.Encrypt;
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

    @Encrypt(algorithm = AES)
    private String name;
    private String gender;
    private String deposit;
    private String accountStatus;
    private String createdDate;
    private String nationality;
    @Encrypt(algorithm = AES)
    private String creditCard;
    private String role;
    @Encrypt(algorithm = AES)
    private String address;
    private String classification;
    @Column(unique = true, nullable = false)
    private String SUID;
    private String effectiveDate;
    private String department;
    @Encrypt(algorithm = AES)
    private String PID;
    private String effectiveArea;
    private byte[] photo;
    private Date lastLogin;

    public void update(UserProfile newProfile) {
        name = newProfile.getName();
        gender = newProfile.getGender();
        deposit = newProfile.getDeposit();
        accountStatus = newProfile.getAccountStatus();
        createdDate = newProfile.getCreatedDate();
        nationality = newProfile.getNationality();
        creditCard = newProfile.getCreditCard();
        role = newProfile.getRole();
        address = newProfile.getAddress();
        classification = newProfile.getClassification();
        SUID = newProfile.getSUID();
        effectiveDate = newProfile.getEffectiveDate();
        effectiveArea = newProfile.getEffectiveArea();
        department = newProfile.getDepartment();
        PID = newProfile.getPID();
        photo = newProfile.getPhoto();
        lastLogin = newProfile.getLastLogin();
    }
}
