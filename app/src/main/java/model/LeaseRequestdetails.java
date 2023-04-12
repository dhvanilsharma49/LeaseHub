package model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class LeaseRequestdetails implements Serializable {
   private String propSize;
   private String propAdd;
   private String srcImage;
   private String tenantName;
   private String contactNo;
   private String emailId;
   private String requestId;
   private String propId;
   private String landlordId;
   private String tenantId;


    public LeaseRequestdetails() {
    }

    public LeaseRequestdetails(String propSize, String propAdd, String srcImage, String tenantName, String contactNo, String emailId, String requestId, String propId, String landlordId, String tenantId) {
        this.propSize = propSize;
        this.propAdd = propAdd;
        this.srcImage = srcImage;
        this.tenantName = tenantName;
        this.contactNo = contactNo;
        this.emailId = emailId;
        this.requestId = requestId;
        this.propId = propId;
        this.landlordId = landlordId;
        this.tenantId = tenantId;
    }

    public String getPropSize() {
        return propSize;
    }

    public void setPropSize(String propSize) {
        this.propSize = propSize;
    }

    public String getPropAdd() {
        return propAdd;
    }

    public void setPropAdd(String propAdd) {
        this.propAdd = propAdd;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(String landlordId) {
        this.landlordId = landlordId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Tenant Name :" + tenantName +"Property Size"+ propSize +"Address :"+ propAdd;
    }
}
