package com.uned.geoloc_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uned.geoloc_3.Interface.JsonHerokuapp;
import com.uned.geoloc_3.Model.Driver;
import com.uned.geoloc_3.Model.Vehicle;

/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_Registro extends AppCompatActivity {

    private JsonHerokuapp jsonHerokuapp;
    Button btn_registry, btn_back, btn_exit;
    EditText et_email, et_password, et_name, et_surname, et_birthdate, et_mobile_phone;
    Spinner spinner_genre, spinner_vehicle;
    private List<Vehicle> vehiclesList;
    private List<String> vehiclesString;
    String[] generos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btn_registry = (Button) findViewById(R.id.btn_registry);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        spinner_genre = (Spinner) findViewById(R.id.spinner_vehicles);
        et_email = (EditText) findViewById(R.id.txt_email2);
        et_password = (EditText) findViewById(R.id.et_password);
        et_name = (EditText) findViewById(R.id.et_name);
        et_surname = (EditText) findViewById(R.id.et_surname);
        et_birthdate = (EditText) findViewById(R.id.et_birthdate);
        et_mobile_phone = (EditText) findViewById(R.id.et_mobile_phone);
        //spinner_vehicle = (Spinner) findViewById(R.id.spinner_vehicle);

        generos = getResources().getStringArray(R.array.genero);
        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, generos);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_genre.setAdapter(gender_adapter);

        //crea el objeto Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avillena-pfg.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Llamada a la interface
        jsonHerokuapp = retrofit.create(JsonHerokuapp.class);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Registro.this, MainActivity.class);
                startActivity(intent);

            }
        });


        // Hace una llamada GET al servidor Node.js solicitando los vehiculos
        //getVehicles();

        //Conexion a la BD POstgresql
        //sqlThread.start();


        //Rellena el spinner después de hacer la consulta GET getVehicles() al servidor
        ArrayAdapter<String> vehicleArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, vehiclesString);
        vehicleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner_vehicle.setAdapter(vehicleArrayAdapter);


        /*
        spinner_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String vehicle = (String) parent.getSelectedItem();
                displayVehicleData(vehicle);
                System.out.println("Id vehiculo seleccionado: " + vehicle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         */


        btn_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_Registro.this, "Intentando Registrar Driver nuevo: " + et_email.getText(), Toast.LENGTH_SHORT).show();

                String email = et_email.getText().toString();
                String pass = et_password.getText().toString();
                String name = et_name.getText().toString();
                String surname = et_surname.getText().toString();
                String birthdate = et_birthdate.getText().toString();
                int mobile_phone = Integer.valueOf(et_mobile_phone.getText().toString());
                String genre = spinner_genre.getSelectedItem().toString();

                /*
                System.out.println("elementos capturados de la APP");
                System.out.println(email);
                System.out.println(pass);
                System.out.println(name);
                System.out.println(surname);
                System.out.println(birthdate);
                System.out.println(mobile_phone);
                System.out.println(genre);

                 */

                //Realiza una consulta POST para registrar un nuevo Driver
                createDriver(email, pass, name, surname, birthdate, genre, mobile_phone);

                //Realiza la consulta INSERT en la BD
                //makeRequestDB("SELECT * FROM drivers");

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
                vehiclesString = new ArrayList<String>();

                System.out.println("Tipo: ");
                for (Vehicle vehicle : vehiclesList) {
                    String content = "";
                    content += String.valueOf(vehicle.getId_vehicle());
                    vehiclesString.add(content);
                }
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                System.out.println(t.getMessage());
            }

        });
    }


    // Método que realiza la petición POST al servidor para crear un nuevo Driver
    private void createDriver(String email, String password, String name, String surname, String birthdate, String genre, int mobile_number) {

        Driver driver = new Driver(email, password, name, surname, birthdate, genre, mobile_number, true);

        System.out.println("Parametros recibidos de la llamada:");
        System.out.println(email);
        System.out.println(password);
        System.out.println(name);
        //System.out.println(surname);
        //System.out.println(birthdate);
        //System.out.println(mobile_number);
        //System.out.println(genre);

        System.out.println("Objeto creado:");
        System.out.println(driver.getEmail());
        System.out.println(driver.getPassword());
        System.out.println(driver.getName());


        /*
        Map<String, String> fields = new HashMap<>();
        fields.put("email", email);
        fields.put("password", password);
        fields.put("name", name);
        fields.put("surname", surname);
        fields.put("birthdate", birthdate);
        fields.put("genre", genre);
        fields.put("mobile_number", String.valueOf(mobile_number));
        fields.put("available", "true");

         */

        Call<Driver> call = jsonHerokuapp.createDriver(driver);
        //Call<Vehicle> call = jsonHerokuapp.createVehicle("Car", "Tesla", "S", 5, "Electric", true);
        //Call<Driver> call = jsonHerokuapp.createDriver(fields);


        call.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Activity_Registro.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    //System.out.println("Code: " + response.code());
                    return;
                }

                Driver postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "email: " + postResponse.getEmail() + "\n";
                content += "password: " + postResponse.getPassword() + "\n";
                content += "name: " + postResponse.getName() + "\n";
                /*
                content += "surname: " + postResponse.getSurname() + "\n";
                content += "birthdate: " + postResponse.getBirthdate() + "\n";
                content += "genre: " + postResponse.getGenre() + "\n";
                content += "mobile_number: " + postResponse.getMobile_number() + "\n";
                content += "Available: " + postResponse.getAvailable() + "\n\n";

                 */

                Toast.makeText(Activity_Registro.this, "User: " + postResponse.getEmail() + " registered!", Toast.LENGTH_SHORT).show();
                System.out.println(content);
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Toast.makeText(Activity_Registro.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            // come back to the MainActivity
            //TODO: volver al MainActivity
        });
    }


    // Método que realiza la petición POST al servidor para crear un nuevo Vehicle
    private void createVehicle() {
        Vehicle vehicle = new Vehicle("9660CCR", "Car", "Tesla", "S", 5, "Electric", true);


        Map<String, String> fields = new HashMap<>();
        fields.put("type", "Car");
        fields.put("brand", "Seat");
        fields.put("model", "Ibiza");
        fields.put("passengers", "5");
        fields.put("fuel", "Gas");
        fields.put("available", "true");


        //Call<Vehicle> call = jsonHerokuapp.createVehicle(vehicle);
        //Call<Vehicle> call = jsonHerokuapp.createVehicle("Car", "Tesla", "S", 5, "Electric", true);
        Call<Vehicle> call = jsonHerokuapp.createVehicle(fields);


        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Activity_Registro.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    //System.out.println("Code: " + response.code());
                    return;
                }

                Vehicle postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "id_vehicle: " + postResponse.getId_vehicle() + "\n";
                content += "Type: " + postResponse.getType() + "\n";
                content += "Brand: " + postResponse.getBrand() + "\n";
                content += "Model: " + postResponse.getModel() + "\n";
                content += "Passengers: " + postResponse.getPassengers() + "\n";
                content += "Fuel: " + postResponse.getFuel() + "\n";
                content += "Available: " + postResponse.getAvailable() + "\n\n";

                System.out.println(content);
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                Toast.makeText(Activity_Registro.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                //System.out.println(t.getMessage());
            }
        });
    }


    // captura el vehiculo seleccionado
    public void getSelectedVehicle(View v) {
        String vehicle = (String) spinner_genre.getSelectedItem();
        displayVehicleData(vehicle);
    }

    public void displayVehicleData(String vehicle) {
        Toast.makeText(this, vehicle, Toast.LENGTH_LONG).show();
    }


    /*Conexión a la BD Postgres alojada en HEROKU*/
    //Función que se conecta a la BD y realiza una consulta
    /*
    public void makeRequestDB(String req) {
        final String request = req;
        Thread sqlThread = new Thread() {
            public void run() {
                try {
                    Class.forName("org.postgresql.Driver");
                    // "jdbc:postgresql://IP:PUERTO/DB", "USER", "PASSWORD");
                    // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                    Connection conn = DriverManager.getConnection(
                            "jdbc:postgresql://ec2-107-20-173-2.compute-1.amazonaws.com:5432/d2346t6en0926l", "wzkowhhekyvcbh", "dbc37ca58c23fa2edf7ed4af8319e00316de9aaf1defbb8cac1fd86500704f6a");
                    //En el stsql se puede agregar cualquier consulta SQL deseada.
                    //String stsql = "Select version()";
                    String stsql = request;
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(stsql);
                    rs.next();
                    System.out.println("CONSULTA EXITOSA!! --> ");
                    System.out.println("email: " + rs.getString(2));
                    System.out.println(rs.getString(3));

                    conn.close();
                } catch (SQLException se) {
                    System.out.println("oops! No se puede conectar. Error: " + se.toString());
                } catch (ClassNotFoundException e) {
                    System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
                }
            }
        };
        sqlThread.start();
    }

     */
}
