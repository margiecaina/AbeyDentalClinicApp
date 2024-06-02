package com.example.user_app;

public class ReadWriteUserDetails {
    public String FirstName, LastName, Phone, Address, DOB, UserAccess, Gender, textdescription;

    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textFirstName, String textLastName, String textPhone, String Address, String DOB, String userAccess, String textGender, String textdescription){
        this.FirstName = textFirstName;
        this.LastName = textLastName;
        this.Phone = textPhone;
        this.Address = Address;
        this.DOB = DOB;
        this.UserAccess = userAccess;
        this.Gender = textGender;
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
