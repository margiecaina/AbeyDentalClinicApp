package com.example.loginexperiment;

public class ReadWriteUserDetails {
    public String FirstName, LastName, Phone, Address, UserAccess, DOB, Gender, textdescription;

    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textFirstName, String textLastName, String textPhone, String Address, String userAccess,String DOB,String Gender, String textdescription){
        this.FirstName = textFirstName;
        this.LastName = textLastName;
        this.Phone = textPhone;
        this.Address = Address;
        this.UserAccess = userAccess;
        this.DOB = DOB;
        this.Gender = Gender;
        this.textdescription = textdescription;
    }

    public String getProfilepiclink() {
        return profilepiclink;
    }

    public void setProfilepiclink(String profilepiclink) {
        this.profilepiclink = profilepiclink;
    }

    String profilepiclink;
}
