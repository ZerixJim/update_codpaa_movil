package com.codpaa.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.SpinnerMarcaModel;

public class CustomAdapter extends ArrayAdapter<SpinnerMarcaModel>{
	
	Activity _context;
	private ArrayList<SpinnerMarcaModel> _datos;
	

	public CustomAdapter(Activity con, int textViewResourceId,ArrayList<SpinnerMarcaModel> objects) {
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
		
		SpinnerMarcaModel temp = _datos.get(position);
		
		TextView name = (TextView) row.findViewById(R.id.txtCusSpi1);
		name.setText(temp.getNombre());
		if(position > 0){
			
			name.setTextColor(Color.BLUE);
		}
		
		
		return row;
	}
	
	
	
	
	

}
