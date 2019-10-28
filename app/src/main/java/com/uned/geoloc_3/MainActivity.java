package com.uned.geoloc_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uned.geoloc_3.Interface.JsonHerokuapp;
import com.uned.geoloc_3.Model.Driver;
import com.uned.geoloc_3.Model.LoginCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btoRegister, btn_exit;
    EditText et_email, et_password;
    private JsonHerokuapp jsonHerokuapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btoRegister = (Button) findViewById(R.id.btn_register);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        et_email = (EditText) findViewById(R.id.tv_email);
        et_password = (EditText) findViewById(R.id.et_password);

        //crea el objeto Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avillena-pfg.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Llamada a la interface
        jsonHerokuapp = retrofit.create(JsonHerokuapp.class);

        btoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String pass = et_password.getText().toString();

                //Make GET request for login user
                loginUser(email, pass);

                //obtieneDrivers();
                //obtieneDriver();
                //loginUser();
            }
        });

        //salir de la aplicación
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //obtieneDrivers();

    }

    private void obtieneDriver() {
        Call<List<Driver>> call = jsonHerokuapp.getDriver(1);

        call.enqueue(new Callback<List<Driver>>() {

            @Override
            public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                System.out.println(response);
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                System.out.println("Todo correcto.");

                List<Driver> listDriver = response.body();
                System.out.println("Driver: " + listDriver.get(0).getName());
                Toast.makeText(MainActivity.this, "Driver: " + listDriver.get(0).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                System.out.println("Algo ha fallado!!");
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtieneDrivers() {
        Call<List<Driver>> call = jsonHerokuapp.getDrivers();

        call.enqueue(new Callback<List<Driver>>() {
            @Override
            public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                List<Driver> drivers = response.body();
                for (Driver driver : drivers) {
                    System.out.println(driver);
                }

            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Function that make Driver login
    private void loginUser(final String email, String pass) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", email);
        parameters.put("_sort", pass);

        final String emailUser = email;

        System.out.println("Parametros recibidos de la llamada:");
        System.out.println(email);
        System.out.println(pass);

        //Call<Driver> call = jsonHerokuapp.createDriver(driver);
        //Call<Vehicle> call = jsonHerokuapp.createVehicle("Car", "Tesla", "S", 5, "Electric", true);
        Call<LoginCode> call = jsonHerokuapp.loginDriver(email, pass);
        call.enqueue(new Callback<LoginCode>() {
            @Override
            public void onResponse(Call<LoginCode> call, Response<LoginCode> response) {
                if (!response.isSuccessful()) {
                    System.out.println("¡Algo ha fallado!");
                    System.out.println(response);

                        Toast.makeText(MainActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        //System.out.println("Code: " + response.code());
                        return;
                }

                LoginCode loginCode = response.body();
                int codigo = loginCode.getCode();
                int id_driver = loginCode.getIdDriver();
                System.out.println(codigo);
                System.out.println(id_driver);
                System.out.println(loginCode);
                //Toast.makeText(MainActivity.this, "Code: " + codigo, Toast.LENGTH_SHORT).show();
                switch (codigo) {
                    case 0:
                        System.out.println("El usuario introducido no existe en el sistema. Regístrese");
                        Toast.makeText(MainActivity.this, "El usuario introducido no existe en el sistema. Regístrese", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        System.out.println("Usuario introducido Correctamente!!");
                        Toast.makeText(MainActivity.this, "Usuario introducido Correctamente!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, UserLogedActivity.class);

                        // Objeto que se va a encargar de enviar la información del usuario a la otra actividad
                        Bundle userBundle = new Bundle();
                        userBundle.putString("email", emailUser);
                        userBundle.putInt("id_driver", id_driver);

                        // Le añadimos al Intent los datos que queremos enviar
                        intent.putExtras(userBundle);

                        startActivity(intent);

                        break;
                    case 2:
                        System.out.println("El password introducido es erróneo. Prueba de nuevo");
                        Toast.makeText(MainActivity.this, "El password introducido es erróneo. Prueba de nuevo", Toast.LENGTH_LONG).show();
                        break;
                }

            }

            @Override
            public void onFailure(Call<LoginCode> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }


}
