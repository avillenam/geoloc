package com.uned.geoloc;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.uned.geoloc.Interface.JsonHerokuapp;
import com.uned.geoloc.Model.RegistryCode;
import com.uned.geoloc.Model.Vehicle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.uned.geoloc.CONSTANTES.BASE_URL;

public class ObjetoNuevoActivity extends AppCompatActivity {
    private JsonHerokuapp jsonHerokuapp;
    Spinner spinner_objects;
    Button btn_new_object, btn_back, btn_exit;
    EditText et_matricula, et_marca, et_modelo;
    private List<Vehicle> vehiclesList;
    private List<String> vehiclesString;
    String[] objects_type;

    int id_current_driver;
    String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_objeto_nuevo);

        btn_new_object = findViewById(R.id.btn_new_object);
        btn_back = findViewById(R.id.btn_back);
        btn_exit = findViewById(R.id.btn_exit);
        spinner_objects = findViewById(R.id.spinner_objects);
        et_matricula = findViewById(R.id.et_matricula);
        et_marca = findViewById(R.id.et_marca);
        et_modelo = findViewById(R.id.et_modelo);


        objects_type = getResources().getStringArray(R.array.vehicle_type);
        System.out.println("objects_type: " + objects_type);
        for (String objeto : objects_type) {
            System.out.println("Objeto: " + objeto);
        }
        ArrayAdapter<String> object_adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, objects_type);
        System.out.println("ArrayAdapter<String> object_adapter");

        object_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println("object_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);\n");

        spinner_objects.setAdapter(object_adapter);
        System.out.println("spinner_objects.setAdapter(object_adapter);\n");

        System.out.println("spinner_objects: " + spinner_objects);

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
                Intent intent = new Intent(ObjetoNuevoActivity.this, Activity_Usuario_Conectado.class);
                startActivity(intent);

            }
        });

        btn_new_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String matricula = et_matricula.getText().toString();
                String marca = et_marca.getText().toString();
                String modelo = et_modelo.getText().toString();

                String object_type = spinner_objects.getSelectedItem().toString();

                // Comprobación de los dátos obligatorios
                if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
                    Toast.makeText(ObjetoNuevoActivity.this, "Rellena los campos obligatorios.", Toast.LENGTH_LONG).show();
                } else {
                    //Realiza una consulta POST para registrar un nuevo Driver
                    createVehicle(object_type, matricula, marca, modelo);
                }

                Toast.makeText(ObjetoNuevoActivity.this, "Crear objeto nuevo", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        //Obtenemos los datos del conductor enviados a través del Bundle
        Bundle userBundle = this.getIntent().getExtras();
        if (userBundle != null) {
            // Recibe del MainActivity el 'email' y el 'id_driver' del conductor conectado
            email = userBundle.getString("email");
            id_current_driver = userBundle.getInt("id_driver");

            System.out.println("parámetros recibidos a través de un Bundle: ");
            System.out.println(id_current_driver);
            System.out.println(email);
        }

        // Cerrar el Activity
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    // Método que realiza la petición POST al servidor para crear un nuevo Driver
    private void createVehicle(String object_type, String matricula, String marca, String modelo) {

        Vehicle vehicle = new Vehicle(object_type, matricula, marca, modelo, true);

        System.out.println("Parametros recibidos de la llamada:");
        System.out.println(object_type);
        System.out.println(matricula);
        System.out.println(marca);
        System.out.println(modelo);

        System.out.println("Objeto creado:");
        System.out.println(vehicle.getType());
        System.out.println(vehicle.getMatricula());
        System.out.println(vehicle.getBrand());
        System.out.println(vehicle.getModel());

        Call<RegistryCode> call = jsonHerokuapp.createNewObject(vehicle);

        call.enqueue(new Callback<RegistryCode>() {
            @Override
            public void onResponse(Call<RegistryCode> call, Response<RegistryCode> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ObjetoNuevoActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                RegistryCode postResponse = response.body();
                int codigo = postResponse.getCode();
                String message = postResponse.getMessage();
                System.out.println(codigo);
                System.out.println(message);
                // code=1 Objeto registrado correctamente
                // code=2 Error de formato
                switch (codigo) {
                    case 1: //Objetol registrado correctamente
                        System.out.println("Objeto registrado Correctamente!!");
                        Toast.makeText(ObjetoNuevoActivity.this, "Objeto registrado Correctamente!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ObjetoNuevoActivity.this, Activity_Usuario_Conectado.class);

                        // Objeto que se va a encargar de enviar la información del usuario a la otra actividad
                        Bundle userBundle = new Bundle();
                        userBundle.putString("email", email);
                        userBundle.putInt("id_driver", id_current_driver);

                        // Le añadimos al Intent los datos que queremos enviar
                        intent.putExtras(userBundle);

                        startActivity(intent);

                        break;
                    case 2: //Error de formato
                        System.out.println("Hay algún error al introducir los datos");
                        Toast.makeText(ObjetoNuevoActivity.this, "Hay algún error al introducir los datos. Prueba de nuevo", Toast.LENGTH_LONG).show();
                        break;
                }

            }

            @Override
            public void onFailure(Call<RegistryCode> call, Throwable t) {
                Toast.makeText(ObjetoNuevoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
