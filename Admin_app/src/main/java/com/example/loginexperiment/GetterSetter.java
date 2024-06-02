package com.example.loginexperiment;

public class GetterSetter {

    public GetterSetter(String firstname, String lastname, String Address, String appointmentdate, String appointmenttime, String services, String surl, String phonenumber, String BookingType, String Uid, String Gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = Address;
        this.appointmentdate = appointmentdate;
        this.appointmenttime = appointmenttime;
        this.services = services;
        this.surl = surl;
        this.phonenumber = phonenumber;
        this.BookingType = BookingType;
        this.Uid = Uid;
        this.gender = Gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAppointmentdate() {
        return appointmentdate;
    }

    public void setAppointmentdate(String appointmentdate) {
        this.appointmentdate = appointmentdate;
    }

    public String getAppointmenttime() {
        return appointmenttime;
    }

    public void setAppointmenttime(String appointmenttime) {
        this.appointmenttime = appointmenttime;
    }
    public String getBookingType() {
        return BookingType;
    }

    public String getUid() {
        return Uid;
    }

    public void setBookingType(String BookingType) {
        this.BookingType = BookingType;
    }

    public void setUid(String uid) {
        Uid = uid;
    }


    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    String firstname, lastname, address, appointmentdate, appointmenttime, services, surl;
    String Uid, BookingType;
    String phonenumber;

    public String getCompletename() {
        return completename;
    }

    public void setCompletename(String completename) {
        this.completename = completename;
    }

    public String getFeedback1() {
        return feedback1;
    }

    public void setFeedback1(String feedback1) {
        this.feedback1 = feedback1;
    }

    public String getFeedback2() {
        return feedback2;
    }

    public void setFeedback2(String feedback2) {
        this.feedback2 = feedback2;
    }

    public String getFeedback3() {
        return feedback3;
    }

    public void setFeedback3(String feedback3) {
        this.feedback3 = feedback3;
    }

    public String getFeedback4() {
        return feedback4;
    }

    public void setFeedback4(String feedback4) {
        this.feedback4 = feedback4;
    }

    String completename;
    String feedback1;
    String feedback2;
    String feedback3;
    String feedback4;

    public String getRealtime() {
        return realtime;
    }

    public void setRealtime(String realtime) {
        this.realtime = realtime;
    }

    String realtime;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    String gender;

    String profilepic;

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    String prescription;

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    GetterSetter(){

    }

}
