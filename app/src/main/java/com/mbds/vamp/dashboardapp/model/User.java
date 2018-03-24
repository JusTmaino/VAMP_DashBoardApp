package com.mbds.vamp.dashboardapp.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    String username;
    String password;
    String fullName;
    Date bDay;
    String email;
    String tel;
    Location homeAddress;
    Profile profile;
    Car[] cars;
    Media avatar;


    public User() {}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getbDay() {
        return bDay;
    }

    public void setbDay(Date bDay) {
        this.bDay = bDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Media getAvatar() {
        return avatar;
    }

    public void setAvatar(Media avatar) {
        this.avatar = avatar;
    }

    public Location getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Location homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Car[] getCars() {
        return cars;
    }

    public void setCars(Car[] cars) {
        this.cars = cars;
    }
}
