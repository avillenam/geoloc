package com.uned.geoloc_3.Model;

public class Vehicle {
    private int id_vehicle;
    private String type;
    private String brand;
    private String model;
    private int passengers;
    private String fuel;
    private Boolean available;

    public Vehicle(String type, String brand, String model, int passengers, String fuel, Boolean available) {
        this.id_vehicle = id_vehicle;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.passengers = passengers;
        this.fuel = fuel;
        this.available = available;
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

    public int getPassengers() {
        return passengers;
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


