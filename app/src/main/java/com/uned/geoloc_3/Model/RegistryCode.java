package com.uned.geoloc_3.Model;

public class RegistryCode {
    private int code;
    private int id_driver;
    private String message;

    public RegistryCode(int code, int id_driver, String message) {
        this.code = code;
        this.id_driver = id_driver;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public int getId_driver() {
        return id_driver;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RegistryCode{" +
                "code=" + code +
                ", id_driver=" + id_driver +
                ", message='" + message + '\'' +
                '}';
    }
}
