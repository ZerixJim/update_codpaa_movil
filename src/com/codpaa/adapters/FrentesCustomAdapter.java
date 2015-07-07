package com.codpaa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;

import java.util.ArrayList;

import com.codpaa.models.FrentesModel;



public class FrentesCustomAdapter extends ArrayAdapter<FrentesModel> {

    private ArrayList<FrentesModel> datos;
    Context context;
    LayoutInflater inflater;

    private class ViewHolder{
        TextView txtMarca;
        TextView txtProducto;
        TextView txtCantidad;
        TextView txtStatus;
        TextView txtFecha;
        FrentesModel freModel;
    }

    public FrentesCustomAdapter(Context context, int textViewResourceId, ArrayList<FrentesModel> objects) {
        super(context, textViewResourceId, objects);

        this.context = context;
        this.datos = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FrentesModel frentesModel = datos.get(position);
        ViewHolder viewHolder;

        if(row == null){

            row = inflater.inflate(R.layout.custom_view_frentes,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.txtMarca = (TextView) row.findViewById(R.id.textViewRowMarca);
            viewHolder.txtProducto = (TextView) row.findViewById(R.id.textViewRowProducto);
            viewHolder.txtCantidad = (TextView) row.findViewById(R.id.textViewRowCantidad);
            viewHolder.txtStatus = (TextView) row.findViewById(R.id.textViewRowStatus);
            viewHolder.txtFecha = (TextView) row.findViewById(R.id.textCusRowFecha);
            viewHolder.freModel = frentesModel;

            row.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) row.getTag();
            viewHolder.freModel = frentesModel;
        }

        viewHolder.txtMarca.setText(frentesModel.getMarca());
        viewHolder.txtProducto.setText(frentesModel.getProducto());
        viewHolder.txtCantidad.setText("Cantidad: "+String.valueOf(frentesModel.getCantidad()));
        viewHolder.txtFecha.setText(frentesModel.getFecha());
        if(frentesModel.getStatus() == 2){
            viewHolder.txtStatus.setText("Enviado");
            viewHolder.txtStatus.setTextColor(Color.GREEN);
        }else if(frentesModel.getStatus() == 3) {
            viewHolder.txtStatus.setText("");
            viewHolder.txtCantidad.setText("");

        }else{
            viewHolder.txtStatus.setText("En proceso");
            viewHolder.txtStatus.setTextColor(Color.BLUE);
        }


        return row;
    }
}
