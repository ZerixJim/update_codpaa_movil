package com.codpaa.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.codpaa.R;
import com.codpaa.adapter.MensajesRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MensajeModel;
import com.codpaa.util.QuickstartPreferences;
import com.codpaa.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {


    private MensajesRecyclerAdapter adapter;

    private BroadcastReceiver mNewMessageBroadcastReceiver;
    private boolean isReceiverMessageRegistered;
    private LinearLayout linearNoImage;
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    /*private String mParam1;
    private String mParam2;*/

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_message, container, false);

        RecyclerView mRecyclerView = root.findViewById(R.id.recycler_mensajes);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        linearNoImage =  root.findViewById(R.id.no_message);



        //ActionBar actionBar = getSupportActionBar();
        /*ActionBar actionBar = getC

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/



        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), null));
            adapter = new MensajesRecyclerAdapter(getContext());

            mRecyclerView.setAdapter(adapter);
        }



        mNewMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(QuickstartPreferences.NEW_MESSAGE)){
                    adapter.setItems(mensajeModels());
                    adapter.notifyDataSetChanged();

                    if (mensajeModels().size() > 0){
                        linearNoImage.setVisibility(View.GONE);
                    } else {
                        linearNoImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        };





        // Inflate the layout for this fragment
        return  root;
    }


    private List<MensajeModel> mensajeModels(){
        List<MensajeModel> array = new ArrayList<>();
        Cursor cur = new BDopenHelper(getContext()).mensajes();
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


    @Override
    public void onResume() {
        super.onResume();

        //Log.d("MessageList", "onResume");
        Log.d("Mensajes", mensajeModels().size() + "");

        if (mensajeModels().size() > 0){
            linearNoImage.setVisibility(View.GONE);
        } else {
            linearNoImage.setVisibility(View.VISIBLE);
        }

        adapter.setItems(mensajeModels());
        adapter.notifyDataSetChanged();


        if (!isReceiverMessageRegistered){
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mNewMessageBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.NEW_MESSAGE));
            isReceiverMessageRegistered = true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();


        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mNewMessageBroadcastReceiver);
        isReceiverMessageRegistered = false;

    }
}