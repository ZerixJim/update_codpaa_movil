package com.codpaa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;

import java.util.ArrayList;

import com.codpaa.model.FrentesModel;



public class FrentesCustomAdapter extends ArrayAdapter<FrentesModel> {

    private final ArrayList<FrentesModel> datos;
    Context context;
    LayoutInflater inflater;

    private class ViewHolder{
        TextView txtMarca;
        TextView txtProducto;
        TextView txtCantidad;
        TextView txtStatus;
        TextView txtFecha;
        //TextView txtFilas;
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

            viewHolder.txtMarca =  row.findViewById(R.id.textViewRowMarca);
            viewHolder.txtProducto = row.findViewById(R.id.textViewRowProducto);
            viewHolder.txtCantidad =  row.findViewById(R.id.textViewRowCantidad);
            viewHolder.txtStatus =  row.findViewById(R.id.textViewRowStatus);
            viewHolder.txtFecha =  row.findViewById(R.id.textCusRowFecha);
            //viewHolder.txtFilas =  row.findViewById(R.id.cantidad_filas);
            viewHolder.freModel = frentesModel;

            row.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) row.getTag();
            viewHolder.freModel = frentesModel;
        }


        if (datos.size() <= 1 && frentesModel.getProducto() == null){
            viewHolder.txtStatus.setVisibility(View.INVISIBLE);
            viewHolder.txtCantidad.setVisibility(View.INVISIBLE);
        }


        viewHolder.txtMarca.setText(frentesModel.getMarca());
        if(frentesModel.getCategoria() == 0){
            viewHolder.txtProducto.setText(frentesModel.getProducto());
        }else{
            int categoria;
            categoria = frentesModel.getCategoria();
            switch(categoria){
                case 1:
                    viewHolder.txtProducto.setText("BLANQUEADORES");
                    break;
                case 2:
                    viewHolder.txtProducto.setText("LIMPIADORES MULTIUSOS");
                    break;
                case 3:
                    viewHolder.txtProducto.setText("DESMANCHADORES");
                    break;
                case 4:
                    viewHolder.txtProducto.setText("DESINFECCIÓN");
                    break;
                default:
                    viewHolder.txtProducto.setText("NO DEFINIDO");
                    break;
            }
        }

        //viewHolder.txtProducto.setText(frentesModel.getProducto());
        if(frentesModel.getCategoria() == 0) {
            viewHolder.txtCantidad.setText("Cantidad: " + frentesModel.getCantidad());
        }else{
            viewHolder.txtCantidad.setText("Cantidad: " + frentesModel.getCantidad() + " cm");
        }



        //viewHolder.txtFilas.setText("Frentes Linea de Cajas: " + String.valueOf(frentesModel.getFilas()));
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
