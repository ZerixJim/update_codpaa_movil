package com.codpaa.adapter.generic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.fragment.DetalleProductoDialogFragment;
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


        holder.precioCompra.setText("precio compra $" + producto.getPrecioCompra());
        holder.precioVenta.setText("precio sugerido venta $" + producto.getPrecioVenta());

        holder.utilidad.setText("margen utilidad " + producto.getUtilidad() + "%");

        holder.fechaPrecio.setText("fecha precio " + producto.getFechaPrecio());


        holder.detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDialogDetalle(producto.getIdProducto());

            }
        });



        if (producto.getIdEstatusCatalogacion() == 2 || producto.getIdEstatusCatalogacion() == 4){


            holder.radioGroup.setVisibility(View.INVISIBLE);

        }


        if (producto.getEstatus() > 0){


            if (producto.getEstatus() == Producto.EstatusTypes.DESCATALOGADO){
                holder.estatus.setText("Descatalogado ");
            }else if(producto.getEstatus() == Producto.EstatusTypes.CATALOGADO){

                holder.radioGroup.setVisibility(View.INVISIBLE);

                holder.estatus.setText("Catalogado ");

                holder.cardView.setBackgroundColor(Color.parseColor("#adffc9"));



            }else if(producto.getEstatus() == Producto.EstatusTypes.PROCESO_CATALOGACION){
                holder.estatus.setText("Proceso Catalogar");
            }else if(producto.getEstatus() == Producto.EstatusTypes.ACEPTO_CATALOGACION){

                holder.radioGroup.setVisibility(View.INVISIBLE);

                holder.estatus.setText("Acepto Catalogar ");

                holder.cardView.setBackgroundColor(Color.parseColor("#efffad"));



            }
        }


        if (producto.getCantidad() > 0 && holder.cantidad.getVisibility() == View.VISIBLE){

            holder.cantidad.setText(String.valueOf(producto.getCantidad()));

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

                producto.setChanges(true);

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
                                        producto.setCantidad(Integer.parseInt(holder.inventario.getText().toString()));
                                    }


                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                        }

                        producto.setEstatus(Producto.EstatusTypes.CATALOGADO);

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

                        producto.setEstatus(Producto.EstatusTypes.ACEPTO_CATALOGACION);

                        break;


                    case R.id.no_acepta:

                        producto.setEstatus(Producto.EstatusTypes.PROCESO_CATALOGACION);

                        if (holder.viewCheck.getVisibility() == View.GONE)
                            holder.viewCheck.setVisibility(View.VISIBLE);



                        producto.setCantidad(0);


                        break;
                }

            }
        });



        holder.faltaEspacio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    producto.addOjecion(buttonView.getText().toString());
                }else {

                    producto.removeObjecion(buttonView.getText().toString());


                }

                //Log.d("arraitems", producto.getObjeciones().toString());
            }
        });


        holder.falteRecurso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    producto.addOjecion(buttonView.getText().toString());
                }else {

                    producto.removeObjecion(buttonView.getText().toString());

                }

                //Log.d("arraitems", producto.getObjeciones().toString());
            }
        });


        holder.competitividad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    producto.addOjecion(buttonView.getText().toString());
                }else {

                    producto.removeObjecion(buttonView.getText().toString());

                }
            }
        });


        holder.seConsulta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    producto.addOjecion(buttonView.getText().toString());
                }else {

                    producto.removeObjecion(buttonView.getText().toString());

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

        TextView nombre, presentacion, barCode, estatus, precioCompra, precioVenta, utilidad;
        TextView fechaPrecio, detalle;
        CardView cardView;
        ImageView imageView;
        RadioGroup radioGroup;
        EditText cantidad,inventario;
        LinearLayout viewCheck;
        Spinner spinner;
        CheckBox faltaEspacio, falteRecurso, competitividad, seConsulta;


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


            faltaEspacio = (CheckBox) itemView.findViewById(R.id.falta_espacio);
            falteRecurso = (CheckBox) itemView.findViewById(R.id.faltan_recursos);
            competitividad = (CheckBox) itemView.findViewById(R.id.competitividad);
            seConsulta = (CheckBox) itemView.findViewById(R.id.se_consultara);


            precioCompra = (TextView) itemView.findViewById(R.id.precio_compra);
            precioVenta = (TextView) itemView.findViewById(R.id.precio_venta);
            utilidad = (TextView) itemView.findViewById(R.id.utilidad);

            fechaPrecio = (TextView) itemView.findViewById(R.id.fecha_precio);
            detalle = (TextView) itemView.findViewById(R.id.detalle);

            cardView = (CardView) itemView.findViewById(R.id.card);


        }
    }



    private void createDialogDetalle(int idProducto){

        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();


        DetalleProductoDialogFragment dialog = DetalleProductoDialogFragment.newInstance();


        Bundle bundle = new Bundle();

        bundle.putInt("idProducto", idProducto);

        dialog.setArguments(bundle);


        dialog.show(fragmentManager, "dialog");




    }


    public List<Producto> getProductListValidation(){
        List<Producto> list = new ArrayList<>();


        if (mProductosList.size() > 0){

            for (Producto producto: mProductosList){



                if (producto.isChanges()){

                    list.add(producto);

                }


            }


        }


        return list;

    }


}
