package com.project.m13.m13project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    static final String REGISTER_URL = "http://walavib.esy.es/registrar.php";
    Context h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        h=this;
    }
    public void BTRegistrarse(View v){
        final EditText nombre1 = (EditText)findViewById(R.id.nombre);
        EditText email1 = (EditText)findViewById(R.id.email);
        final EditText contraseña1 = (EditText)findViewById(R.id.contraseña);
        EditText recontraseña1 = (EditText)findViewById(R.id.contraseña1);

        final String nombre = nombre1.getText().toString();
       final String email = email1.getText().toString();
        final String contraseña = contraseña1.getText().toString();
        final String recontraseña = recontraseña1.getText().toString();

        if(!isValidEmailAddress(email)) {
            email1.setError("no es un email permitido");
        }else if(TextUtils.isEmpty(nombre)) {
            nombre1.setError("Rellene este campo");
        }
      else if(TextUtils.isEmpty(email)){
            email1.setError("Rellene este campo");
        }else if(TextUtils.isEmpty(contraseña)){
            contraseña1.setError("Rellene este campo");
        }else if(TextUtils.isEmpty(recontraseña)){
            contraseña1.setError("Rellene este campo");
        }else if(!contraseña.equals(recontraseña)){
            contraseña1.setError("Contraseña no repetida");
            recontraseña1.setError("Contraseña no repetida");
        }else {
            //inicio una consulta Post con la libreria volley
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        //Si no hay error en la conexion se activa este metodo
                        @Override
                        public void onResponse(String response) {
                            //comprueba que el php no envie una mensaje de error
                            if(response.equals("ok")){
                                //la actividad llama al metodo onbackPressed el cual nos vuelve a la anterios panatalla
                                onBackPressed();
                            }else {
                                //muestra el error
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
                    params.put("usuario", email);
                    params.put("name",nombre);
                    params.put("password",contraseña);
                    return params;
                }

            };
            //inicio la libreria
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //inserto la libreria
            requestQueue.add(stringRequest);

        }
    }
    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }
}
