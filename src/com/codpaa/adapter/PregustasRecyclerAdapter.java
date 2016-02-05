package com.codpaa.adapter;
/*
 * Created by Gustavo on 04/02/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.Pregunta;

import java.util.List;


public class PregustasRecyclerAdapter extends RecyclerView.Adapter<PregustasRecyclerAdapter.PreguntaViewHolder>{

    List<Pregunta> preguntas;
    Context context;

    public PregustasRecyclerAdapter(List<Pregunta> preguntas, Context context) {
        this.preguntas = preguntas;
        this.context = context;
    }

    @Override
    public PreguntaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_pregunta, parent, false);

        return new PreguntaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreguntaViewHolder holder, int position) {

        final Pregunta pregunta = preguntas.get(position);

        holder.pregunta.setText(pregunta.getNumeroPregunta() + ".- " + pregunta.getContenidoPregunta());

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("RadioGroup", " " + group.getCheckedRadioButtonId());
                pregunta.setIdRespesta(group.getCheckedRadioButtonId());
            }
        });

        holder.radioGroup.check(pregunta.getIdRespesta());



    }

    public boolean getRespuestaFaltante(){

        for (Pregunta pr : preguntas){
            if (pr.getIdRespesta() <= 0)
                return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    class PreguntaViewHolder extends RecyclerView.ViewHolder{
        TextView pregunta;
        RadioGroup radioGroup;


        public PreguntaViewHolder(View itemView) {
            super(itemView);
            pregunta = (TextView) itemView.findViewById(R.id.textView);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.respuesta_group);


            getRespuestas(radioGroup);


        }


        private void getRespuestas(RadioGroup radioGroup){
            SQLiteDatabase db = new BDopenHelper(context).getReadableDatabase();

            String sql = "select id_tipo, descripcion from respuestatipo";

            Cursor cursor = db.rawQuery(sql, null);

            RadioButton radio[] = new RadioButton[cursor.getCount()];

            for (cursor.moveToFirst(); !cursor.isAfterLast() ; cursor.moveToNext()){
                radio[cursor.getPosition()] = new RadioButton(context);
                radio[cursor.getPosition()].setText(cursor.getString(cursor.getColumnIndex("descripcion")));
                radio[cursor.getPosition()].setId(cursor.getInt(cursor.getColumnIndex("id_tipo")));
                radioGroup.addView(radio[cursor.getPosition()]);

            }
            cursor.close();
            db.close();

        }
    }

}
