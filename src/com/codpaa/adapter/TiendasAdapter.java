package com.codpaa.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.TiendasModel;

import java.util.ArrayList;


public class TiendasAdapter extends ArrayAdapter<TiendasModel> {

    Context _context;
    ArrayList<TiendasModel> _datos;
    LayoutInflater inflater;

    private class ViewHolder{
        TextView nombreTienda;
        TextView sucursal;

    }

    public TiendasAdapter(Activity con, int textViewResourceId,ArrayList<TiendasModel> objects) {
        super(con, textViewResourceId, objects);

        this._context= con;
        this._datos = objects;

        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.custom_row_tiendas, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombreTienda = (TextView) convertView.findViewById(R.id.txt_nombre_tienda);
            viewHolder.sucursal = (TextView) convertView.findViewById(R.id.txt_sucursal_tienda);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TiendasModel temp = _datos.get(position);

        viewHolder.nombreTienda.setText(temp.getNombre());
        viewHolder.sucursal.setText(temp.getSucursal());
        if(position > 0){

            viewHolder.nombreTienda.setTextColor(Color.BLUE);
        }




        return convertView;
    }
}
