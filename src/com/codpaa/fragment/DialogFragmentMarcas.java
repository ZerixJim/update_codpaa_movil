package com.codpaa.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codpaa.R;
import com.codpaa.adapter.RecyclerMarcasFaltantes;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;

import java.util.ArrayList;
import java.util.List;

public class DialogFragmentMarcas extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Bundle b = getArguments();

        View v = inflater.inflate(R.layout.dialog_marcas, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_marcas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Log.d("Dialogo marcas", b.getInt("idTienda", 0) + " ");

        RecyclerMarcasFaltantes adapter = new RecyclerMarcasFaltantes(getContext(), getMarcasFaltantes(b.getInt("idTienda", 0)));

        recyclerView.setAdapter(adapter);


        return v;
    }





    private List<MarcaModel> getMarcasFaltantes(int idTienda){

        List<MarcaModel> list = new ArrayList<>();

        String sql = "select  m.nombre, p.total_fotos  from tienda_marca tm " +
               
                " left join marca m on (m.idMarca = tm.idMarca)" +

                " left join (" +

                "  select count(*)total_fotos,p.idTienda, p.idMarca, p.fecha_captura from photo p " +

                "  group by p.idTienda, p.idMarca, p.fecha_captura  " +

                " ) p on (tm.idTienda = p.idTienda and p.idMarca = tm.idMarca and p.fecha_captura = date('now'))" +

                " where tm.idTienda = " + idTienda;

        SQLiteDatabase db = new BDopenHelper(getContext()).getReadableDatabase();


        Cursor cursor = db.rawQuery(sql, null, null);

        if (cursor.getCount() > 0){

            for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext()){

                final MarcaModel mm = new MarcaModel();

                mm.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                mm.setNumbrePhotos(cursor.getInt(cursor.getColumnIndex("total_fotos")));

                Log.d("Dialogo marcas 2", mm.getNombre());

                list.add(mm);



            }


        }
        cursor.close();
        db.close();

        return list;

    }


    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }
}
