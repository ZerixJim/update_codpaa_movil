package com.codpaa.activity;
/*
 * Created by Gustavo on 20/10/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.codpaa.R;
import com.codpaa.activity.impulsor.TiendaNueva;
import com.codpaa.adapter.ViewPagerAdapter;
import com.codpaa.fragment.FragmentDomingo;
import com.codpaa.fragment.FragmentJueves;
import com.codpaa.fragment.FragmentLunes;
import com.codpaa.fragment.FragmentMartes;
import com.codpaa.fragment.FragmentMiercoles;
import com.codpaa.fragment.FragmentNoAsignado;
import com.codpaa.fragment.FragmentSabado;
import com.codpaa.fragment.FragmentViernes;
import com.codpaa.util.Configuracion;

import java.util.Calendar;

public class CalendarioRuta extends AppCompatActivity{

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    private int idPromotor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_ruta);

        Intent i = getIntent();
        idPromotor = i.getIntExtra("idCelular", 0);

        //Log.d("idPromo Calen", " "+ idPromotor);

        toolbar = (Toolbar) findViewById(R.id.toolbar_calendario);

        //toolbar.setLogo(R.drawable.ic_launcher);
        if (toolbar != null){
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);

                Configuracion config = new Configuracion(this);
                String modo = "";

                if(config.getPromotorMode() == 1){

                    modo = "Promotor";

                }else if(config.getPromotorMode() == 2){

                    modo = "Impulsor";

                }


                actionBar.setSubtitle(modo);

            }


        }

        viewPager = (ViewPager) findViewById(R.id.viewpager_calendario);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_calendario);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.nueva_tienda:

                Intent i = new Intent(this, TiendaNueva.class);

                i.putExtra("idPromotor", idPromotor);

                startActivity(i);


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle b = new Bundle();
        b.putInt("idPromotor", idPromotor);

        FragmentLunes fL = new FragmentLunes();
        fL.setArguments(b);

        FragmentMartes fM = new FragmentMartes();
        fM.setArguments(b);

        FragmentMiercoles fMi = new FragmentMiercoles();
        fMi.setArguments(b);

        FragmentJueves fJ = new FragmentJueves();
        fJ.setArguments(b);

        FragmentViernes fV = new FragmentViernes();
        fV.setArguments(b);

        FragmentSabado fS = new FragmentSabado();
        fS.setArguments(b);

        FragmentDomingo fD = new FragmentDomingo();
        fD.setArguments(b);

        FragmentNoAsignado fN = new FragmentNoAsignado();
        fN.setArguments(b);

        adapter.addFrag(fD, "Domingo");
        adapter.addFrag(fL, "Lunes");
        adapter.addFrag(fM, "Martes");
        adapter.addFrag(fMi, "Miercoles");
        adapter.addFrag(fJ, "Jueves");
        adapter.addFrag(fV, "Viernes");
        adapter.addFrag(fS, "Sabado");
        adapter.addFrag(fN, "No Asignado");



        viewPager.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        viewPager.setCurrentItem(dayOfWeek-1);

    }

}
