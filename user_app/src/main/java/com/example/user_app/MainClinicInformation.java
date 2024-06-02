package com.example.user_app;

public class MainClinicInformation {

    public String MainClinicAddress;
    public String MainClinicPhone;
    public String MainDentistFirstName;
    public String MainDentistLastName;
    public String MainEmail;

    public MainClinicInformation(){};

    public String getMainClinicAddress() {
        return MainClinicAddress;
    }

    public String getMainClinicPhone() {
        return MainClinicPhone;
    }

    public String getMainDentistFirstName() {
        return MainDentistFirstName;
    }

    public String getMainDentistLastName() {
        return MainDentistLastName;
    }

    public String getMainEmail() {
        return MainEmail;
    }

    public void setMainClinicAddress(String mainClinicAddress) {
        MainClinicAddress = mainClinicAddress;
    }

    public void setMainClinicPhone(String mainClinicPhone) {
        MainClinicPhone = mainClinicPhone;
    }

    public void setMainDentistFirstName(String mainDentistFirstName) {
        MainDentistFirstName = mainDentistFirstName;
    }

    public void setMainDentistLastName(String mainDentistLastName) {
        MainDentistLastName = mainDentistLastName;
    }

    public void setMainEmail(String mainEmail) {
        MainEmail = mainEmail;
    }

    public MainClinicInformation(String maindentistLastName, String maindentistFirstName, String mainClinicAddress, String mainClinicPhone, String mainDentist, String mainEmail) {
        this.MainClinicAddress = mainClinicAddress;
        this.MainClinicPhone = mainClinicPhone;
        this.MainDentistFirstName = maindentistFirstName;
        this.MainDentistLastName = maindentistLastName;
        this.MainEmail = mainEmail;
    }

}