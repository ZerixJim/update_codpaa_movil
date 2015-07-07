package com.codpaa.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.models.SpinnerProductoModel;

public class ProductosCustomAdapter extends ArrayAdapter<SpinnerProductoModel>{
	Activity _context;
	private ArrayList<SpinnerProductoModel> _datos;
	

	public ProductosCustomAdapter(Activity con, int textViewResourceId,ArrayList<SpinnerProductoModel> objects) {
		super(con, textViewResourceId, objects);
		
		this._context= con;
		this._datos = objects;
		
		
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
		View row = convertView;
		
		if(row == null){
			LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.custom_spinner_list, parent, false);
			
		}
		
		SpinnerProductoModel temp = _datos.get(position);
		
		TextView name = (TextView) row.findViewById(R.id.txtCusSpi1);
		name.setText(temp.getNombre());
		
		TextView presentacion = (TextView) row.findViewById(R.id.txtCusSpi2);
		presentacion.setText(temp.getPresentacion());
		if(position > 0){
			
			name.setTextColor(Color.BLUE);
		}
		
		
		return row;
	}

}
