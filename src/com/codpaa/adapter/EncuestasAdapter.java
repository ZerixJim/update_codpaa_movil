package com.codpaa.adapter;
/*
 * Created by grim on 3/02/16.
 */

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.Encuesta;

import java.util.List;

public class EncuestasAdapter extends ArrayAdapter<Encuesta>{

    private LayoutInflater inflater;

    public EncuestasAdapter(Context context, int resource, List<Encuesta> objects) {
        super(context, resource, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder{
        TextView nombreEncuesta;
        TextView marca;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Encuesta encuesta = getItem(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.row_encuestas, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.marca = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.nombreEncuesta = (TextView) convertView.findViewById(R.id.textView1);

            convertView.setTag(viewHolder);

        }else {

            viewHolder = (ViewHolder) convertView.getTag();

        }


        if (encuesta != null) {
            viewHolder.nombreEncuesta.setText(encuesta.getNombreEncuesta());
            viewHolder.marca.setText(encuesta.getNombreMarca());
        }

        return convertView;

    }
}
