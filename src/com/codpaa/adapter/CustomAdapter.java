package com.codpaa.adapter;


import java.util.ArrayList;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.SpinnerMarcaModel;


public class CustomAdapter extends ArrayAdapter<SpinnerMarcaModel>{
	
	private Context _context;
	private ArrayList<SpinnerMarcaModel> _datos;
	private LayoutInflater inflater;

	private class ViewHolder{
        TextView txtNombre;
        TextView txtDescrip;
    }
	

	public CustomAdapter(Context con, int textViewResourceId,ArrayList<SpinnerMarcaModel> objects) {
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
		ViewHolder viewHolder;

		if(row == null){
			//LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.custom_spinner_list, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.txtNombre = (TextView) row.findViewById(R.id.txtCusSpi1);
			viewHolder.txtDescrip = (TextView) row.findViewById(R.id.txtCusSpi2);

			row.setTag(viewHolder);

		}else {
			viewHolder = (ViewHolder) row.getTag();
		}

		SpinnerMarcaModel temp = _datos.get(position);

		viewHolder.txtNombre.setText(temp.getNombre());
		//viewHolder.txtDescrip.setText(temp.getImgUrl());


		return row;
	}

	private View getCustomView(int position, View convertView, ViewGroup parent){
		View row = convertView;
        ViewHolder viewHolder;
		
		if(row == null){
			//LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.custom_spinner_list, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.txtNombre = (TextView) row.findViewById(R.id.txtCusSpi1);
            viewHolder.txtDescrip = (TextView) row.findViewById(R.id.txtCusSpi2);

            row.setTag(viewHolder);
			
		}else {
            viewHolder = (ViewHolder) row.getTag();
        }
		
		SpinnerMarcaModel temp = _datos.get(position);
		
		viewHolder.txtNombre.setText(temp.getNombre());
        //viewHolder.txtDescrip.setText(temp.getImgUrl());


		
		
		return row;
	}
	
	
	
	
	

}
