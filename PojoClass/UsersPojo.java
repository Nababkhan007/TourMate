package com.example.nabab.tourmate.PojoClass;

public class UsersPojo {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String occupation;
    private String dateOfBirth;
    private String address;
    private String downLoadImageUrlLink;

    public UsersPojo() {

    }

    public UsersPojo(String firstName, String lastName, String phoneNumber, String occupation, String dateOfBirth, String address, String downLoadImageUrlLink) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.occupation = occupation;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.downLoadImageUrlLink = downLoadImageUrlLink;
    }

    public UsersPojo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public String getDownLoadImageUrlLink() {
        return downLoadImageUrlLink;
    }
}
