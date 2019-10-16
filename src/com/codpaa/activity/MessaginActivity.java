package com.codpaa.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure.Mensaje;
import com.codpaa.response.MensajeHttpResponse;
import com.codpaa.util.Utilities;
import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.RequestParams;



import java.text.SimpleDateFormat;
import java.util.Calendar;
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

                    SQLiteDatabase db = new BDopenHelper(MessaginActivity.this).getWritableDatabase();


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    Calendar calendar = Calendar.getInstance();

                    String fechaLeido = simpleDateFormat.format(calendar.getTime());

                    ContentValues values = new ContentValues();
                    values.put("estatus", 1);
                    values.put(Mensaje.FECHA_LECTURA, fechaLeido);
                    db.update(Utilities.TABLE_MENSAJE, values,"id_mensaje="+idMensaje, null);

                    String sql = String.format(Locale.getDefault(),
                            "select %s, %s, %s from %s where %s=%d and %s=0",
                            Mensaje.ID_SERVIDOR, Mensaje.ID_PROMOTOR, Mensaje.FECHA_LECTURA,
                            Mensaje.TABLE_NAME,Mensaje.ID_MENSAJE, idMensaje, Mensaje.ENVIADO);

                    Cursor cursor = db.rawQuery(sql, null);

                    //Log.d("SQL" , sql + " Count " + cursor.getCount());

                    if (cursor.getCount() > 0) {


                        cursor.moveToFirst();
                        AsyncHttpClient client = new AsyncHttpClient();

                        RequestParams rp = new RequestParams();
                        rp.put("idPromotor", cursor.getInt(cursor.getColumnIndex(Mensaje.ID_PROMOTOR)));
                        rp.put("idMensaje", cursor.getInt(cursor.getColumnIndex(Mensaje.ID_SERVIDOR)));
                        rp.put("fecha", cursor.getString(cursor.getColumnIndex(Mensaje.FECHA_LECTURA)));



                        client.post(Utilities.WEB_SERVICE_CODPAA + "sendmensaje.php",
                                rp, new MensajeHttpResponse(MessaginActivity.this, idMensaje));

                    }


                    cursor.close();
                    db.close();

                }
            });
        }
    }

}
