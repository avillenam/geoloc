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
import com.uned.geoloc_3.Model.Message;
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
    Button btn_back, btn_exit, btn_new_vehicle, btn_attach_vehicle, btn_deattach_vehicle;
    Spinner spinner_vehicles;
    Switch switch_start_location;
    private List<Vehicle> vehiclesList;
    private List<Vehicle> vehiclesAvailable;
    private List<String> listVehicles;
    private JsonHerokuapp jsonHerokuapp;

    // Conductor actual
    Driver current_driver = null;
    int id_current_driver;
    String email;
    // Vehículo asociado al conductor actual
    Vehicle current_vehicle = null;
    int id_current_vehicle;


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
        btn_attach_vehicle = (Button) findViewById(R.id.btn_attach_vehicle);
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
            email = userBundle.getString("email");
            id_current_driver = userBundle.getInt("id_driver");

            System.out.println("parámetros recibidos a través de un Bundle: ");
            System.out.println(id_current_driver);
            System.out.println(email);

            // Obtener el objeto conductor a partir del id_current_driver
            getDriverById(id_current_driver);

            // Obtener el objeto vehículo asociado al conductor a partir del id_current_driver
            getVehicleByIdDriver(id_current_driver);


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

        btn_attach_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: establecer la relación
                // Primero elimina cualquier relación anterior establecida
                /*
                if (id_current_vehicle != 0) {
                    deleteVehicleDriverRelation(id_current_driver);
                }

                 */

                // Establece la relación conductor-vecículo
                System.out.println("Se va a crear una relacion conductor-vehículo");
                System.out.println(id_current_driver);
                System.out.println(id_current_vehicle);

                if (current_vehicle != null) {
                    Toast.makeText(UserLogedActivity.this, "Desenlazar Vehiculo del Conductor", Toast.LENGTH_SHORT).show();
                    deleteVehicleDriverRelation(id_current_driver);
                    driverVehicleRelation(id_current_driver, id_current_vehicle);
                }else{
                    driverVehicleRelation(id_current_driver, id_current_vehicle);
                }

                // TODO: comprobar que ho exista ya la relacion conductor-vehiculo, porque si no, tumba el servidor. O hacer la comprobación en el servidor
                Toast.makeText(UserLogedActivity.this, "Enlace Vehiculo " + id_current_vehicle + " del Conductor" + id_current_driver, Toast.LENGTH_SHORT).show();

            }
        });

        btn_deattach_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserLogedActivity.this, "Desenlazar Vehiculo del Conductor", Toast.LENGTH_SHORT).show();
                deleteVehicleDriverRelation(id_current_driver);

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

    private void getVehicleByIdDriver(int id_driver) {
        Call<List<Vehicle>> call = jsonHerokuapp.getVehicleByIdDriver(id_driver);

        //se hace un enqueue
        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("¡Algo ha fallado!");
                    Toast.makeText(UserLogedActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (current_vehicle != null) {
                    // Almacena en el objeto vehicle el vehículo asociado al conductor actual
                    current_vehicle = response.body().get(0);

                    System.out.println("Id:" + current_vehicle.getId_vehicle());
                    System.out.println("Tipo:" + current_vehicle.getType());
                    System.out.println("Marca:" + current_vehicle.getBrand());
                    System.out.println("Modelo:" + current_vehicle.getModel());
                    System.out.println("Combustible:" + current_vehicle.getFuel());
                    System.out.println("Pasajeros:" + current_vehicle.getPassengers());

                    // Rellenar los TextView con los datos correspondientes del vehículo asociado al conductor actual
                    txt_idVehicle.setText(String.valueOf(current_vehicle.getId_vehicle()));
                    txt_type.setText(current_vehicle.getType());
                    txt_brand.setText(current_vehicle.getBrand());
                    txt_model.setText(current_vehicle.getModel());
                    txt_fuel.setText(current_vehicle.getFuel());
                    txt_passengers.setText(String.valueOf(current_vehicle.getPassengers()));
                }


            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                Toast.makeText(UserLogedActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

                            // Selecciona vehículo actual y lo almacena el la variable 'current_vehicle'
                            current_vehicle = vehiclesAvailable.get(position - 1);
                            id_current_vehicle = current_vehicle.getId_vehicle();
                            displayUserData(current_vehicle);
                            Toast.makeText(UserLogedActivity.this, "Vehículo seleccionado" + current_vehicle, Toast.LENGTH_SHORT).show();

                            // Rellenar los TextView con los datos correspondientes del vehículo seleccionado
                            txt_idVehicle.setText(String.valueOf(current_vehicle.getId_vehicle()));
                            txt_type.setText(current_vehicle.getType());
                            txt_brand.setText(current_vehicle.getBrand());
                            txt_model.setText(current_vehicle.getModel());
                            txt_fuel.setText(current_vehicle.getFuel());
                            txt_passengers.setText(String.valueOf(current_vehicle.getPassengers()));
                        }


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

    // Establece relación conductor-vehículo
    public void driverVehicleRelation(int id_driver_current, int id_vehicle_current) {
        Call<Message> call = jsonHerokuapp.driverVehicleRelation(id_driver_current, id_vehicle_current);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                // si no es satisfactoria la respuesta, muestra un mensaje con el error
                if (!response.isSuccessful()) {
                    System.out.println("Código: " + response.code());
                }

                Message msg = response.body();
                System.out.println(response);
                System.out.println(msg);
                System.out.println("msg: " + msg.getResponse());
                //Log.i("onSuccess", response.body().toString());
                Toast.makeText(UserLogedActivity.this, msg.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(UserLogedActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Elimina la relación conductor-vehículo para el id_driver especificado
    public void deleteVehicleDriverRelation(int id_driver) {
        Call<Message> call = jsonHerokuapp.deleteDriverVehicleRelation(id_driver);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                // si no es satisfactoria la respuesta, muestra un mensaje con el error
                if (!response.isSuccessful()) {
                    System.out.println("Código: " + response.code());
                }

                current_vehicle = null;
                txt_idVehicle.setText("");
                txt_type.setText("");
                txt_brand.setText("");
                txt_model.setText("");
                txt_fuel.setText("");
                txt_passengers.setText("");

                //Message msg = response.body();
                Message msg = response.body();
                System.out.println("msg: " + msg.getResponse());
                Log.i("onSuccess", response.body().toString());
                Toast.makeText(UserLogedActivity.this, msg.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(UserLogedActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

                // Almacena en el objeto driver el conductor actual obtenido del a petición GET
                current_driver = response.body().get(0);

                // Rellenamos los TextView con los atrbutos del objeto Driver devuelto
                txt_emailUSer.setText(current_driver.getEmail());
                txt_idDriver.setText(String.valueOf(current_driver.getId_driver()));
                txt_name.setText(current_driver.getName());
                txt_surname.setText(current_driver.getSurname());
                txt_mobile.setText(String.valueOf(current_driver.getMobile_number()));
                txt_gender.setText(current_driver.getGenre());
            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                Toast.makeText(UserLogedActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }
}
