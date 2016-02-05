package com.codpaa.fragment;
/*
 * Created by grim on 3/02/16.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.activity.EncuestaActivity;
import com.codpaa.adapter.EncuestasAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.Encuesta;

import java.util.ArrayList;
import java.util.List;

public class DialogEncuestas extends DialogFragment implements AdapterView.OnItemClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_encuestas, container, false);
        ListView listView = (ListView) v.findViewById(R.id.listView);

        EncuestasAdapter encuestasAdapter = new EncuestasAdapter(getActivity(),R.layout.layout_encuestas, getEncuestas());

        listView.setAdapter(encuestasAdapter);
        listView.setOnItemClickListener(this);

        return v;
    }

    private List<Encuesta> getEncuestas() {

        SQLiteDatabase db = new BDopenHelper(getActivity()).getReadableDatabase();
        List<Encuesta> array = new ArrayList<>();
        String sql = "select p.id_encuesta, p.nombre_encuesta, m.nombre " +
                "from preguntas as p left join marca as m on p.id_marca=m.idMarca " +
                "group by id_encuesta;";
        Cursor cursor = db.rawQuery(sql, null);

        for (cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()){
            final Encuesta encuesta = new Encuesta();
            encuesta.setIdEncuesta(cursor.getInt(cursor.getColumnIndex("id_encuesta")));
            encuesta.setNombreEncuesta(cursor.getString(cursor.getColumnIndex("nombre_encuesta")));
            encuesta.setNombreMarca(cursor.getString(cursor.getColumnIndex("nombre")));

            array.add(encuesta);

        }

        getDialog().setTitle("Encuestas" + "(" +cursor.getCount() + ")");

        cursor.close();

        return array;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        dismiss();
        Encuesta encuesta = (Encuesta) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(), " "+encuesta.getNombreEncuesta(), Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getActivity(), EncuestaActivity.class);
        i.putExtra("idEncuesta", encuesta.getIdEncuesta());
        getActivity().startActivity(i);


    }
}
