package com.uned.geoloc_3.Interface;

import com.uned.geoloc_3.Model.Driver;
import com.uned.geoloc_3.Model.LoginCode;
import com.uned.geoloc_3.Model.Message;
import com.uned.geoloc_3.Model.RegistryCode;
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

    //Registro de un nuevo portador de objetos o conductor
    @POST("register")
//    Call<Driver> createDriver(@Body Driver driver);
    Call<RegistryCode> createDriver(@Body Driver driver);

    // Realiza una petición POST para establecer relación conductor-vehiculo
    @FormUrlEncoded
    @POST("vehicleDriverApp")
    Call<Message> driverVehicleRelation(
            @Field("id_driver") int id_driver,
            @Field("id_vehicle") int id_vehicle
    );

    // Realiza una petición POST para eliminar relación conductor-vehiculo para el id_driver especificado
    @FormUrlEncoded
    @POST("deleteVehicleDriver")
    Call<Message> deleteDriverVehicleRelation(
            @Field("id_driver") int id_driver
    );

    // Realiza una petición POST para establecer la disponibilidad del conductor
    @FormUrlEncoded
    @POST("driverAvailability")
    Call<Message> driverAvailability(
            @Field("id") int id_driver,
            @Field("availability") Boolean availability
    );

    // Realiza una petición POST para establecer la disponibilidad del vehículo
    @FormUrlEncoded
    @POST("vehicleAvailability")
    Call<Message> vehicleAvailability(
            @Field("id") int id_vehicle,
            @Field("availability") Boolean availability
    );

    @GET("loginDriver/{email}/{password}")
    Call<List<Vehicle>> getVehicle(@Path("id_vehicle") int id_vehicle);

    @GET("posts")
    Call<LoginCode> loginDriver(@QueryMap Map<String, String> parameters);

    // método que usa el MainActivity sin Bcrypt
    //@GET("loginDriver/{email}/{password}")
    //Call<LoginCode> loginDriver(
    //@Path("email") String email,
    //@Path("password") String password
    //);

    // método que usa el MainActivity sin Bcrypt
    @FormUrlEncoded
    @POST("loginDriver")
    Call<LoginCode> loginDriver(
        @Field("email") String email,
        @Field("password") String password
    );

    // Llamada GET al servidor que devuelve un objeto Driver a partir del id_driver
    @GET("driver/{id_driver}")
    Call<List<Driver>> driverById(@Path("id_driver") int id_driver);

    // Petición GET para obtener el objeto Vehicle asociado al conductor con id_driver
    @GET("vehicleByIdDriver/{id_driver}")
    Call<List<Vehicle>> getVehicleByIdDriver(@Path("id_driver") int id_driver);

    // Realiza una petición POST para guardar la posicion del vehículo
    @FormUrlEncoded
    @POST("position")
    Call<LoginCode> vehiclePosition(
            @Field("id_vehicle") int id_vehicle,
            @Field("id_driver") int id_driver,
            @Field("coord_x") double coord_x,
            @Field("coord_y") double coord_y,
            @Field("accuracy") double accuracy,
            @Field("address") String address,
            @Field("speed") double speed
    );
    // Establece relación conductor-vehiculo
}
