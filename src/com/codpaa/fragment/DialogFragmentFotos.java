package com.codpaa.fragment;
/*
 * Created by grim on 14/01/16.
 */


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codpaa.R;
import com.codpaa.adapter.FotosAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.FotosModel;

import java.util.ArrayList;

public class DialogFragmentFotos extends DialogFragment{




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        View v = inflater.inflate(R.layout.dialog_fragment_fotos, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_fotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getDialog().setTitle("Fotos");

        FotosAdapter fotosAdapter = new FotosAdapter(getFotos(bundle.getInt("idTienda")), getActivity());

        recyclerView.setAdapter(fotosAdapter);



        return v;
    }

    private ArrayList<FotosModel> getFotos(int idTienda) {

        SQLiteDatabase db = new BDopenHelper(getActivity()).getReadableDatabase();
        ArrayList<FotosModel> arrayList = new ArrayList<>();
        String sql = "select p.idPhoto, " +
                "m.nombre as nombre_marca, p.fecha, p.imagen, p.status,te.nombre as n_exhi " +
                "from photo as p " +
                "left join tipoexhibicion as te on te.idExhibicion=p.idExhibicion " +
                "left join clientes as c on p.idTienda=c.idTienda " +
                "left join marca as m on p.idMarca=m.idMarca where p.idTienda="+idTienda;

        Cursor cF = db.rawQuery(sql, null);

        for (cF.moveToFirst();!cF.isAfterLast(); cF.moveToNext()){
            final FotosModel fm = new FotosModel();
            fm.setMarca(cF.getString(cF.getColumnIndex("nombre_marca")));
            fm.setImg(cF.getString(cF.getColumnIndex("imagen")));
            fm.setStatus(cF.getInt(cF.getColumnIndex("status")));
            fm.setFecha(cF.getString(cF.getColumnIndex("fecha")));
            fm.setTipo(cF.getString(cF.getColumnIndex("n_exhi")));

            arrayList.add(fm);
        }

        cF.close();
        return arrayList;
    }
}
