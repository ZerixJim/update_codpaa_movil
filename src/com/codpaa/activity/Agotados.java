package com.codpaa.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codpaa.R;

public class Agotados extends AppCompatActivity {


    private int idPromotor, idTienda;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.agotados_layout);


        idPromotor = getIntent().getIntExtra("idPromotor", 0);
        idTienda = getIntent().getIntExtra("idTienda", 0);













    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
