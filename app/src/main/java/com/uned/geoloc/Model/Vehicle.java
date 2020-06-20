package com.uned.geoloc.Model;

public class Vehicle {
    private int id_vehicle;
    private String matricula;
    private String type;
    private String brand;
    private String model;
    private Boolean available;

    public Vehicle( String type, String matricula, String brand, String model, Boolean available) {
        this.matricula = matricula;
        this.id_vehicle = id_vehicle;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.available = available;
    }

    public String getMatricula() {
        return matricula;
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

    public Boolean getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "" +
                "[" + id_vehicle + "]: " +
                " " + matricula +
                ", " + type +
                ", " + brand +
                ", " + model +
                "";
    }
}


