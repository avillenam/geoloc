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
import android.widget.TextView;
import android.widget.Toast;

import com.uned.geoloc_3.Interface.JsonHerokuapp;
import com.uned.geoloc_3.Model.Vehicle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLogedActivity extends AppCompatActivity {

    TextView tv_emailUSer, tv_idDriver;
    Button btn_back, btn_exit, btn_new_vehicle;
    Spinner spinner_vehicles;
    private List<Vehicle> vehiclesList = new ArrayList<Vehicle>();
    private List<Vehicle> vehiclesAvailable = new ArrayList<Vehicle>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loged);


        tv_emailUSer = findViewById(R.id.tv_email);
        tv_idDriver = findViewById(R.id.tv_idDriver);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_new_vehicle = (Button) findViewById(R.id.btn_create_vehicle);
        spinner_vehicles = (Spinner) findViewById(R.id.spinner_vehicles);

        // Hace una llamada GET al servidor Node.js solicitando los vehiculos
        getVehicles();

        //Obtenemos el dato enviao por parámetro a través del Bundle
        Bundle userBundle = this.getIntent().getExtras();

        if (userBundle != null) {
            String email = userBundle.getString("email");
            int id_driver = userBundle.getInt("id_driver");
            tv_idDriver.setText("id: " + id_driver);
            tv_emailUSer.setText(email);
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

        // cerrar de la Activity
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avillena-pfg.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Llamada a la interface
        JsonHerokuapp jsonHerokuapp = retrofit.create(JsonHerokuapp.class);
        Call<List<Vehicle>> call = jsonHerokuapp.getVehicles();

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
                vehiclesList = response.body();

                // Filtrar los que estén disponibles (available=true)
                for(Vehicle vehicle : vehiclesList){
                    if(vehicle.getAvailable()==true){
                        vehiclesAvailable.add(vehicle);
                    }
                }

                /*
                for (Vehicle vehicle : vehiclesList) {
                    String content = "";
                    content += String.valueOf(vehicle.getId_vehicle());
                    content += ", " + String.valueOf(vehicle.getType());
                    content += ", " + String.valueOf(vehicle.getBrand());
                    content += ", " + String.valueOf(vehicle.getModel());
                    content += ", " + String.valueOf(vehicle.getAvailable());
                    vehiclesString.add(content);
                }

                 */

                ArrayAdapter<Vehicle> adapter = new ArrayAdapter<Vehicle>(UserLogedActivity.this, android.R.layout.simple_spinner_item, vehiclesAvailable);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_vehicles.setAdapter(adapter);

                spinner_vehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Vehicle vehicle = (Vehicle) parent.getSelectedItem();
                        displayUserData(vehicle);
                        Toast.makeText(UserLogedActivity.this, "Vehículo seleccionado" + vehicle, Toast.LENGTH_SHORT).show();
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

}
