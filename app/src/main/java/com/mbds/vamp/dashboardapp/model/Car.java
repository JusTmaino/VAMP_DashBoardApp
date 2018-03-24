package com.mbds.vamp.dashboardapp.model;

import java.util.Arrays;

public class Car {

    // Description
    String registerNumber;
    String brand;
    String model;
    int seatCount;
    String avatar;

    // State
    int charge;
    boolean locked;
    Location currentLocation;

    // Users
    User [] allowedUsers;


    public Car() {}


    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public User[] getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(User[] allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return brand + " " + model;
    }
}
