package com.codpaa.activity;
/*
 * Created by Gustavo on 29/03/2016.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;

import com.codpaa.R;
import com.codpaa.adapter.MensajesRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MensajeModel;
import com.codpaa.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ListaMensajesActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private LayoutManager mLayoutManager;
    private MensajesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mensajes);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_mensajes);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));



        adapter = new MensajesRecyclerAdapter(this);

        mRecyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();


        Log.d("MessageList", "onResume");

        adapter.setItems(mensajeModels());
        adapter.notifyDataSetChanged();





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

            array.add(model);


        }
        cur.close();
        return array;
    }




}
