package model;

import androidx.annotation.NonNull;

public class LeaseRequest {
    private String requestId;
    private String tenantId;
    private String propertyId;
    private String landlordId;

    public LeaseRequest() {
    }

    public LeaseRequest(String requestId, String tenantId, String propertyId, String landlordId) {
        this.requestId = requestId;
        this.tenantId = tenantId;
        this.propertyId = propertyId;
        this.landlordId = landlordId;
    }

    public String getRequestId() { return requestId; }

    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(String landlordId) {
        this.landlordId = landlordId;
    }

    @NonNull
    @Override
    public String toString() {
        return " Tenant :" + tenantId + " Lanlord : "+ landlordId + " Property :" + propertyId + " Request Id :" + requestId;
    }
}
