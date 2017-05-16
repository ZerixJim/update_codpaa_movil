package com.codpaa.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.widget.CompoundButton;
import android.widget.Switch;

import com.codpaa.R;
import com.codpaa.util.Configuracion;


/*
 * Created by grim on 16/05/2017.
 */

public class SettingsApplication extends AppCompatActivity {

    private Switch switchImpulsor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings_application);


        switchImpulsor = (Switch) findViewById(R.id.switch_impulsador);

        switchImpulsor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Configuracion configuracion = new Configuracion(getApplicationContext());

                if (isChecked){

                    configuracion.setPromotorMode(2);

                }else {
                    configuracion.setPromotorMode(1);
                }


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkMode();
    }


    private void checkMode(){
        Configuracion configuracion = new Configuracion(this);

        if (configuracion.getPromotorMode()  == 2){
            switchImpulsor.setChecked(true);
        } else {
            switchImpulsor.setChecked(false);
        }


    }
}
