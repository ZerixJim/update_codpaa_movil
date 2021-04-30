package com.codpaa.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codpaa.R;
import com.codpaa.model.ProductosModel;
import com.codpaa.util.Utilities;

import java.util.ArrayList;
import java.util.List;

public class AgotadosRecyclerAdapter extends RecyclerView.Adapter<AgotadosRecyclerAdapter.ViewHolder> {


    private List<ProductosModel> list;
    private Context context;

    public AgotadosRecyclerAdapter(Context context,List<ProductosModel> list) {
        this.list = list;

        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agotados_row, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ProductosModel model =  list.get(position);
        holder.nombre.setText(model.getNombre());
        holder.presentacion.setText(model.getPresentacion());
        holder.cb.setText(model.getCodigoBarras());



        holder.rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("onCheck", "" + i);

                model.setIdStatusProduct(i);

            }
        });



        if (model.getHasImage() == 1 ){

            Glide.with(context)
                    .load(Utilities.PRODUCT_PATH + model.getIdMarca() + "/" + model.getIdProducto() + ".gif")
                    .placeholder(R.drawable.ic_no_image)
                    .into(holder.image);
        }else {

            Glide.with(context)
                    .load(R.drawable.ic_no_image)
                    .into(holder.image);

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView nombre, presentacion, cb;
        private ImageView image;
        private RadioGroup rGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);
            presentacion = itemView.findViewById(R.id.presentacion);
            cb = itemView.findViewById(R.id.codigo);
            image = itemView.findViewById(R.id.image);

            rGroup = itemView.findViewById(R.id.radio);

            RadioButton radioButton1 = new RadioButton(context);
            radioButton1.setId(1);
            radioButton1.setText("Agotado");
            RadioButton radioButton2 = new RadioButton(context);
            radioButton2.setId(2);
            radioButton2.setText("Pre-agotado");
            RadioButton radioButton3 = new RadioButton(context);
            radioButton3.setId(3);
            radioButton3.setText("Disponible");


            rGroup.addView(radioButton1);
            rGroup.addView(radioButton2);
            rGroup.addView(radioButton3);



        }
    }


    public List<ProductosModel> getItemsModified(){

        List<ProductosModel> array = new ArrayList<>();

        if (list != null){

            for (ProductosModel p : list){

                if (p.getIdStatusProduct() != 0 && p.getIdProducto() != 0){
                    array.add(p);
                }
            }

        }

        return array;

    }




}
