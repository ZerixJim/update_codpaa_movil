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
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.activity.EncuestaActivity;
import com.codpaa.adapter.EncuestasAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.Encuesta;

import java.util.ArrayList;
import java.util.List;

public class DialogEncuestas extends DialogFragment implements AdapterView.OnItemClickListener {


    private int idPromotor, idTienda;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idPromotor = getArguments().getInt("idPromotor");
        idTienda = getArguments().getInt("idTienda");



    }

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
        String sql = "select  p.id_encuesta, p.nombre_encuesta, m.nombre, p.tipo_encuesta " +
                " from preguntas as p " +
                " left join marca as m on m.idMarca=id_marca " +
                " where p.id_encuesta " +
                " not in " +
                "( select idEncuesta from  encuesta_respuestas where p.id_encuesta=idEncuesta " +
                " and idPromotor="+idPromotor+" and  idTienda ="+idTienda+" )  group by id_encuesta";

        //Log.d("SQL", sql);

        Cursor cursor = db.rawQuery(sql, null);

        for (cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()){
            final Encuesta encuesta = new Encuesta();
            encuesta.setIdEncuesta(cursor.getInt(cursor.getColumnIndex("id_encuesta")));
            encuesta.setNombreEncuesta(cursor.getString(cursor.getColumnIndex("nombre_encuesta")));
            encuesta.setNombreMarca(cursor.getString(cursor.getColumnIndex("nombre")));
            encuesta.setTipoEncuesta(cursor.getInt(cursor.getColumnIndex("tipo_encuesta")));

            array.add(encuesta);

        }

        if (cursor.getCount()  == 1){
            getDialog().setTitle("Tienes un Encuesta por contestar");
            TextView textView = (TextView) getDialog().findViewById(android.R.id.title);
            textView.setSingleLine(false);

        } else if (cursor.getCount() > 1) {

            getDialog().setTitle("Tienes Encuestas por contestar");

            TextView textView = (TextView) getDialog().findViewById(android.R.id.title);
            textView.setSingleLine(false);


        }



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
        i.putExtra("idPromotor", idPromotor);
        i.putExtra("idTienda", idTienda);
        i.putExtra("tipoEncuesta", encuesta.getTipoEncuesta());

        getActivity().startActivity(i);


    }
}
