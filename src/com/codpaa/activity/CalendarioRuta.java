package com.codpaa.activity;
/*
 * Created by Gustavo on 20/10/2015.
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.codpaa.R;
import com.codpaa.adapter.SimpleRecyclerAdapter;
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

public class CalendarioRuta extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    private int idPromotor;
    private SearchView searchView;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_ruta);

        Intent i = getIntent();
        idPromotor = i.getIntExtra("idCelular", 0);

        //Log.d("idPromo Calen", " "+ idPromotor);

        toolbar = findViewById(R.id.toolbar_calendario);

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

        viewPager =  findViewById(R.id.viewpager_calendario);

        setupViewPager(viewPager);

        tabLayout =  findViewById(R.id.tabs_calendario);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }


        Log.i("Position", viewPager.getCurrentItem() + "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);


        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {




                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //Log.i("search", newText);

                Fragment page = adapter.getItem(viewPager.getCurrentItem());

                //Log.i("fragmentype", page.getTargetFragment().getId() + " ");



                if (page instanceof FragmentLunes){


                    SimpleRecyclerAdapter simpleRecyclerAdapter = ((FragmentLunes) page).getAdapter();

                    simpleRecyclerAdapter.getFilter().filter(newText);



                }else if(page instanceof  FragmentMartes){

                    SimpleRecyclerAdapter sim = ((FragmentMartes) page).getAdapter();
                    sim.getFilter().filter(newText);


                }else if(page instanceof  FragmentMiercoles){

                    SimpleRecyclerAdapter sim = ((FragmentMiercoles) page).getAdapter();
                    sim.getFilter().filter(newText);

                }else if (page instanceof FragmentJueves){

                    SimpleRecyclerAdapter sim = ((FragmentJueves) page).getAdapter();
                    sim.getFilter().filter(newText);


                }else if (page instanceof FragmentViernes){
                    SimpleRecyclerAdapter sim = ((FragmentViernes) page).getAdapter();
                    sim.getFilter().filter(newText);

                }else if (page instanceof FragmentSabado){

                    SimpleRecyclerAdapter sim = ((FragmentSabado) page).getAdapter();
                    sim.getFilter().filter(newText);

                }else if(page instanceof FragmentDomingo){
                    SimpleRecyclerAdapter sim = ((FragmentDomingo) page).getAdapter();
                    sim.getFilter().filter(newText);

                }else if (page instanceof FragmentNoAsignado){

                    SimpleRecyclerAdapter sim = ((FragmentNoAsignado) page).getAdapter();
                    sim.getFilter().filter(newText);

                }


                //Log.i("Cantidad", newText.length() + " ");



                return false;
            }
        });





        return true;
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;

           /* case R.id.nueva_tienda:

                Intent i = new Intent(this, TiendaNueva.class);

                i.putExtra("idPromotor", idPromotor);

                startActivity(i);


                return true;*/
        }
        return super.onOptionsItemSelected(item);

    }

    private void setupViewPager(ViewPager viewPager){


        viewPager.addOnPageChangeListener(this);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
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


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        if (searchView != null){

            if (!searchView.isIconified()) {
                searchView.setIconified(true);




            }

        }


    }

    @Override
    public void onPageSelected(int position) {

        viewPager.getAdapter().notifyDataSetChanged();



    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }
}
