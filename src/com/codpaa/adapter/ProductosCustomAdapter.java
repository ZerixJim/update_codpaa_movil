package com.codpaa.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.util.Utilities;
import com.squareup.picasso.Picasso;



public class ProductosCustomAdapter extends ArrayAdapter<SpinnerProductoModel>{
	Activity _context;
	private ArrayList<SpinnerProductoModel> _datos;
    BitmapDrawable bitmapDrawable;

	private class ViewHolder{
		TextView txtNombre;
		TextView txtPresentacion;
		TextView txtCodigoBarras;
		ImageView imagenProducto;
		TextView divider;
 	}
	

	public ProductosCustomAdapter(Activity con, int textViewResourceId,ArrayList<SpinnerProductoModel> objects) {
		super(con, textViewResourceId, objects);
		
		this._context= con;
		this._datos = objects;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            bitmapDrawable = (BitmapDrawable) _context.getResources()
                    .getDrawable(R.drawable.ic_launcher, _context.getTheme());
        }else {
            bitmapDrawable = (BitmapDrawable) _context.getResources()
                    .getDrawable(R.drawable.ic_launcher);
        }
		
		
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		 return getCustomView(position, convertView, parent);

	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return getCustomView(position, convertView, parent);
	}

    @Override
    public int getCount() {

        return _datos!=null ? _datos.size(): 0;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){
		View row = convertView;
        ViewHolder viewHolder;
		
		if(row == null){
			LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.row_spinner_product, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtNombre = (TextView) row.findViewById(R.id.text_nombre_producto);
            viewHolder.txtPresentacion = (TextView) row.findViewById(R.id.text_presentacion_producto);
			viewHolder.txtCodigoBarras = (TextView) row.findViewById(R.id.text_codigo_barras);
			viewHolder.imagenProducto = (ImageView) row.findViewById(R.id.image_producto);
            viewHolder.divider = (TextView) row.findViewById(R.id.divider);

            row.setTag(viewHolder);
			
		}else {
            viewHolder = (ViewHolder) row.getTag();
        }
		
		SpinnerProductoModel temp = _datos.get(position);
		

		viewHolder.txtNombre.setText(temp.getNombre());
        viewHolder.txtPresentacion.setText(temp.getPresentacion());


		if (position == 0){
			viewHolder.imagenProducto.setVisibility(View.GONE);
            viewHolder.divider.setVisibility(View.INVISIBLE);
		}else {

            viewHolder.divider.setVisibility(View.VISIBLE);
            if (viewHolder.imagenProducto.getVisibility() == View.GONE)
                viewHolder.imagenProducto.setVisibility(View.VISIBLE);

			Picasso picasso = Picasso.with(_context);

			picasso.load(Utilities.PRODUCT_PATH+temp.getIdMarca()+"/"+temp.getIdProducto()+".gif")
					.resize(bitmapDrawable.getBitmap().getWidth(),0)
					.into(viewHolder.imagenProducto);


            if (getCount() == position + 1){
                viewHolder.divider.setVisibility(View.INVISIBLE);
            }
		}






        if(temp.getCodigoBarras() != null){

            if (!temp.getCodigoBarras().equals("")){
				String cb = "CB: " + temp.getCodigoBarras();
                viewHolder.txtCodigoBarras.setText(cb);

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
