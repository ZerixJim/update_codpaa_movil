package com.codpaa.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codpaa.R;
import com.codpaa.adapter.MenuAdapter;
import com.codpaa.adapter.SimpleRecyclerAdapter;
import com.codpaa.adapter.ViewPagerAdapter;
import com.codpaa.util.Configuracion;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteFragment extends Fragment implements ViewPager.OnPageChangeListener {


    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    private int idPromotor;
    private SearchView searchView;
    private ViewPagerAdapter adapter;

    private static final String ID_PROM = "idPromo";



    public RouteFragment() {
        // Required empty public constructor
    }



    public static RouteFragment newInstance(int idPromotor) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putInt(ID_PROM, idPromotor);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPromotor = getArguments().getInt(ID_PROM, 0);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_route, container, false);

        toolbar = root.findViewById(R.id.toolbar_calendario);


        try {

            //toolbar.setLogo(R.drawable.ic_launcher);
            if (toolbar != null){
                ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

                ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
                if (actionBar != null){
                    actionBar.setDisplayHomeAsUpEnabled(true);

                    Configuracion config = new Configuracion(getContext());
                    String modo = "";

                    if(config.getPromotorMode() == 1){

                        modo = "Promotor";

                    }else if(config.getPromotorMode() == 2){

                        modo = "Impulsor";

                    }


                    actionBar.setSubtitle(modo);


                }


            }

        }catch (NullPointerException e){

            e.printStackTrace();
        }



        viewPager =  root.findViewById(R.id.viewpager_calendario);

        setupViewPager(viewPager);

        tabLayout =  root.findViewById(R.id.tabs_calendario);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }


        Log.i("Position", viewPager.getCurrentItem() + "");


        setHasOptionsMenu(true);

        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //MenuInflater inflater = inflater.getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);


        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

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


        super.onCreateOptionsMenu(menu, inflater);

    }

    private void setupViewPager(ViewPager viewPager){


        viewPager.addOnPageChangeListener(this);

        adapter = new ViewPagerAdapter(getChildFragmentManager());
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