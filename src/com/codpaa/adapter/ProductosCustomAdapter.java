package com.codpaa.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.util.Utilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;



public class ProductosCustomAdapter extends ArrayAdapter<SpinnerProductoModel>{
	private Activity _context;
	private ArrayList<SpinnerProductoModel> _datos;




	private static class ViewHolder{
		TextView txtNombre;
		TextView txtPresentacion;
		TextView txtCodigoBarras;
		ImageView imagenProducto;
		TextView divider;
		ProgressBar progressBar;
		FrameLayout frameLayout;
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

		View row = convertView;
		final ViewHolder viewHolder;

		if(row == null){
			LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.row_spinner_product, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.txtNombre =  row.findViewById(R.id.text_nombre_producto);
			viewHolder.txtPresentacion =  row.findViewById(R.id.text_presentacion_producto);
			viewHolder.txtCodigoBarras =  row.findViewById(R.id.text_codigo_barras);
			viewHolder.imagenProducto =  row.findViewById(R.id.image_producto);
			viewHolder.divider =  row.findViewById(R.id.divider);
			viewHolder.progressBar = row.findViewById(R.id.progress);
			viewHolder.frameLayout =  row.findViewById(R.id.frame);

			row.setTag(viewHolder);

		}else {

			viewHolder = (ViewHolder) row.getTag();
		}

		SpinnerProductoModel temp = _datos.get(position);



		if (position != 0){

			viewHolder.txtNombre.setText(temp.getNombre() + " " + temp.getPresentacion());
		}else {
			viewHolder.txtNombre.setText(temp.getNombre());
		}
		//viewHolder.txtPresentacion.setText(temp.getPresentacion());
		viewHolder.txtPresentacion.setVisibility(View.GONE);
		viewHolder.progressBar.setVisibility(View.GONE);
		viewHolder.frameLayout.setVisibility(View.GONE);
		viewHolder.divider.setVisibility(View.GONE);


		if(temp.getCodigoBarras() != null){

			if (!temp.getCodigoBarras().equals("")){
				String cb = "CB: " + temp.getCodigoBarras();
				viewHolder.txtCodigoBarras.setText(cb);

			}else {
				viewHolder.txtCodigoBarras.setText(R.string.barcode);
			}
		}
		if(position == 0){

			viewHolder.txtCodigoBarras.setVisibility(View.GONE);
			viewHolder.txtPresentacion.setVisibility(View.GONE);
			viewHolder.txtCodigoBarras.setVisibility(View.GONE);
		}





		return row;
	}

    @Override
    public int getCount() {

        return _datos!=null ? _datos.size(): 0;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){
		View row = convertView;
        final ViewHolder viewHolder;
		
		if(row == null){
			LayoutInflater inflater = _context.getLayoutInflater();
			row = inflater.inflate(R.layout.row_spinner_product, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtNombre = row.findViewById(R.id.text_nombre_producto);
            viewHolder.txtPresentacion =  row.findViewById(R.id.text_presentacion_producto);
			viewHolder.txtCodigoBarras =  row.findViewById(R.id.text_codigo_barras);
			viewHolder.imagenProducto =  row.findViewById(R.id.image_producto);
            viewHolder.divider =  row.findViewById(R.id.divider);
            viewHolder.progressBar =  row.findViewById(R.id.progress);

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
            viewHolder.progressBar.setVisibility(View.GONE);
			viewHolder.txtPresentacion.setVisibility(View.GONE);
			viewHolder.txtCodigoBarras.setVisibility(View.GONE);
		}else {
			viewHolder.txtCodigoBarras.setVisibility(View.VISIBLE);
			viewHolder.txtPresentacion.setVisibility(View.VISIBLE);
            viewHolder.divider.setVisibility(View.VISIBLE);
            if (viewHolder.imagenProducto.getVisibility() == View.GONE)
                viewHolder.imagenProducto.setVisibility(View.VISIBLE);

			Picasso picasso = Picasso.get();

            //picasso.setIndicatorsEnabled(true);

			picasso.load(Utilities.PRODUCT_PATH+temp.getIdMarca()+"/"+temp.getIdProducto()+".gif")
					//.resize(bitmapDrawable.getBitmap().getWidth(), 0)
					//.fit()
					//.placeholder(R.drawable.progress_animated)
					//.centerCrop()
					.resize(600,200)
					.centerInside()
                    //.centerInside()
					//.noFade()
					.into(viewHolder.imagenProducto, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            viewHolder.progressBar.setVisibility(View.GONE);
                        }
                    });


            if (getCount() == position + 1){
                viewHolder.divider.setVisibility(View.INVISIBLE);

            }
		}






        if(temp.getCodigoBarras() != null){

            if (!temp.getCodigoBarras().equals("")){
				String cb = "CB: " + temp.getCodigoBarras();
                viewHolder.txtCodigoBarras.setText(cb);

            }else {
                viewHolder.txtCodigoBarras.setText(R.string.barcode);
            }
        }
		if(position == 0){
			
			viewHolder.txtCodigoBarras.setText("");
		}



		
		
		return row;
	}

}
