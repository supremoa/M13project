package com.project.m13.m13project;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class email extends Activity {
Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        bundle=getIntent().getExtras();
        setTitle("Email");
        TextView emailMostrar=(TextView)findViewById(R.id.emailMostrar);
        emailMostrar.setText(bundle.getString("emailMostrar"));
    }
}
