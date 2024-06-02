package com.example.user_app;

public class GetterSetter {


    public GetterSetter(String firstname, String lastname, String address, String appointmentdate, String appointmenttime, String services, String surl, String phonenumber, String status, String realtime) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.appointmentdate = appointmentdate;
        this.appointmenttime = appointmenttime;
        this.services = services;
        this.surl = surl;
        this.phonenumber = phonenumber;
        this.status = status;
    }

    public String getStatus(){return status;}
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setStatus(String status) {
        this.status = status;
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

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    String firstname, lastname, address, appointmentdate, appointmenttime, services, surl, status;
    String phonenumber;

    public String getRealtime() {
        return realtime;
    }

    public void setRealtime(String realtime) {
        this.realtime = realtime;
    }

    String realtime;

    GetterSetter(){

    }
}
