package com.uned.geoloc;

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

import com.uned.geoloc.Interface.JsonHerokuapp;
import com.uned.geoloc.Model.Driver;
import com.uned.geoloc.Model.RegistryCode;
import com.uned.geoloc.Model.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.uned.geoloc.CONSTANTES.BASE_URL;

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

        btn_registry = findViewById(R.id.btn_registry);
        btn_back = findViewById(R.id.btn_back);
        btn_exit = findViewById(R.id.btn_exit);
        spinner_genre = findViewById(R.id.spinner_gender);
        et_email = findViewById(R.id.txt_mail);
        et_password = findViewById(R.id.et_password);
        et_name = findViewById(R.id.et_name);
        et_surname = findViewById(R.id.et_surname);
        et_birthdate = findViewById(R.id.et_birthdate);
        et_mobile_phone = findViewById(R.id.et_mobile_phone);

        generos = getResources().getStringArray(R.array.genero);
        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, generos);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_genre.setAdapter(gender_adapter);

        //crea el objeto Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

        //Rellena el spinner después de hacer la consulta GET getVehicles() al servidor
        ArrayAdapter<String> vehicleArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, vehiclesString);
        vehicleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        btn_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String pass = et_password.getText().toString();
                String name = et_name.getText().toString();
                String surname;
                if(et_surname.getText().toString().isEmpty()){
                    surname = "";
                }else{
                    surname = et_surname.getText().toString();
                }
                String birthdate;
                if(et_birthdate.getText().toString().isEmpty()){
                    birthdate = "01/01/1980";
                }else{
                    birthdate = et_birthdate.getText().toString();
                }
                int mobile_phone;
                if(et_mobile_phone.getText().toString().isEmpty()){
                    mobile_phone = 0;
                }else{
                    mobile_phone = Integer.valueOf(et_mobile_phone.getText().toString());
                }
//                int mobile_phone = Integer.valueOf(et_mobile_phone.getText().toString());
                String genre = spinner_genre.getSelectedItem().toString();

                // Comprobación de los dátos obligatorios
                if (email.isEmpty() || name.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(Activity_Registro.this, "Rellena los campos obligatorios.", Toast.LENGTH_LONG).show();
                } else {
                    //Realiza una consulta POST para registrar un nuevo Driver
                    createDriver(email, pass, name, surname, birthdate, genre, mobile_phone);
                }
            }

        });

        // Cerrar el Activity
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    // Método que realiza la petición POST al servidor para crear un nuevo Driver
    private void createDriver(String email, String password, String name, String surname, String birthdate, String genre, int mobile_number) {

//        Driver driver = new Driver(email, password, name, surname, birthdate, genre, mobile_number, true);
        Driver driver = new Driver(email, password, name, surname, birthdate, genre, mobile_number, true);

        System.out.println("Parametros recibidos de la llamada:");
        System.out.println(email);
        System.out.println(password);
        System.out.println(name);
        System.out.println(surname);
        System.out.println(birthdate);
        System.out.println(genre);
        System.out.println(mobile_number);

        System.out.println("Objeto creado:");
        System.out.println(driver.getEmail());
        System.out.println(driver.getPassword());
        System.out.println(driver.getName());
        System.out.println(driver.getSurname());
        System.out.println(driver.getBirthdate());
        System.out.println(driver.getGenre());
        System.out.println(driver.getMobile_number());
        System.out.println(driver.getAvailable());

        Call<RegistryCode> call = jsonHerokuapp.createDriver(driver);

        call.enqueue(new Callback<RegistryCode>() {
            @Override
            public void onResponse(Call<RegistryCode> call, Response<RegistryCode> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Activity_Registro.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    System.out.println("response.toString:" + response.toString());
                    return;
                }

                RegistryCode postResponse = response.body();
                int codigo = postResponse.getCode();
                int id_driver = postResponse.getId_driver();
                String message = postResponse.getMessage();
                System.out.println(codigo);
                System.out.println(id_driver);
                System.out.println(message);
                // code=0 Si el usuario ya existe en el sistema
                // code=1 Usuario registrado correctamente
                // code=2 Error de formato
                switch (codigo) {
                    case 0: //Si el usuario ya existe en el sistema
                        System.out.println("El usuario introducido ya existe en el sistema. Inicie sesión");
                        Toast.makeText(Activity_Registro.this, "El usuario introducido ya existe en el sistema. Inicie sesión", Toast.LENGTH_LONG).show();
                        break;
                    case 1: //Usuario registrado correctamente
                        System.out.println("Usuario registrado Correctamente!!");
                        Toast.makeText(Activity_Registro.this, "Usuario registrado Correctamente!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Activity_Registro.this, MainActivity.class);

                        // Objeto que se va a encargar de enviar la información del usuario a la otra actividad
                        Bundle userBundle = new Bundle();
                        userBundle.putInt("id_driver", id_driver);

                        // Le añadimos al Intent los datos que queremos enviar
                        intent.putExtras(userBundle);

                        startActivity(intent);

                        break;
                    case 2: //Error de formato
                        System.out.println("Hay algún error al introducir los datos");
                        Toast.makeText(Activity_Registro.this, "Hay algún error al introducir los datos. Prueba de nuevo", Toast.LENGTH_LONG).show();
                        break;
                }

            }

            @Override
            public void onFailure(Call<RegistryCode> call, Throwable t) {
                Toast.makeText(Activity_Registro.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
