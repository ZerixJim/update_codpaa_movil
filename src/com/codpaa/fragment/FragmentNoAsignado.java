package com.codpaa.fragment;/*
 * Created by grim on 09/06/2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codpaa.R;
import com.codpaa.adapter.SimpleRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.RutaDia;
import com.codpaa.util.Configuracion;

import java.util.ArrayList;

public class FragmentNoAsignado extends Fragment {

    SimpleRecyclerAdapter adapter;
    SQLiteDatabase base;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int idPromotor = getArguments().getInt("idPromotor");

        View view = inflater.inflate(R.layout.fragment_test, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_calendario);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new SimpleRecyclerAdapter(getActivity(), rutaDia(), idPromotor);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public ArrayList<RutaDia> rutaDia(){
        Configuracion c = new Configuracion(getContext());
        ArrayList<RutaDia> arrayRutaDia = new ArrayList<>();

        base = new BDopenHelper(getContext()).getReadableDatabase();
        String noAsign="select c.grupo, c.sucursal, v.rol, c.idTienda, v.idModo, c.latitud, " +
                " c.longitud  " +
                "from clientes as c " +
                " left join visitaTienda as v " +
                " on c.idTienda = v.idTienda " +
                " where (lunes + martes + miercoles + jueves + viernes + sabado + domingo)<=0 " +
                " and idModo=" + c.getPromotorMode() +
                " order by c.grupo asc";
        Cursor cursor = base.rawQuery(noAsign, null);


        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            final RutaDia ruta = new RutaDia();
            ruta.setNombreTienda(cursor.getString(0));
            ruta.setSucursal(cursor.getString(1));
            ruta.setRol(cursor.getString(2));
            ruta.setIdTienda(cursor.getInt(3));
            ruta.setModo(cursor.getInt(cursor.getColumnIndex("idModo")));

            ruta.setLatitud(cursor.getString(cursor.getColumnIndex("latitud")));
            ruta.setLongitud(cursor.getString(cursor.getColumnIndex("longitud")));

            arrayRutaDia.add(ruta);

        }

        cursor.close();
        base.close();

        return arrayRutaDia;
    }
}
