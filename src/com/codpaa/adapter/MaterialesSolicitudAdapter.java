package com.codpaa.adapter;

/*
 * Created by grim on 27/01/17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.MaterialModel;

import java.util.List;

public class MaterialesSolicitudAdapter extends RecyclerView.Adapter<MaterialesSolicitudAdapter.ViewHolder>{


    private List<MaterialModel> materiales;


    public MaterialesSolicitudAdapter(List<MaterialModel> materiales) {
        this.materiales = materiales;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_solicitud_material, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MaterialModel materialModel = materiales.get(position);
        holder.nombreMaterial.setText(materialModel.getNombreMaterial());
        holder.cantidad.setText(materialModel.getCantidad()+" "+materialModel.getUnidad()+ "s");

        holder.producto.setVisibility(View.GONE);
        holder.fecha.setText(materialModel.getFecha());

        if (materialModel.getIdTipoMaterial() == 2){
            holder.producto.setVisibility(View.VISIBLE);
            holder.producto.setText(materialModel.getNombreProducto());
        }


    }

    @Override
    public int getItemCount() {
        return materiales.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView nombreMaterial, fecha, cantidad, status, producto;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            nombreMaterial = (TextView) itemView.findViewById(R.id.nombre_material);
            fecha = (TextView) itemView.findViewById(R.id.txt_fecha);
            cantidad = (TextView) itemView.findViewById(R.id.cantidad);
            status = (TextView) itemView.findViewById(R.id.status);
            producto = (TextView) itemView.findViewById(R.id.producto);


        }
    }


}
