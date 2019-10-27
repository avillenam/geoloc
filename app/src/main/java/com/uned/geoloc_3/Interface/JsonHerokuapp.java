package com.uned.geoloc_3.Interface;

import com.uned.geoloc_3.Model.Driver;
import com.uned.geoloc_3.Model.LoginCode;
import com.uned.geoloc_3.Model.LoginUser;
import com.uned.geoloc_3.Model.Vehicle;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface JsonHerokuapp {

    @GET("getVehicles")
    Call<List<Vehicle>> getVehicles();

    @GET("getDrivers")
    Call<List<Driver>> getDrivers();

    @GET("driver/1/")
    Call<List<Driver>> getDriver();

    @GET("driver/{id_driver}/")
    Call<List<Driver>> getDriver(@Path("id_driver") int id_driver);

    @FormUrlEncoded
    @POST("vehicle")
    Call<Vehicle> createVehicle(
            @Field("type") String type,
            @Field("brand") String brand,
            @Field("model") String model,
            @Field("passengers") int passengers,
            @Field("fuel") String fuel,
            @Field("available") Boolean available
    );

    @FormUrlEncoded
    @POST("vehicle")
    Call<Vehicle> createVehicle(@FieldMap Map<String, String> fields);

    // POST request for creating a new Driver
    @FormUrlEncoded
    @POST("driver")
    Call<Driver> createDriver(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("surname") String surname,
            @Field("birthdate") String birthdate,
            @Field("genre") String genre,
            @Field("mobile_number") String mobile_number,
            @Field("available") String available
    );

    // POST request for creating a new Driver
    @FormUrlEncoded
    @POST("driver")
    Call<Driver> createDriver(@FieldMap Map<String, String> fields);

    @POST("driver")
    Call<Driver> createDriver(@Body Driver driver);

    /*
    @FormUrlEncoded
    @POST("loginDriver")
    Call<LoginUser> loginDriver(@FieldMap Map<String, String> fields);

    @POST("loginDriver")
    Call<LoginCode> loginDriver(@Body LoginUser loginUser);

    @FormUrlEncoded
    @POST("loginDriver")
    Call<LoginCode> loginDriver(
            @Field("email") String email,
            @Field("password") String password
    );

     */

    @GET("loginDriver/{email}/{password}")
    Call<List<Vehicle>> getVehicle(@Path("id_vehicle") int id_vehicle);

    @GET("posts")
    Call<LoginCode> loginDriver(@QueryMap Map<String, String> parameters);

    @GET("loginDriver/{email}/{password}")
    Call<LoginCode> loginDriver(@Path("email") String email, @Path("password") String password);
}