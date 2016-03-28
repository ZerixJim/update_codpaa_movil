package com.codpaa.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codpaa.R;

public class MessaginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagin_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        String strMessage = bundle.getString("content");

        TextView message = (TextView) findViewById(R.id.message);
        if (strMessage == null || strMessage.isEmpty()){
            message.setText("no se pudo recibir el mensaje");
        }


        message.setText(strMessage);

        Log.d("BundleMessage", strMessage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Acuse de Enterado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
