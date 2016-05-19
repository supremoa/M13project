package com.project.m13.m13project;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Selected extends Activity {
    private TextView nomLib, descLib, precLib, propiLib;
    private ImageView image;
    Bundle inten;
    private String emilio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_activity);
        nomLib = (TextView) findViewById(R.id.TSnombr);
        descLib = (TextView) findViewById(R.id.TSDescripcion);
        precLib = (TextView) findViewById(R.id.TSPrecio);
        propiLib = (TextView) findViewById(R.id.TSPropi);
        image = (ImageView) findViewById(R.id.setPicture);

        inten = getIntent().getExtras();
        emilio = MenuLibros.material.get(inten.getInt("id")).getPropietario();
        nomLib.setText(" " + MenuLibros.material.get(inten.getInt("id")).getNombre());
        descLib.setText(" " + MenuLibros.material.get(inten.getInt("id")).getTexto());
        precLib.setText(" " + MenuLibros.material.get(inten.getInt("id")).getPrecio() + "");
        propiLib.setText(" " + MenuLibros.material.get(inten.getInt("id")).getPropietario());
        emilio = MenuLibros.material.get(inten.getInt("id")).getId_usu();
        image.setImageBitmap(BitmapFactory.decodeByteArray(MenuLibros.material.get(inten.getInt("id")).getFoto(), 0, MenuLibros.material.get(inten.getInt("id")).getFoto().length));

    }


    public void Cancelar(View view) {
        super.onBackPressed();
    }

    public void Contactar(View view) {
        Intent intent = new Intent(this, email.class);
        intent.putExtra("emailMostrar",emilio);
        startActivity(intent);
    }
}
