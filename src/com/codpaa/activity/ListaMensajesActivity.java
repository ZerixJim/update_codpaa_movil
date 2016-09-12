package com.codpaa.activity;
/*
 * Created by Gustavo on 29/03/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.MenuItem;

import com.codpaa.R;
import com.codpaa.adapter.MensajesRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MensajeModel;
import com.codpaa.util.QuickstartPreferences;
import com.codpaa.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ListaMensajesActivity extends AppCompatActivity{

    private MensajesRecyclerAdapter adapter;

    private BroadcastReceiver mNewMessageBroadcastReceiver;
    private boolean isReceiverMessageRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mensajes);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_mensajes);

        LayoutManager mLayoutManager = new LinearLayoutManager(this);



        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
            adapter = new MensajesRecyclerAdapter(this);

            mRecyclerView.setAdapter(adapter);
        }



        mNewMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(QuickstartPreferences.NEW_MESSAGE)){
                    adapter.setItems(mensajeModels());
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();


        //Log.d("MessageList", "onResume");

        adapter.setItems(mensajeModels());
        adapter.notifyDataSetChanged();


        if (!isReceiverMessageRegistered){
            LocalBroadcastManager.getInstance(this).registerReceiver(mNewMessageBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.NEW_MESSAGE));
            isReceiverMessageRegistered = true;
        }




    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNewMessageBroadcastReceiver);
        isReceiverMessageRegistered = false;


        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:

                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }



    }

    private List<MensajeModel> mensajeModels(){
        List<MensajeModel> array = new ArrayList<>();
        Cursor cur = new BDopenHelper(this).mensajes();
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
            final MensajeModel model = new MensajeModel();
            model.setAsunto(cur.getString(cur.getColumnIndex("asunto")));
            model.setMensaje(cur.getString(cur.getColumnIndex("content")));
            model.setDateTime(cur.getString(cur.getColumnIndex("fecha")));
            model.setVisto(cur.getInt(cur.getColumnIndex("estatus")) > 0);
            model.setIdMensaje(cur.getInt(cur.getColumnIndex("id_mensaje")));
            model.setIdServidor(cur.getInt(cur.getColumnIndex("id_servidor")));
            model.setEnviado(cur.getInt(cur.getColumnIndex("enviado")) > 0);

            array.add(model);


        }
        cur.close();
        return array;
    }




}
