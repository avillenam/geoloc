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

    TextView tv_emailUSer, tv_idDriver, tv_vehicle_selected;
    Button btn_back, btn_exit;
    Spinner spinner_vehicles;
    private List<Vehicle> vehiclesList = new ArrayList<Vehicle>();
    private List<String> vehiclesString = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loged);

        tv_emailUSer = findViewById(R.id.tv_email);
        tv_idDriver = findViewById(R.id.tv_idDriver);
        tv_vehicle_selected = findViewById(R.id.tv_vehicle2);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        spinner_vehicles = (Spinner) findViewById(R.id.spinner_vehicles);

        // Hace una llamada GET al servidor Node.js solicitando los vehiculos
        getVehicles();

        for(Vehicle vehicle:vehiclesList){
            System.out.println(vehicle);
        }

        //Rellena el spinner después de hacer la consulta GET getVehicles() al servidor
        //ArrayAdapter<String> vehicleArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, vehiclesString);
        //vehicleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner_vehicles.setAdapter(vehicleArrayAdapter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vehiclesString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_vehicles.setAdapter(adapter);



        // Selección vehículo en el Spinner
        spinner_vehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String vehicle = (String) parent.getSelectedItem().toString();
                displayVehicleData(vehicle);
                System.out.println("Id vehiculo seleccionado: " + vehicle);
                Toast.makeText(parent.getContext(), "Id vehiculo seleccionado: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        // cerrar de la Activity
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

                for (Vehicle vehicle : vehiclesList) {
                    String content = "";
                    content += String.valueOf(vehicle.getId_vehicle());
                    content += ", " + String.valueOf(vehicle.getType());
                    content += ", " + String.valueOf(vehicle.getBrand());
                    content += ", " + String.valueOf(vehicle.getModel());
                    content += ", " + String.valueOf(vehicle.getAvailable());
                    vehiclesString.add(content);
                }
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
