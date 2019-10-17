package com.codpaa.adapter;


/*
 * Created by grim on 29/08/2016.
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codpaa.R;
import com.codpaa.model.ProductosModel;
import com.codpaa.util.Utilities;


import java.util.ArrayList;
import java.util.List;

public class RecyclerProductosMultiSelect extends RecyclerView.Adapter<RecyclerProductosMultiSelect.ProductoViewHolder>{


    private List<ProductosModel> productosModels;
    private Context context;

    public RecyclerProductosMultiSelect(List<ProductosModel> productosModels, Context context){

        this.productosModels = productosModels;
        this.context = context;

    }

    public List<ProductosModel> getProductosModels() {
        return productosModels;
    }


    public List<Integer> getSelectedItems(){

        List<Integer> arraySelected = new ArrayList<>();

        for (ProductosModel p: productosModels){

            if (p.isSeleted()){

                arraySelected.add(p.getIdProducto());
            }

        }

        Log.d("Productos SIZE ", "" +arraySelected.size());
        return arraySelected;
    }

    public boolean isJustOneSelected() {

        boolean selected = false;

        for ( ProductosModel p : productosModels) {
            if (p.isSeleted())
                selected = true;
        }

        return selected;

    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_producto_catalogo, parent, false);


        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductoViewHolder holder, int position) {

        final ProductosModel productosModel = productosModels.get(position);

        holder.nombre.setText(productosModel.getNombre());
        holder.presentacion.setText(productosModel.getPresentacion());

        if (productosModel.isSeleted()){
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()){
                    productosModel.setSeleted(true);
                } else {
                    productosModel.setSeleted(false);
                }
            }
        });




        //picasso.setIndicatorsEnabled(true);

        Glide.with(context)
                .load(Utilities.PRODUCT_PATH+productosModel.getIdMarca()+"/"+productosModel.getIdProducto()+".gif")
                .into(holder.imageView);




    }

    @Override
    public int getItemCount() {
        return productosModels.size();
    }

    public void clearAdapter(){
        productosModels.clear();
        notifyDataSetChanged();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, presentacion;
        ImageView imageView;
        CheckBox checkBox;

        public ProductoViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.txt_nombre);
            presentacion = (TextView) itemView.findViewById(R.id.txt_presentacion);

            imageView = (ImageView) itemView.findViewById(R.id.image);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);


        }


    }

}
