package com.softwareynegocios.evolutionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softwareynegocios.evolutionapp.BD.Conexion;
import com.softwareynegocios.evolutionapp.Datos.Usuario;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsuario;
    private EditText editTextPassword;
    private Button buttonIngresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonIngresar = findViewById(R.id.buttonIngresar);

        editTextUsuario.requestFocus();

        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = editTextUsuario.getText().toString();
                String password = editTextPassword.getText().toString();
                if(usuario.trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Usuario vacio", Toast.LENGTH_SHORT).show();
                    editTextUsuario.requestFocus();
                } else if(password.trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Contraseña vacia", Toast.LENGTH_SHORT).show();
                    editTextPassword.requestFocus();
                } else {
                    validarServidor(MainActivity.this, view, usuario, password);
                }
            }
        });


    }

    private void validarServidor(MainActivity xthis, View view, final String usuario, String password) {

        final MainActivity xxthis = xthis;
        try {

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Conexion.SERVER+"/api/usuarioapp/"+usuario+"/"+password;
            JSONObject jsonBody = new JSONObject("{}");


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //System.out.println("JSONresponse"+ response.getString().toString());
                            try{

                                //Usuario
                                Usuario usuario = new Usuario();
                                usuario.setCodusuario(response.getInt("codusuario"));
                                usuario.setNombreusuario(response.getString("nombreusuario"));
                                usuario.setMail(response.getString("mail"));
                                usuario.setPassword(response.getString("password"));

                                Conexion.usuario = usuario;

                                MiLogin miLogin = new MiLogin();
                                miLogin.execute();

                                Intent intent = new Intent(xxthis, PedidosActivity.class);
                                startActivity(intent);
                                String mensaje = ("Bienvenido " + Conexion.usuario.getNombreusuario());
                                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();

                            } catch (Exception e){
                                Toast.makeText(MainActivity.this,"ERROR " + e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this,"Nombre de Usuario/a o contraseña son incorrectas. Intente de nuevo!" ,Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonRequest);
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(MainActivity.this,"Error de conexion" + ex.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    class MiLogin extends AsyncTask<Integer,Integer,Integer> {
        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            progreso = new ProgressDialog(MainActivity.this);

            progreso.setMessage("Validando...");
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