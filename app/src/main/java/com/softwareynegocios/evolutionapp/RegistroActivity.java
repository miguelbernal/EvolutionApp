package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.BD.Conexion;

import org.json.JSONObject;

import java.net.URLEncoder;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    private static EditText et_nombre;
    private static EditText et_apellido;
    private static EditText et_correo;
    private static EditText et_fecha;
    private static EditText et_peso;
    private static EditText et_estatura;
    private static EditText et_contrasena;
    private static EditText et_contrasena2;
    private static Button bcancelar;
    private static Button bregistro;
    private static Intent intent;
    String mensaje;

    Spinner Sexo;
    String[] SexoGenero = {"Masculino", "Femenino"};
    String Aviso;
    String SexoSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Sexo = findViewById(R.id.sp_sexo);


        ArrayAdapter<String> AdapterSabor = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SexoGenero);
        Sexo.setAdapter(AdapterSabor);


        Sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (1) {
                    case 0:
                        Aviso = "";
                        Toast.makeText(getApplicationContext(), Aviso, Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        SexoSeleccionado = SexoGenero[position];
                        break;
                    case 2:
                        SexoSeleccionado = SexoGenero[position];
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_correo = findViewById(R.id.et_correo);
        et_fecha = findViewById(R.id.et_fecha);
        et_peso = findViewById(R.id.et_peso);
        et_estatura = findViewById(R.id.et_Estatura);
        et_contrasena = findViewById(R.id.et_contrasena);
        et_contrasena2 = findViewById(R.id.et_contrasena2);


        bcancelar = findViewById(R.id.bCancelar);
        bcancelar.setOnClickListener(this);

        bregistro = findViewById(R.id.bRegistro);
        bregistro.setOnClickListener(this);

        intent = getIntent();
    }


    @Override
    public void onClick(View view) {

        ///BOTON DE REGISTRO ACCION
        if (view.getId()==bregistro.getId()){
            if( et_contrasena.getText().toString().equals("") || et_correo.getText().toString().equals("")
                    ||et_fecha.getText().toString().equals("") || et_contrasena2.getText().toString().equals("")){

                mensaje= "Los datos deben de ser llenados todos.";
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();

            } else if(et_contrasena.getText().toString().equals(et_contrasena2.getText().toString())){
                guardarUsuario();
            } else {
                mensaje= "La contraseña no coincide.";
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }
        }

        ///BOTON DE CANCELAR REGISTRO ACCION
        else if(view.getId()==bcancelar.getId()){
            mensaje ="Registro Cancelado.";
            intent.putExtra("CONTROL","1");
            intent.putExtra("mensaje",mensaje);
            setResult(RESULT_CANCELED,intent);
            finish();
        }
    }

    private void guardarUsuario() {

        try {

            String nombre = URLEncoder.encode(et_nombre.getText().toString(),"UTF-8");
            String apellido = URLEncoder.encode(et_apellido.getText().toString(), "UTF-8");
            String correo = et_correo.getText().toString();
            String nacimiento = et_fecha.getText().toString();
            String sexo = String.valueOf(Sexo.getSelectedItemPosition());
            String password = et_contrasena.getText().toString();
            String peso = et_peso.getText().toString();
            String altura = et_estatura.getText().toString();

            String url = Conexion.SERVER+"/api/usuarios";
            JSONObject jsonBody = new JSONObject("{nombres_usuario:" + nombre +
                    ",apellidos_usuario:" + apellido +
                    ",sexo_usuario:" + sexo +
                    ",nacimiento_usuario:" + nacimiento +
                    ",peso_usuario:" + peso +
                    ",altura_usuario:" + altura +
                    ",email_usuario:" + correo +
                    ",clave_usuario:" + password + " }");

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try{
                                mensaje = "Registro Completado";
                                intent.putExtra("CONTROL","1");
                                intent.putExtra("Usuario",et_correo.getText().toString());
                                setResult(RESULT_OK,intent);
                                Toast.makeText(RegistroActivity.this,mensaje,Toast.LENGTH_LONG).show();

                                MiRegistro miRegistro = new MiRegistro();
                                miRegistro.execute();

                            } catch (Exception e){
                                Log.e("ERROR", e.toString());
                                e.printStackTrace();
                                Toast.makeText(RegistroActivity.this,"Error" + e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TCC","Datos no guardados 2!");
                    mensaje= "Datos no guardados 2";
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonRequest);

        }catch (Exception ex){
            mensaje= "Error de conexion"+ex.getLocalizedMessage();
            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            Log.e("ERROR",ex.getLocalizedMessage());
        }

    }
    class MiRegistro extends AsyncTask<Integer,Integer,Integer> {
        private ProgressDialog progreso;


        @Override
        protected void onPreExecute() {
            progreso = new ProgressDialog(RegistroActivity.this);

            progreso.setMessage("Registrando...");
            progreso.show();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {

            SystemClock.sleep(2000);

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            finish();
            progreso.dismiss();
        }
    }
}
