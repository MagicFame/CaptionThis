package com.magicfame.captionthis;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private int age;
    private boolean calibration;

    public User() {
    }

    public User(String username, int age, boolean calibration) {
        this.username = username;
        this.age = age;
        this.calibration = calibration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isCalibration() {
        return calibration;
    }

    public void setCalibration(boolean calibration) {
        this.calibration = calibration;
    }
}
