
package com.accenture.mbank.model;

public class BranchListModel {
    private String country;

    private String region;

    private String province;

    private String city;

    private String name;

    private String address;

    private String postalcode;

    private String bankDescription;

    private String phoneNumber;

    private String mailAddress;

    private String faxNumber;

    private String mondayTime;

    private String tuesdayTime;

    private String wednesdayTime;

    private String thursdayTime;

    private String fridayTime;

    private String saturdayTime;

    private String sundayTime;

    private double latitude;

    private double longitude;

    private String approximation;

    private String director;

    private String viceDirector;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getBankDescription() {
        return bankDescription;
    }

    public void setBankDescription(String bankDescription) {
        this.bankDescription = bankDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getMondayTime() {
        return mondayTime;
    }

    public void setMondayTime(String mondayTime) {
        this.mondayTime = mondayTime;
    }

    public String getTuesdayTime() {
        return tuesdayTime;
    }

    public void setTuesdayTime(String tuesdayTime) {
        this.tuesdayTime = tuesdayTime;
    }

    public String getWednesdayTime() {
        return wednesdayTime;
    }

    public void setWednesdayTime(String wednesdayTime) {
        this.wednesdayTime = wednesdayTime;
    }

    public String getThursdayTime() {
        return thursdayTime;
    }

    public void setThursdayTime(String thursdayTime) {
        this.thursdayTime = thursdayTime;
    }

    public String getFridayTime() {
        return fridayTime;
    }

    public void setFridayTime(String fridayTime) {
        this.fridayTime = fridayTime;
    }

    public String getSaturdayTime() {
        return saturdayTime;
    }

    public void setSaturdayTime(String saturdayTime) {
        this.saturdayTime = saturdayTime;
    }

    public String getSundayTime() {
        return sundayTime;
    }

    public void setSundayTime(String sundayTime) {
        this.sundayTime = sundayTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getApproximation() {
        return approximation;
    }

    public void setApproximation(String approximation) {
        this.approximation = approximation;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getViceDirector() {
        return viceDirector;
    }

    public void setViceDirector(String viceDirector) {
        this.viceDirector = viceDirector;
    }

    @Override
    public String toString() {
        return "BranchListModel [country=" + country + ", region=" + region + ", province="
                + province + ", city=" + city + ", name=" + name + ", address=" + address
                + ", postalcode=" + postalcode + ", bankDescription=" + bankDescription
                + ", phoneNumber=" + phoneNumber + ", mailAddress=" + mailAddress + ", faxNumber="
                + faxNumber + ", mondayTime=" + mondayTime + ", tuesdayTime=" + tuesdayTime
                + ", wednesdayTime=" + wednesdayTime + ", thursdayTime=" + thursdayTime
                + ", fridayTime=" + fridayTime + ", saturdayTime=" + saturdayTime + ", sundayTime="
                + sundayTime + ", latitude=" + latitude + ", longitude=" + longitude
                + ", approximation=" + approximation + ", director=" + director + ", viceDirector="
                + viceDirector + "]";
    }

}
