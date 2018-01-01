package com.mbds.vamp.dashboardapp.model;

public class Car {

    // Description
    String brand;
    String model;
    int registration;
    int nb_places;

    // State
    int charge;
    int temperature_ext;
    boolean locked;
    boolean started;
    Location currentLocation;
    Media honkSound;

    // Users
    User [] allowedUsers;


    public Car() {}


    // Functionalities
    public void startCar() {}
    public void stopCar() {}
    public void openCar() {}
    public void closeCar() {}
    public void openLights() {}
    public void closeLights() {}
    public void setTemperatureClim(int degres) {}
    public void honk() {}


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

    public int getRegistration() {
        return registration;
    }

    public void setRegistration(int registration) {
        this.registration = registration;
    }

    public int getNb_places() {
        return nb_places;
    }

    public void setNb_places(int nb_places) {
        this.nb_places = nb_places;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getTemperature_ext() {
        return temperature_ext;
    }

    public void setTemperature_ext(int temperature_ext) {
        this.temperature_ext = temperature_ext;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isStarted() { return started; }

    public void setStarted(boolean started) { this.started = started; }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    public User[] getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(User[] allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public Media getHonkSound() { return honkSound; }

    public void setHonkSound(Media honkSound) { this.honkSound = honkSound; }
}
