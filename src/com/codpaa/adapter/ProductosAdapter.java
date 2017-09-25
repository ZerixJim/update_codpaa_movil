package com.codpaa.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codpaa.R;

import java.util.ArrayList;
import com.codpaa.model.ProductosModel;
import com.codpaa.util.Utilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProductosAdapter extends ArrayAdapter<ProductosModel> {

    private ArrayList<ProductosModel> _datos;
    private LayoutInflater inflater;
    private ProductosModel productosModel = null;
    private Context context;


    private static class ViewHolder {
        TextView nombreProducto;
        TextView presentacion;
        ImageView imageView;
        CheckBox checkBox;
        ProductosModel productosModel;
        ProgressBar progressBar;
    }


    public ProductosAdapter(Context context, int textViewResourceId,
                            ArrayList<ProductosModel> objects) {


        super(context,textViewResourceId,objects);

        this.context = context;

        this._datos = objects;


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }




    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        final ViewHolder viewHolder;

        if(row == null){
            row = inflater.inflate(R.layout.custom_view_productos, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombreProducto = (TextView) row.findViewById(R.id.textProNombre);
            viewHolder.presentacion = (TextView) row.findViewById(R.id.textProducPresentacion);
            viewHolder.checkBox = (CheckBox) row.findViewById(R.id.checkProduct);
            viewHolder.imageView = (ImageView) row.findViewById(R.id.image);
            viewHolder.progressBar = (ProgressBar) row.findViewById(R.id.progress);

            row.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) row.getTag();
            viewHolder.productosModel = productosModel;
        }

        productosModel = _datos.get(position);

        //Log.d("idMarca adapter", "" + productosModel.getIdMarca());


        viewHolder.nombreProducto.setText(productosModel.getNombre());
        viewHolder.presentacion.setText(productosModel.getPresentacion());



        if (position == 0 ) {
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.progressBar.setVisibility(View.GONE);
        }
        else {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setVisibility(View.VISIBLE);

        }


        if (productosModel.isChecked()){
            //viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);

        }


        if (position > 0){

            Picasso picasso = Picasso.with(context);


            //Log.d("url", Utilities.PRODUCT_PATH+productosModel.getIdMarca()+"/"+productosModel.getIdProducto()+".gif");

            picasso.load(Utilities.PRODUCT_PATH+productosModel.getIdMarca()+"/"+productosModel.getIdProducto()+".gif")
                    //.resize(bitmapDrawable.getBitmap().getWidth(), 0)
                    //.fit()
                    //.placeholder(R.drawable.progress_animated)
                    //.centerCrop()
                    //.centerInside()
                    //.noFade()
                    .into(viewHolder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            viewHolder.progressBar.setVisibility(View.GONE);

                        }
                    });


        }




        return row;
    }



    public void addElements(ArrayList<ProductosModel> datos){
        this._datos = datos;
        notifyDataSetChanged();
    }

    private ProductosModel getProductosModel(){

        final ProductosModel pm = new ProductosModel();
        pm.setIdProducto(0);
        pm.setPresentacion("Selecciona Producto");
        pm.setNombre("");

        return pm;
    }



    public ArrayList<ProductosModel> getProductos(){


        return _datos;
    }



}
