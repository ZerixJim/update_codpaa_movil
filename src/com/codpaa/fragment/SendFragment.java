package com.codpaa.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.activity.ImageSheduler;
import com.codpaa.update.EnviarDatos;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendFragment extends Fragment implements View.OnClickListener {

    EnviarDatos enviar;
    Button btnVisitas, btnFotos;

    public SendFragment() {
        // Required empty public constructor
    }


    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_send, container, false);

        btnVisitas = root.findViewById(R.id.btnVisitasP);

        btnFotos = root.findViewById(R.id.btnFotosNo);


        btnVisitas.setOnClickListener(this);

        btnFotos.setOnClickListener(this);



        enviar = new EnviarDatos(getContext());




        return root;


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btnVisitasP) {
            visitas();
        } else if (id == R.id.btnFotosNo) {
            Intent i = new Intent(getContext(), ImageSheduler.class);
            startActivity(i);
        }

    }

    public void visitas(){

        Toast.makeText(getContext(), "Enviando..",Toast.LENGTH_LONG).show();

        enviar.enviarVisitas();

    }
}