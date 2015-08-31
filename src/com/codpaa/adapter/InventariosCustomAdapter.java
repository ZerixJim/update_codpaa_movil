package com.codpaa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.codpaa.R;

import java.util.ArrayList;

import com.codpaa.model.InventarioModel;


public class InventariosCustomAdapter extends ArrayAdapter<InventarioModel> {

    private ArrayList<InventarioModel> datos;
    Context context;
    LayoutInflater inflater;
    InventarioModel inventarioModel = null;

    private class ViewHolder{
        TextView txtMarca;
        TextView txtProducto;
        TextView txtStatus;
        TextView txtTipo;
        TextView txtSistema;
        TextView txtFisico;
        InventarioModel inventarioModel;
    }

    public InventariosCustomAdapter(Context context, int textViewResourceId, ArrayList<InventarioModel> objects) {
        super(context, textViewResourceId, objects);

        this.context = context;
        this.datos = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        Log.d("Datos size", "tamano: "+ datos.size());

        if(row == null){
            row = inflater.inflate(R.layout.custom_view_inventario,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.txtMarca = (TextView) row.findViewById(R.id.txtMarcaCustomInve);
            viewHolder.txtProducto = (TextView) row.findViewById(R.id.txtCustomProductoInve);
            viewHolder.txtStatus = (TextView) row.findViewById(R.id.txtCustomStatusInve);
            viewHolder.txtTipo = (TextView) row.findViewById(R.id.txtCustomTipoInve);
            viewHolder.txtSistema = (TextView) row.findViewById(R.id.txtCustomSisteInve);
            viewHolder.txtFisico = (TextView) row.findViewById(R.id.txtCustomFisicoInv);

            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
            viewHolder.inventarioModel = inventarioModel;
        }

        Log.d("Datos size", "tamano: "+ datos.size());

        if(datos.size() <= 0){
            viewHolder.txtMarca.setText("No Existen registros");
            Log.d("Datos size", "vacio");
        }else{
            inventarioModel = null;
            inventarioModel = datos.get(position);
            viewHolder.txtMarca.setText(inventarioModel.getMarca());
            viewHolder.txtProducto.setText(inventarioModel.getProducto());
            if(inventarioModel.getStatus() == 1){
                viewHolder.txtStatus.setText("En proceso");
                viewHolder.txtStatus.setTextColor(Color.BLUE);
            }else if(inventarioModel.getStatus() ==2){
                viewHolder.txtStatus.setText("Enviado");
                viewHolder.txtStatus.setTextColor(Color.GREEN);
            }

            viewHolder.txtTipo.setText(inventarioModel.getTipo());
            if(inventarioModel.getCantidadSistema() > 0){
                int cantidadSistema = inventarioModel.getCantidadSistema();
                viewHolder.txtSistema.setText("Sistema: "+cantidadSistema);
            }else {
                viewHolder.txtSistema.setText("");
            }

            if(inventarioModel.getCantidadFisico() > 0 ){
                int cantidadFisico = inventarioModel.getCantidadFisico();
                viewHolder.txtFisico.setText("Fisico: "+cantidadFisico);
            }else {
                viewHolder.txtFisico.setText("");
            }
        }



        return row;
    }
}
