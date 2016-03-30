package com.codpaa.adapter;/*
 * Created by Gustavo on 29/03/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.activity.MessaginActivity;
import com.codpaa.model.MensajeModel;

import java.util.List;

public class MensajesRecyclerAdapter extends RecyclerView.Adapter<MensajesRecyclerAdapter.MensajesViewHolder>{


    private Context context;
    private List<MensajeModel> mensajesArray;


    public MensajesRecyclerAdapter(Context context, List<MensajeModel> mensajeArray) {

        this.context = context;
        this.mensajesArray = mensajeArray;

    }

    @Override
    public MensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mensajes_card, parent, false);


        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MensajesViewHolder holder, int position) {

        final MensajeModel mModel = mensajesArray.get(position);

        holder.txtAsunto.setText(mModel.getAsunto());
        holder.txtMensaje.setText(mModel.getMensaje());
        holder.txtFecha.setText(mModel.getDateTime());

        if (mModel.isVisto()){
            holder.imgVisto.setVisibility(View.VISIBLE);
        } else {
            holder.imgVisto.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mensajesArray.size();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtAsunto, txtMensaje, txtFecha;
        ImageView imgVisto;

        public MensajesViewHolder(View itemView) {
            super(itemView);

            txtAsunto = (TextView) itemView.findViewById(R.id.asunto);
            txtMensaje = (TextView) itemView.findViewById(R.id.mensaje);
            txtFecha = (TextView) itemView.findViewById(R.id.fecha);

            imgVisto = (ImageView) itemView.findViewById(R.id.image_favorito);

        }

        @Override
        public void onClick(View v) {

            MensajeModel mensajeModel = mensajesArray.get(getAdapterPosition());
            Intent i = new Intent(context, MessaginActivity.class);

            i.putExtra("content", mensajeModel.getMensaje());
            i.putExtra("idMensaje", mensajeModel.getIdMensaje());


            context.startActivity(i);
        }
    }
}
