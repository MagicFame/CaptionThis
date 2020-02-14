package com.magicfame.captionthis;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private int age;
    private boolean calibration;
    private int tailleRelle;
    private int tailleBras;
    private int niveau;
    private int poids;
    private boolean notifications;
    private boolean donnes;

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

    public int getTailleRelle() {
        return tailleRelle;
    }

    public void setTailleRelle(int tailleRelle) {
        this.tailleRelle = tailleRelle;
    }

    public int getTailleBras() {
        return tailleBras;
    }

    public void setTailleBras(int tailleBras) {
        this.tailleBras = tailleBras;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isDonnes() {
        return donnes;
    }

    public void setDonnes(boolean donnes) {
        this.donnes = donnes;
    }

    public void modifyObject (User u){
        this.username = u.username;
        this.age = u.age;
        this.calibration = u.calibration;
        this.tailleBras = u.tailleBras;
        this.tailleRelle = u.tailleRelle;
        this.niveau = u.niveau;
        this.poids = u.poids;
        this.notifications = u.notifications;
        this.donnes = u.donnes;
    }
}
