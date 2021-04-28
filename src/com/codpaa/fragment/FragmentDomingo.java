package com.codpaa.fragment;/*
 * Created by Gustavo on 21/10/2015.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codpaa.R;
import com.codpaa.adapter.SimpleRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.RutaDia;
import com.codpaa.util.Configuracion;

import java.util.ArrayList;

public class FragmentDomingo extends Fragment {

    SimpleRecyclerAdapter adapter;
    SQLiteDatabase base;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

    public SimpleRecyclerAdapter getAdapter() {
        return adapter;
    }

    public ArrayList<RutaDia> rutaDia(){
        Configuracion c = new Configuracion(getContext());
        ArrayList<RutaDia> arrayRutaDia = new ArrayList<>();

        base = new BDopenHelper(getContext()).getReadableDatabase();
        String domingo="select c.grupo, c.sucursal, v.rol, c.idTienda, v.idModo, c.latitud," +
                " c.longitud, c.idFormato, c.idTipo , " +
                " count(v2.idTienda) ||'/'|| (v.lunes+v.martes+v.miercoles+v.jueves + v.viernes+v.sabado+v.domingo) visitas  " +
                " from clientes as c " +
                " left join visitaTienda as v " +
                " on c.idTienda = v.idTienda " +
                " left join (select c.idTienda from coordenadas c " +
                "  where c.semana = ( (strftime('%j', date('now', '-3 days', 'weekday 4')) - 1) / 7 + 1)  and strftime('%Y',c.fecha_captura) = strftime('%Y' ,'now') " +
                "   group by c.idTienda, c.fecha_captura " +
                " ) as v2 on (v2.idTienda = v.idTienda) " +
                " where v.domingo>=1 and idModo=" +c.getPromotorMode() +
                " group by v.idTienda " +
                " order by v.domingo asc";
        Cursor cursor = base.rawQuery(domingo, null);


        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            final RutaDia ruta = new RutaDia();
            ruta.setNombreTienda(cursor.getString(cursor.getColumnIndex("grupo")));
            ruta.setSucursal(cursor.getString(cursor.getColumnIndex("sucursal")));
            ruta.setRol(cursor.getString(cursor.getColumnIndex("rol")));
            ruta.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));
            ruta.setModo(cursor.getInt(cursor.getColumnIndex("idModo")));

            ruta.setLatitud(cursor.getString(cursor.getColumnIndex("latitud")));
            ruta.setLongitud(cursor.getString(cursor.getColumnIndex("longitud")));
            ruta.setFormato(cursor.getInt(cursor.getColumnIndex("idFormato")));
            ruta.setIdTipoTienda(cursor.getInt(cursor.getColumnIndex("idTipo")));
            ruta.setNumVisitas(cursor.getString(cursor.getColumnIndex("visitas")));

            arrayRutaDia.add(ruta);

        }

        cursor.close();
        base.close();

        return arrayRutaDia;
    }
}
