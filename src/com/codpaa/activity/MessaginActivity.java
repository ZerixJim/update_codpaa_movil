package com.codpaa.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.db.BDopenHelper;
import com.codpaa.util.Utilities;

import java.util.Locale;

public class MessaginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagin_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        String strMessage = bundle.getString("content");
        final int idMensaje = bundle.getInt("idMensaje");
        int idServer = bundle.getInt("idServidor", 0);

        TextView message = (TextView) findViewById(R.id.message);

        if (strMessage == null || strMessage.isEmpty()){
            //message.setText("no se pudo recibir el mensaje");
            if (message != null) {
                message.setMovementMethod(LinkMovementMethod.getInstance());
                message.setText(String.format("%s","no se pudo recibir el mensaje"));
            }
        }


        if (message != null) {
            //message.setText(idMensaje + ".- " + strMessage);
            message.setText(String.format(Locale.getDefault(),"%s (Mensaje id:%d)", strMessage, idServer));
        }

        Log.d("BundleMessage", strMessage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Enviando Acuse de Recibido", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //TODO: implementar el envio de acuse de recibido
                    SQLiteDatabase db = new BDopenHelper(MessaginActivity.this).getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("estatus", 1);
                    db.update(Utilities.TABLE_MENSAJE, values,"id_mensaje="+idMensaje, null);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1200);
                }
            });
        }
    }

}
