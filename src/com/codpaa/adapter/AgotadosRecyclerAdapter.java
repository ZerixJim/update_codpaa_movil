package com.codpaa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codpaa.R;
import com.codpaa.model.ProductosModel;

import java.util.List;

public class AgotadosRecyclerAdapter extends RecyclerView.Adapter<AgotadosRecyclerAdapter.ViewHolder> {


    private List<ProductosModel> list;

    public AgotadosRecyclerAdapter(List<ProductosModel> list) {
        this.list = list;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agotados_row, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductosModel model =  list.get(position);
        holder.nombre.setText(model.getNombre());
        holder.presentacion.setText(model.getPresentacion());
        holder.cb.setText(model.getCodigoBarras());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView nombre, presentacion, cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);
            presentacion = itemView.findViewById(R.id.presentacion);
            cb = itemView.findViewById(R.id.codigo);

        }
    }


}
