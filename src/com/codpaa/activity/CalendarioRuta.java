package com.codpaa.activity;
/*
 * Created by Gustavo on 20/10/2015.
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.codpaa.R;
import com.codpaa.adapter.ViewPagerAdapter;
import com.codpaa.fragment.FragmentDomingo;
import com.codpaa.fragment.FragmentJueves;
import com.codpaa.fragment.FragmentLunes;
import com.codpaa.fragment.FragmentMartes;
import com.codpaa.fragment.FragmentMiercoles;
import com.codpaa.fragment.FragmentSabado;
import com.codpaa.fragment.FragmentViernes;

public class CalendarioRuta extends AppCompatActivity{

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_ruta);

        toolbar = (Toolbar) findViewById(R.id.toolbar_calendario);

        //toolbar.setLogo(R.drawable.ic_launcher);
        if (toolbar != null){
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);

            }


        }

        viewPager = (ViewPager) findViewById(R.id.viewpager_calendario);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_calendario);
        tabLayout.setupWithViewPager(viewPager);


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
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentLunes(), "Lunes");
        adapter.addFrag(new FragmentMartes(), "Martes");
        adapter.addFrag(new FragmentMiercoles(), "Miercoles");
        adapter.addFrag(new FragmentJueves(), "Jueves");
        adapter.addFrag(new FragmentViernes(), "Viernes");
        adapter.addFrag(new FragmentSabado(), "Sabado");
        adapter.addFrag(new FragmentDomingo(), "Domingo");

        
        viewPager.setAdapter(adapter);

    }


}
