package com.mbds.vamp.dashboardapp.model;

/**
 * Created by Yasmine on 15/03/2018.
 */

public abstract class Setting {

    private String name;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
