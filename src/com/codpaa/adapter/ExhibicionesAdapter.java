package com.codpaa.adapter;

/*
 * Created by grim on 9/01/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.codpaa.R;
import com.codpaa.model.ExhibicionesModel;


import java.util.ArrayList;



public class ExhibicionesAdapter extends ArrayAdapter<ExhibicionesModel>{

    private ArrayList<ExhibicionesModel> datos;
    Context context;
    LayoutInflater inflater;

    static class ViewHolder{
        TextView txtMarca;
        TextView txtProducto;
        TextView txtCantidad;
        TextView txtTipo;

    }

    public ExhibicionesAdapter(Context context, int textViewResourceId, ArrayList<ExhibicionesModel> objects) {
        super(context, textViewResourceId, objects);

        this.context = context;
        this.datos = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ExhibicionesModel exhibicionesModel = datos.get(position);
        ViewHolder viewHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_exhi,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.txtMarca = (TextView) convertView.findViewById(R.id.exhibi_marca);
            viewHolder.txtProducto = (TextView) convertView.findViewById(R.id.exhibi_producto);
            viewHolder.txtTipo = (TextView) convertView.findViewById(R.id.exhibi_tipo);
            viewHolder.txtCantidad = (TextView) convertView.findViewById(R.id.exhibi_tamanio);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.txtMarca.setText(exhibicionesModel.getMarca());
        viewHolder.txtProducto.setText(exhibicionesModel.getProducto());
        viewHolder.txtTipo.setText(exhibicionesModel.getTipo());
        viewHolder.txtCantidad.setText(exhibicionesModel.getCantidad());




        return convertView;
    }
}
