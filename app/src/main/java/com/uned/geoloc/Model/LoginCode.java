package com.uned.geoloc.Model;

public class LoginCode {
    private int code;
    private int id_driver;

    public LoginCode(int code, int id_driver) {
        this.code = code;
        this.id_driver = id_driver;
    }

    public int getCode() {
        return code;
    }

    public int getIdDriver() {
        return id_driver;
    }

    @Override
    public String toString() {
        return "LoginCode{" +
                "code=" + code +
                ", id_driver=" + id_driver +
                '}';
    }
}
