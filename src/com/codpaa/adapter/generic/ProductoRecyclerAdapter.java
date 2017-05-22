package com.codpaa.adapter.generic;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.generic.Producto;
import com.codpaa.util.Utilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Grim on 20/05/2017.
 */

public class ProductoRecyclerAdapter extends RecyclerView.Adapter<ProductoRecyclerAdapter.ProductoViewHolder> {


    private List<Producto> mProductosList;
    private Context context;

    public ProductoRecyclerAdapter(Context context, List<Producto> mProductosList) {
        this.mProductosList = mProductosList;
        this.context = context;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_producto_generic, parent, false);

        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductoViewHolder holder, int position) {

        final Producto producto = mProductosList.get(position);


        holder.nombre.setText(producto.getNombre());
        holder.presentacion.setText(producto.getPresentacion());
        holder.barCode.setText(producto.getCodeBarras());


        if (producto.getEstatus() > 0){

            String fecha = producto.getFecha();

            if (producto.getEstatus() == 1){
                holder.estatus.setText("Descatalogado " + fecha);
            }else if(producto.getEstatus() == 2){
                holder.estatus.setText("Catalogado " + fecha);
            }else if(producto.getEstatus() == 3){
                holder.estatus.setText("No Aceptado " + fecha);
            }else if(producto.getEstatus() == 4){

                holder.estatus.setText("Proceso Catalogoar " + fecha);

            }
        }




        if (producto.getInventario() > 0 && holder.cantidad.getVisibility() == View.VISIBLE){

            holder.cantidad.setText(producto.getInventario());

        }


        Picasso picasso = Picasso.with(context);


        picasso.load(Utilities.PRODUCT_PATH+producto.getIdMarca()+"/"+producto.getIdProducto()+".gif")

                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        //viewHolder.progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError() {
                        //viewHolder.progressBar.setVisibility(View.GONE);

                    }
                });


        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                hideViews(holder);
                switch (checkedId){
                    case R.id.catalogado:


                        if(holder.inventario.getVisibility() == View.GONE){
                            holder.inventario.setVisibility(View.VISIBLE);


                            holder.inventario.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    if (holder.inventario.getText().length() > 0){
                                        producto.setInventario(Integer.parseInt(holder.inventario.getText().toString()));
                                    }


                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                        }

                        producto.setEstatus(Producto.EstatusTypes.CATALOGADO);

                        break;


                    case R.id.por_catalogar:

                        if (holder.cantidad.getVisibility() == View.VISIBLE)
                            holder.cantidad.setVisibility(View.GONE);
                        producto.setEstatus(Producto.EstatusTypes.POR_CATALOGAR);
                        producto.setInventario(0);



                        break;



                    case R.id.acepta:



                        if(holder.cantidad.getVisibility() == View.GONE){
                            holder.cantidad.setVisibility(View.VISIBLE);


                            holder.cantidad.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    if (holder.cantidad.getText().length() > 0){
                                        producto.setCantidad(Integer.parseInt(holder.cantidad.getText().toString()));
                                    }


                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                        }

                        producto.setEstatus(Producto.EstatusTypes.POR_CATALOGAR);

                        break;


                    case R.id.no_acepta:

                        producto.setEstatus(Producto.EstatusTypes.NO_ACEPTO_CATALOGACION);

                        if (holder.viewCheck.getVisibility() == View.GONE)
                            holder.viewCheck.setVisibility(View.VISIBLE);



                        producto.setInventario(0);
                        producto.setCantidad(0);





                        break;
                }

            }
        });




    }


    private void hideViews(ProductoViewHolder holder){

        holder.viewCheck.setVisibility(View.GONE);
        holder.inventario.setVisibility(View.GONE);
        holder.cantidad.setVisibility(View.GONE);

    }



    @Override
    public int getItemCount() {
        return mProductosList.size();
    }


    class ProductoViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, presentacion, barCode, estatus;
        ImageView imageView;
        RadioGroup radioGroup;
        EditText cantidad,inventario;
        LinearLayout viewCheck;
        Spinner spinner;


        ProductoViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombre_producto);
            presentacion = (TextView) itemView.findViewById(R.id.presentacion);
            barCode = (TextView) itemView.findViewById(R.id.bar_code);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
            cantidad = (EditText) itemView.findViewById(R.id.cantidad);
            inventario = (EditText) itemView.findViewById(R.id.inventario);
            viewCheck = (LinearLayout) itemView.findViewById(R.id.view_check);
            spinner = (Spinner) itemView.findViewById(R.id.spinner);
            estatus = (TextView) itemView.findViewById(R.id.estatus);

        }
    }


    public List<Producto> getProductListValidation(){
        List<Producto> list = new ArrayList<>();


        if (mProductosList.size() > 0){

            for (Producto producto: mProductosList){


                if (producto.getEstatus() != 0){

                    list.add(producto);


                }


            }


        }


        return list;

    }


}
