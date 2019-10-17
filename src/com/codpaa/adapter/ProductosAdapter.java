package com.codpaa.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codpaa.R;

import java.util.ArrayList;
import com.codpaa.model.ProductosModel;
import com.codpaa.util.Utilities;


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

            viewHolder.nombreProducto =  row.findViewById(R.id.textProNombre);
            viewHolder.presentacion = row.findViewById(R.id.textProducPresentacion);
            viewHolder.checkBox = row.findViewById(R.id.checkProduct);
            viewHolder.imageView =  row.findViewById(R.id.image);


            row.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) row.getTag();
            viewHolder.productosModel = productosModel;
        }

        productosModel = _datos.get(position);

        //Log.d("idMarca adapter", "" + productosModel.getIdMarca());


        if (position == 0){

            viewHolder.presentacion.setVisibility(View.GONE);

        }else {

            viewHolder.presentacion.setVisibility(View.VISIBLE);
        }

        viewHolder.nombreProducto.setText(productosModel.getNombre());
        viewHolder.presentacion.setText(productosModel.getPresentacion());



        if (position == 0 ) {
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
            viewHolder.imageView.setVisibility(View.GONE);

        }
        else {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.VISIBLE);


        }


        if (productosModel.isChecked()){
            //viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);

        }


        if (position > 0){


            Glide.with(context)
                    .load(Utilities.PRODUCT_PATH+productosModel.getIdMarca()+"/"+productosModel.getIdProducto()+".gif")
                    .into(viewHolder.imageView);



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
