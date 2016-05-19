package com.project.m13.m13project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main extends Activity {
    static String usuari;
    static String tipo;
    Context h;
    static final String REGISTER_URL = "http://walavib.esy.es/Login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h = this;

    }

    public void BTLogin(View v) {
        final EditText usu = (EditText) findViewById(R.id.usuario);
        final EditText pass = (EditText) findViewById(R.id.pass);

        //inicio una consulta Post con la libreria volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    //Si no hay error en la conexion se activa este metodo
                    @Override
                    public void onResponse(String response) {
                        //comprueba que el php no envia una mensaje de error
                        if (!response.equals("Usuario o Contraseña incorrectos")) {
                            try {
                                //Tranforma lo que envia en un JSONObject
                                JSONObject object = new JSONObject(response);
                                //guarda la variables importantes para saber que usuario es y si es admin o no
                                usuari = object.getString("email");
                                tipo = object.getString("tipo");
                                //inicia menu libro
                                Intent i = new Intent(h, MenuLibros.class);
                                i.putExtra("id", usu.getText().toString());
                                startActivity(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //muestra el error
                            System.out.println(response);
                            Toast.makeText(h, response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    //Si hay un error salta este metodo el cual muestra el error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(h, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            //metodo en el cual realizamos el POST
            @Override
            protected Map<String, String> getParams() {
                //POST hashMap $_POST['key']=valor;
                Map<String, String> params = new HashMap<String, String>();
                params.put("usuario", usu.getText().toString());
                params.put("password", pass.getText().toString());
                return params;
            }
        };
        //inicio la libreria
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void BTVisitante(View v) {
        Intent i = new Intent(h, MenuLibros.class);
        i.putExtra("id", "");
        tipo = "0";
        startActivity(i);
    }

    public void BTRegistro(View v) {

        startActivity(new Intent(this, Registro.class));
    }


}
