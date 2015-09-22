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
import com.codpaa.model.SpinnerProductoModel;

public class ProductosCustomAdapter extends ArrayAdapter<SpinnerProductoModel>{
	Activity _context;
	private ArrayList<SpinnerProductoModel> _datos;

	private class ViewHolder{
		TextView txtNombre;
		TextView txtPresentacion;
		TextView txtCodigoBarras;
	}
	

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
        ViewHolder viewHolder;
		
		if(row == null){
			LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.custom_spinner_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtNombre = (TextView) row.findViewById(R.id.txtCusSpi1);
            viewHolder.txtPresentacion = (TextView) row.findViewById(R.id.txtCusSpi2);
			viewHolder.txtCodigoBarras = (TextView) row.findViewById(R.id.txt_cb);

            row.setTag(viewHolder);
			
		}else {
            viewHolder = (ViewHolder) row.getTag();
        }
		
		SpinnerProductoModel temp = _datos.get(position);
		

		viewHolder.txtNombre.setText(temp.getNombre());
        viewHolder.txtPresentacion.setText(temp.getPresentacion());
        if(temp.getCodigoBarras() != null){

            if (!temp.getCodigoBarras().equals("")){
                viewHolder.txtCodigoBarras.setText("CB:" + temp.getCodigoBarras());
            }else {
                viewHolder.txtCodigoBarras.setText("CB:N/A");
            }
        }
		if(position == 0){
			
			viewHolder.txtCodigoBarras.setText("");
		}

		
		
		return row;
	}

}
