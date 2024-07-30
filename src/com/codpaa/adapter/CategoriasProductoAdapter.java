package com.codpaa.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.SpinnerCateProdModel;

public class CategoriasProductoAdapter extends ArrayAdapter<SpinnerCateProdModel>{

    private Context _context;
    private ArrayList<SpinnerCateProdModel> _datos;
    private LayoutInflater inflater;

    private class ViewHolder{
        TextView txtNombre;
        TextView txtDescrip;
    }


    public CategoriasProductoAdapter(Context con, int textViewResourceId, ArrayList<SpinnerCateProdModel> objects) {
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

        View row = convertView;
        CategoriasProductoAdapter.ViewHolder viewHolder;

        if(row == null){
            //LayoutInflater inflater = _context.getLayoutInflater();
            row = inflater.inflate(R.layout.custom_spinner_list, parent, false);

            viewHolder = new CategoriasProductoAdapter.ViewHolder();

            viewHolder.txtNombre =  row.findViewById(R.id.txtCusSpi1);
            viewHolder.txtDescrip =  row.findViewById(R.id.txtCusSpi2);

            row.setTag(viewHolder);

        }else {
            viewHolder = (CategoriasProductoAdapter.ViewHolder) row.getTag();
        }

        SpinnerCateProdModel temp = _datos.get(position);

        viewHolder.txtNombre.setText(temp.getCategoria());
        //viewHolder.txtDescrip.setText(temp.getImgUrl());


        return row;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        CategoriasProductoAdapter.ViewHolder viewHolder;

        if(row == null){
            //LayoutInflater inflater = _context.getLayoutInflater();
            row = inflater.inflate(R.layout.custom_spinner_list, parent, false);

            viewHolder = new CategoriasProductoAdapter.ViewHolder();

            viewHolder.txtNombre = row.findViewById(R.id.txtCusSpi1);
            viewHolder.txtDescrip =  row.findViewById(R.id.txtCusSpi2);

            row.setTag(viewHolder);

        }else {
            viewHolder = (CategoriasProductoAdapter.ViewHolder) row.getTag();
        }

        SpinnerCateProdModel temp = _datos.get(position);

        viewHolder.txtNombre.setText(temp.getCategoria());
        //viewHolder.txtDescrip.setText(temp.getImgUrl());



        return row;
    }
}
