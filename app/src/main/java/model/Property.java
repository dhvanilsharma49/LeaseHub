package model;


import androidx.annotation.NonNull;

import java.io.Serializable;

public class Property implements Serializable {
    private String propertyId;
    private String userId;
    private String propSize;
    private String propDesc;
    private String propAddress;
    private String propAmenities;
    private String propType;
    private String srcImage;
    private Float price;


    public Property() {
    }

    public Property(String propertyId, String userId, String propSize, String propDesc, String propAddress, String propAmenities, String propType, String srcImage, Float price) {
        this.propertyId = propertyId;
        this.userId = userId;
        this.propSize = propSize;
        this.propDesc = propDesc;
        this.propAddress = propAddress;
        this.propAmenities = propAmenities;
        this.propType = propType;
        this.srcImage = srcImage;
        this.price = price;
    }

    public String getPropertyId() { return propertyId; }

    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getPropSize() {
        return propSize;
    }

    public void setPropSize(String propSize) {
        this.propSize = propSize;
    }

    public String getPropDesc() {
        return propDesc;
    }

    public void setPropDesc(String propDesc) {
        this.propDesc = propDesc;
    }

    public String getPropAddress() {
        return propAddress;
    }

    public void setPropAddress(String propAddress) {
        this.propAddress = propAddress;
    }

    public String getPropAmenities() { return propAmenities; }

    public void setPropAmenities(String propAmenities) { this.propAmenities = propAmenities; }

    public String getPropType() {
        return propType;
    }

    public void setPropType(String propType) {
        this.propType = propType;
    }

    public String getSrcImage() { return srcImage; }

    public void setSrcImage(){ this.srcImage = srcImage; }

    public Float getPrice() { return price; }

    public void setPrice(Float price) { this.price = price; }

    @NonNull
    @Override
    public String toString() {
        return "User ID :"+ userId + " Size : "+ propSize + " Description :" + propDesc +" Address :"+ propAddress +" Amanities : "+propAmenities + " Type :" + propType + "Image "+ srcImage + " Price :"+ price;
    }
}
