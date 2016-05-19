package com.project.m13.m13project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myGalery extends AppCompatActivity {
    private final String REGISTER_URL = "http://walavib.esy.es/Listaus.php";
    private static final String REGISTER_URL2 = "http://walavib.esy.es/borrar.php";

    private static List<Material> material;
    private Context h;
    private     Bundle miId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_galery);
        h=this;
        miId=getIntent().getExtras();
        doInBackground(h);
        material= new ArrayList<>();


    }

    protected String doInBackground(final Context h){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String id = object.getString("id");
                                String id_usu = object.getString("id_usuario");
                                String nombre = object.getString("nombre");
                                String texto = object.getString("texto");
                                String cate = object.getString("cate");
                                String precio = object.getString("precio");
                                String foto = object.getString("foto");
                                String propietario= object.getString("nombreusu");

                                material.add(new Material(Integer.parseInt(id), id_usu, nombre, texto, cate, Double.parseDouble(precio), Base64.decode(foto, Base64.DEFAULT),propietario));
                            }
                            poner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(h, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", Main.usuari);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(h);
        requestQueue.add(stringRequest);
        return null;
    }
    public void poner() {
        final Lists adapter = new Lists(h, material);
        final GridView list = (GridView) findViewById(R.id.mylibros);
        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(h,Selected.class);
                i.putExtra("id",position);
                startActivity(i);
            }
        });*/
        list.setAdapter(adapter);

            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(h);
                    adb.setTitle("Borrar?");
                    adb.setMessage("Esta seguro que quiere borrar el articulo ");
                    final int positionToRemove = position;
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            borrar(position, list);
                        }
                    });
                    adb.show();


                    return true;
                }
            });


    }
    public void borrar(final int position, final GridView list) {
        //inicio una consulta Post con la libreria volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL2,
                new Response.Listener<String>() {
                    //Si no hay error en la conexion se activa este metodo
                    @Override
                    public void onResponse(String response) {
                        //comprueba que el php no envie una mensaje de error
                        if (response.equals("ok")) {
                            Toast.makeText(h, "borrado con exito", Toast.LENGTH_LONG).show();
                            //borrar de la listview
                            material.remove(position);
                            //notifica al adapter que se a actualizado la lista
                            ((Lists) list.getAdapter()).notifyDataSetChanged();
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
                params.put("id", material.get(position).getId() + "");

                return params;
            }

        };
        //inicio la libreria
        RequestQueue requestQueue = Volley.newRequestQueue(h);
        //inserto la libreria
        requestQueue.add(stringRequest);
    }

    public void BTAgregar(View view) {
        Intent i = new Intent(this, addLibro.class);
        i.putExtra("id", miId.getString("id"));
        startActivity(i);

    }
}
