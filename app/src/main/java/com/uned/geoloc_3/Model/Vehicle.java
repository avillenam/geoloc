package com.uned.geoloc_3.Model;

import androidx.annotation.NonNull;

public class Vehicle {
    private int id_vehicle;
    private String type;
    private String brand;
    private String model;
    private int passengeres;
    private String fuel;
    private Boolean available;

    public Vehicle(String car, String tesla, String s, int i, String electric, boolean b) {
    }


    public int getId_vehicle() {
        return id_vehicle;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getPassengeres() {
        return passengeres;
    }

    public String getFuel() {
        return fuel;
    }

    public Boolean getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "id:" + id_vehicle +
                ", " + type +
                ", " + brand +
                ", " + model;
    }
}


