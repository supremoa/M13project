package com.project.m13.m13project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class addLibro extends Activity {
    private String APP_DIRECTORY = "myPictureApp/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal.jpg";
    static final String REGISTER_URL = "http://walavib.esy.es/agregar.php";
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;
    private ImageView imageView;
    Context h;
    Bitmap bit;
    String tipo;
    Bundle otro;
    boolean conectando = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_libro);
        imageView = (ImageView) findViewById(R.id.setPicture);
        h = this;
        otro = getIntent().getExtras();

        Spinner sni = (Spinner) findViewById(R.id.Cursos);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sni.setAdapter(adapter);
        sni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                parent.setSelection(0);
                tipo = parent.getSelectedItem().toString();
            }
        });
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        file.mkdirs();

        String path = Environment.getExternalStorageDirectory() + File.separator
                + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

        File newFile = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent, PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PHOTO_CODE:
                if (resultCode == RESULT_OK) {
                    String dir = Environment.getExternalStorageDirectory() + File.separator
                            + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    try {
                        decodeBitmap(dir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri path = data.getData();
                    try {
                        rotarFoto(null, path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //imageView.setImageURI(path);
                }
                break;
        }

    }

    private void decodeBitmap(String dir) throws IOException {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);
        try {
            rotarFoto(bitmap, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //imageView.setImageBitmap(bitmap);
    }


    private void rotarFoto(Bitmap bitmap, Uri uri) throws IOException {
        Matrix matrix = new Matrix();
        //  matrix.postRotate(90);
        if (bitmap != null) {
            bit = bitmap;
            bit = Bitmap.createScaledBitmap(bit, 350, 350, false);
            Bitmap rotated = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
            imageView.setImageBitmap(rotated);
        }
        if (uri != null) {
            bit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            bit = Bitmap.createScaledBitmap(bit, 350, 350, false);
            Bitmap rotated = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
            imageView.setImageBitmap(rotated);

        }
    }

    public void Foto(View view) {
        final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(addLibro.this);
        builder.setTitle("Elige una opcion :D");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int seleccion) {
                if (options[seleccion] == "Tomar foto") {
                    openCamera();
                } else if (options[seleccion] == "Elegir de galeria") {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                } else if (options[seleccion] == "Cancelar") {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void Cancelar(View view) {
        super.onBackPressed();
    }

    public void save(View view) {
        final TextView nombre = (TextView) findViewById(R.id.editText);
        final TextView descrip = (TextView) findViewById(R.id.descrip);
        final TextView precio = (TextView) findViewById(R.id.editText2);
        if (conectando) {
            if (nombre.getText().toString().equals("") || descrip.getText().toString().equals("") || precio.getText().toString().equals("") || bit == null) {
                Toast.makeText(h, "Campos mal rellenados", Toast.LENGTH_LONG).show();
            } else {
                (new conect(h, nombre.getText().toString(), descrip.getText().toString(), precio.getText().toString())).execute();
                conectando=false;
            }

        }

    }

    private class conect extends AsyncTask<String, StringBuilder, String> {
        Context h;
        String nombre;
        String descrip;
        String precio;

        public conect(Context h) {
            this.h = h;
        }

        public conect(Context h, String nombre, String descrip, String precio) {
            this.h = h;
            this.nombre = nombre;
            this.descrip = descrip;
            this.precio = precio;
        }

        protected String doInBackground(String... params) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
            final String foto = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);



            //inicio una consulta Post con la libreria volley
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        //Si no hay error en la conexion se activa este metodo
                        @Override
                        public void onResponse(String response) {
                            //comprueba que el php no envie un mensaje de error
                            if (response.equals("ok")) {
                                ((Activity) h).onBackPressed();
                            } else {
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
                            conectando=true;
                        }
                    }) {
                //metodo en el cual realizamos el POST
                @Override
                protected Map<String, String> getParams() {
                    //POST hashMap $_POST['key']=valor;
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_usuario", otro.getString("id"));
                    params.put("foto", foto);
                    params.put("nombre", nombre);
                    params.put("texto", descrip);
                    params.put("precio", precio);
                    params.put("tipo", tipo);
                    return params;
                }
            };
            //inicio la libreria
            RequestQueue requestQueue = Volley.newRequestQueue(h);
            requestQueue.add(stringRequest);

            return null;
        }


        protected void onPostExecute(String array) {


        }
    }
}
