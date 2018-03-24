package com.jsutech.zyzz.smucard.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyzz on 3/23/18.
 *
 */

public class SrunPay {
    public String getSUID() {
        return SUID;
    }

    public void setSUID(String SUID) {
        this.SUID = SUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBal() {
        return bal;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String geteWalletBal() {
        return eWalletBal;
    }

    public void seteWalletBal(String eWalletBal) {
        this.eWalletBal = eWalletBal;
    }

    public String getBillingPackage() {
        return billingPackage;
    }

    public void setBillingPackage(String billingPackage) {
        this.billingPackage = billingPackage;
    }

    public String getPackageBal() {
        return packageBal;
    }

    public void setPackageBal(String packageBal) {
        this.packageBal = packageBal;
    }

    public Map<String, String> getSrunPayForm(){
        return payForm;
    }

    public void addToSrunPayForm(String key, String value){
        payForm.put(key, value);
    }

    public void removeFromSrunPayForm(String key){
        if (payForm.containsKey(key)){
            payForm.remove(key);
        }
    }

    public SrunPay(){
        payForm = new HashMap<>();
    }

    private String SUID;
    private String name;
    private String bal;
    private String status;
    private String cardStatus;
    private String eWalletBal;
    private String billingPackage;
    private String packageBal;
    private Map<String, String> payForm;
}
