package com.uned.geoloc_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.uned.geoloc_3.Interface.JsonHerokuapp;
import com.uned.geoloc_3.Model.Driver;
import com.uned.geoloc_3.Model.LoginCode;
import com.uned.geoloc_3.Model.Vehicle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLogedActivity extends AppCompatActivity {

    TextView txt_emailUSer, txt_idDriver, txt_name, txt_surname, txt_mobile, txt_gender;
    TextView txt_idVehicle, txt_type, txt_brand, txt_model, txt_fuel, txt_passengers;
    Button btn_back, btn_exit, btn_new_vehicle, btn_deattach_vehicle;
    Spinner spinner_vehicles;
    Switch switch_start_location;
    private List<Vehicle> vehiclesList;
    private List<Vehicle> vehiclesAvailable;
    private List<String> listVehicles;
    private JsonHerokuapp jsonHerokuapp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loged);


        txt_emailUSer = (TextView) findViewById(R.id.txt_email2);
        txt_idDriver = (TextView) findViewById(R.id.txt_idDriver2);
        txt_name = (TextView) findViewById(R.id.txt_name2);
        txt_surname = (TextView) findViewById(R.id.txt_surname2);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile2);
        txt_gender = (TextView) findViewById(R.id.txt_gender2);
        txt_idVehicle = (TextView) findViewById(R.id.txt_idVehicle2);
        txt_type = (TextView) findViewById(R.id.txt_type2);
        txt_brand = (TextView) findViewById(R.id.txt_brand2);
        txt_model = (TextView) findViewById(R.id.txt_model2);
        txt_fuel = (TextView) findViewById(R.id.txt_fuel2);
        txt_passengers = (TextView) findViewById(R.id.txt_passengers2);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_new_vehicle = (Button) findViewById(R.id.btn_create_vehicle);
        btn_deattach_vehicle = (Button) findViewById(R.id.btn_deattach_vehicle);
        spinner_vehicles = (Spinner) findViewById(R.id.spinner_vehicles);
        switch_start_location = (Switch) findViewById(R.id.switch1);

        //crea el objeto Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avillena-pfg.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Llamada a la interface
        jsonHerokuapp = retrofit.create(JsonHerokuapp.class);


        // Hace una llamada GET al servidor Node.js solicitando los vehiculos
        getVehicles();

        //Obtenemos el dato enviao por parámetro a través del Bundle
        Bundle userBundle = this.getIntent().getExtras();

        if (userBundle != null) {
            String email = userBundle.getString("email");
            int id_driver = userBundle.getInt("id_driver");

            getDriverById(id_driver);

        }


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // TODO: crear nuevo vehículo
        btn_new_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserLogedActivity.this, "Crear nuevo Vehiculo", Toast.LENGTH_SHORT).show();
            }
        });

        btn_deattach_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserLogedActivity.this, "Desenlazar Vehiculo del Conductor", Toast.LENGTH_SHORT).show();
            }
        });

        // cerrar de la Activity
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switch_start_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switch_start_location.isChecked()) {
                    Toast.makeText(UserLogedActivity.this, "Comienza la localización", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserLogedActivity.this, "Para la localización", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void displayUserData(Vehicle vehicle) {
        String brand = vehicle.getBrand();
        String model = vehicle.getModel();

        String userData = "Brand: " + brand + "\nModel: " + model;

        Toast.makeText(this, userData, Toast.LENGTH_LONG).show();
    }


    private void getVehicles() {
        //crea el objeto Retrofit
        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avillena-pfg.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         */

        //Llamada a la interface
        //JsonHerokuapp jsonHerokuapp = retrofit.create(JsonHerokuapp.class);
        //jsonHerokuapp = retrofit.create(JsonHerokuapp.class);
        Call<List<Vehicle>> call = jsonHerokuapp.getVehicles();
        System.out.println("Llamada a la interface dentro del getVehicles");

        //se hace un enqueue
        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {

                // si no es satisfactoria la respuesta, muestra un mensaje con el error
                if (!response.isSuccessful()) {
                    System.out.println("Código: " + response.code());
                }

                Log.i("onSuccess", response.body().toString());

                //almacenamos la respuesta en una lista. Ya estan pareseados al haber usado un converter (GSON)
                vehiclesList = new ArrayList<Vehicle>();
                vehiclesList = response.body();

                // Filtrar los que estén disponibles (available=true)
                vehiclesAvailable = new ArrayList<Vehicle>();
                listVehicles = new ArrayList<String>();
                listVehicles.add("Select vehicle");
                for (Vehicle vehicle : vehiclesList) {
                    if (vehicle.getAvailable() == true) {
                        vehiclesAvailable.add(vehicle);
                        listVehicles.add(vehicle.toString());
                    }
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserLogedActivity.this, android.R.layout.simple_spinner_item, listVehicles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_vehicles.setAdapter(adapter);

                spinner_vehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Vehicle vehicle = (Vehicle) parent.getSelectedItem();
                        if (position != 0) {
                            Vehicle vehicle = vehiclesAvailable.get(position - 1);
                            displayUserData(vehicle);
                            Toast.makeText(UserLogedActivity.this, "Vehículo seleccionado" + vehicle, Toast.LENGTH_SHORT).show();

                            // Rellenar los TextView con los datos correspondientes del vehículo seleccionado
                            txt_idVehicle.setText(String.valueOf(vehicle.getId_vehicle()));
                            txt_type.setText(vehicle.getType());
                            txt_brand.setText(vehicle.getBrand());
                            txt_model.setText(vehicle.getModel());
                            txt_fuel.setText(vehicle.getFuel());
                            txt_passengers.setText(String.valueOf(vehicle.getPassengers()));
                        }

                        // TODO: establecer la relación


                        // TODO: una vez seleccionado el vehículo, habría que poner tanto para el Vehicle como para el Driver, available = false;
                        // para ello, habría qe añadir sendos métodos POST en la aplicación del Node.js para que actualice esto.
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                System.out.println(t.getMessage());
            }

        });
    }

    // muestra Toast con la selección del vehículo
    public void displayVehicleData(String vehicle) {
        Toast.makeText(this, vehicle, Toast.LENGTH_LONG).show();
    }

    public void getDriverById(int id_driver) {
        System.out.println("dentro del getDriverById");
        Call<List<Driver>> call = jsonHerokuapp.driverById(id_driver);
        System.out.println("Objeto call instanciado");
        call.enqueue(new Callback<List<Driver>>() {
            @Override
            public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("¡Algo ha fallado!");
                    Toast.makeText(UserLogedActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Driver driver = response.body().get(0);

                // Rellenamos los TextView con los atrbutos del objeto Driver devuelto
                txt_emailUSer.setText(driver.getEmail());
                txt_idDriver.setText(String.valueOf(driver.getId_driver()));
                txt_name.setText(driver.getName());
                txt_surname.setText(driver.getSurname());
                txt_mobile.setText(String.valueOf(driver.getMobile_number()));
                txt_gender.setText(driver.getGenre());
            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                Toast.makeText(UserLogedActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }
}
