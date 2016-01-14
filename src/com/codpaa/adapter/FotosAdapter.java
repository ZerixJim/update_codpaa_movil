package com.codpaa.adapter;
/*
 * Created by grim on 14/01/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.FotosModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class FotosAdapter extends RecyclerView.Adapter<FotosAdapter.ViewHolderFotos> {

    ArrayList<FotosModel> fotos = new ArrayList<>();
    Context context;


    public FotosAdapter(ArrayList<FotosModel> fotos, Context context) {
        this.fotos = fotos;
        this.context = context;
    }

    @Override
    public ViewHolderFotos onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_fotos, parent,false);


        return new ViewHolderFotos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderFotos holder, int position) {

        FotosModel fo = fotos.get(position);

        holder.marca.setText(fo.getMarca());
        holder.fecha.setText(fo.getFecha());


        if (fo.getStatus() == 2) {
            holder.status.setText("enviado");
        }else {
            holder.status.setText("en proceso");
        }

        Picasso.with(context)
                .load(new File(fo.getImg()))
                .placeholder(R.drawable.placeholder)
                .resize(50, 100)
                .centerCrop()
                .into(holder.img);


    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    class ViewHolderFotos extends RecyclerView.ViewHolder{

        ImageView img;
        TextView marca;
        TextView fecha;
        TextView status;

        public ViewHolderFotos(View itemView) {
            super(itemView);


            img = (ImageView) itemView.findViewById(R.id.image);
            marca = (TextView) itemView.findViewById(R.id.txt_marca);
            fecha = (TextView) itemView.findViewById(R.id.txt_fecha);
            status = (TextView) itemView.findViewById(R.id.txt_status);


        }
    }

}
