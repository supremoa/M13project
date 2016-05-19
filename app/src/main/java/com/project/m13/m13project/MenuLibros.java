package com.project.m13.m13project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MenuLibros extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final String REGISTER_URL = "http://walavib.esy.es/Lista.php";
    static final String REGISTER_URL2 = "http://walavib.esy.es/borrar.php";
    String nombreCurso;
    String nombreAsignatura;
    String tipo;
    Context h;
    static List<Material> material;
    Bundle otro;
    int idRadio;
    String[] texto = new String[]{
            "Todos",
            "Libros",
            "Apuntes",
            "Otros"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        h = this;

        otro = getIntent().getExtras();
        Spinner sni = (Spinner) findViewById(R.id.Cursos);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.layout.spinner_item, texto) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                ((TextView) v).setTextColor(Color.WHITE
                );

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundResource(R.color.walawib);

                ((TextView) v).setTextColor(Color.WHITE
                );

                //  ((TextView) v).setTypeface(fontStyle);
                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sni.setAdapter(adapter);
        sni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo = parent.getSelectedItem().toString();
                if (!tipo.equals("Todos")) {
                    conectar(tipo);
                } else {
                    conectar("t");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                parent.setSelection(0);
                tipo = parent.getSelectedItem().toString();

            }
        });

      /*  final RadioGroup r=(RadioGroup)findViewById(R.id.RadioGrup);

        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id)
            {
                RadioButton checkedRadioButton = (RadioButton)r.findViewById(id);

                boolean isChecked = checkedRadioButton.isChecked();
                System.out.println("aqui");
                if (isChecked)
                {
                    checkedRadioButton.setChecked(false);
                }
                else
                    checkedRadioButton.setChecked(true);
            }});*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (!otro.getString("id").equals("")) {


            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();


            Button btagre = (Button) findViewById(R.id.button);
            btagre.setVisibility(View.GONE);

        }
        conectar("t");
    }

    @Override
    public void onResume() {
        super.onResume();
        // (new conect(h)).execute();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, myGalery.class);
            i.putExtra("id", otro.getString("id"));
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            onBackPressed();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //BOTONES
    public void BTAgregar(View v) {
        Intent i = new Intent(this, addLibro.class);
        i.putExtra("id", otro.getString("id"));
        startActivity(i);
    }


    protected String conectar(final String filtro) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            material = new ArrayList();
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
                                String propietario = object.getString("nombreusu");
                                material.add(new Material(Integer.parseInt(id), id_usu, nombre, texto, cate, Double.parseDouble(precio), Base64.decode(foto, Base64.DEFAULT), propietario));
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
                params.put("filtro", filtro);
                params.put("id", otro.getString("id"));
                System.out.println("id mostrar " + otro.getString("id") + " filtro " + filtro);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(h);
        requestQueue.add(stringRequest);
        return null;
    }

    public void poner() {
        final Lists adapter = new Lists(h, material);
        final GridView list = (GridView) findViewById(R.id.listlibros);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(h, Selected.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
        list.setAdapter(adapter);
        if (Main.tipo.equals("1")) {
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

    }

    //metodo encargado borrar libros admin
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

}
