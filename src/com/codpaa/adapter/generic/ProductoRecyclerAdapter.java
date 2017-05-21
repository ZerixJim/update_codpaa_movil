package com.codpaa.adapter.generic;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.generic.Producto;
import com.codpaa.util.Utilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

        Producto producto = mProductosList.get(position);

        holder.nombre.setText(producto.getNombre());
        holder.presentacion.setText(producto.getPresentacion());
        holder.barCode.setText(producto.getCodeBarras());



        Picasso picasso = Picasso.with(context);


        //Log.d("url", Utilities.PRODUCT_PATH+productosModel.getIdMarca()+"/"+productosModel.getIdProducto()+".gif");

        picasso.load(Utilities.PRODUCT_PATH+producto.getIdMarca()+"/"+producto.getIdProducto()+".gif")
                //.resize(bitmapDrawable.getBitmap().getWidth(), 0)
                //.fit()
                //.placeholder(R.drawable.progress_animated)
                //.centerCrop()
                //.centerInside()
                //.noFade()
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

                switch (checkedId){
                    case R.id.catalogado:

                        if(holder.cantidad.getVisibility() == View.GONE){
                            holder.cantidad.setVisibility(View.VISIBLE);
                        }

                        break;


                    case R.id.por_catalogar:

                        holder.cantidad.setVisibility(View.GONE);

                        break;
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return mProductosList.size();
    }


    class ProductoViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, presentacion, barCode;
        ImageView imageView;
        RadioGroup radioGroup;
        EditText cantidad;


        ProductoViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombre_producto);
            presentacion = (TextView) itemView.findViewById(R.id.presentacion);
            barCode = (TextView) itemView.findViewById(R.id.bar_code);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
            cantidad = (EditText) itemView.findViewById(R.id.cantidad);

        }
    }


}
