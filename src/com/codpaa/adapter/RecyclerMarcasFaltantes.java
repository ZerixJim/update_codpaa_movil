package com.codpaa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codpaa.R;
import com.codpaa.model.MarcaModel;

import java.util.List;

public class RecyclerMarcasFaltantes extends RecyclerView.Adapter<RecyclerMarcasFaltantes.ViewHolder> {


    private List<MarcaModel> list;
    private Context context;


    public RecyclerMarcasFaltantes(Context context, List<MarcaModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_marcas_faltantes, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        MarcaModel mm = list.get(position);

        holder.nombreMarca.setText(mm.getNombre());


        if (mm.getNumbrePhotos() > 0){
            holder.photo.setVisibility(View.VISIBLE);
        }else{
            holder.photo.setVisibility(View.INVISIBLE);
        }


        if (mm.getNumberFrentes() > 0){
            holder.frentes.setVisibility(View.VISIBLE);
        }else {
            holder.frentes.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombreMarca;
        private ImageView check, photo, frentes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreMarca = itemView.findViewById(R.id.nombre_marca);
            check = itemView.findViewById(R.id.check);
            photo = itemView.findViewById(R.id.photo);
            frentes = itemView.findViewById(R.id.frentes);

        }
    }



}
