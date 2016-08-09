package com.codpaa.adapter;/*
 * Created by Gustavo on 29/03/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.activity.MessaginActivity;
import com.codpaa.model.MensajeModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MensajesRecyclerAdapter extends RecyclerView.Adapter<MensajesRecyclerAdapter.MensajesViewHolder>{


    private Context context;
    private List<MensajeModel> mensajesArray;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public MensajesRecyclerAdapter(Context context) {

        this.context = context;


    }

    public void setItems(List<MensajeModel> mensajesArray){
        this.mensajesArray = mensajesArray;
    }

    @Override
    public MensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mensajes_card, parent, false);


        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MensajesViewHolder holder, int position) {

        final MensajeModel mModel = mensajesArray.get(position);



        String time[] = mModel.getDateTime().split(" ");

        holder.txtAsunto.setText(mModel.getAsunto());
        holder.txtMensaje.setText(mModel.getMensaje());
        holder.txtFecha.setText(getTimeAgo(DATE_FORMAT, mModel.getDateTime()));
        holder.txtTime.setText(time[1]);

        if (mModel.isVisto()){
            holder.imgVisto.setVisibility(View.VISIBLE);


            holder.txtAsunto.setTextColor(context.getResources().getColor(R.color.text_primary_leido));
            holder.txtMensaje.setTextColor(context.getResources().getColor(R.color.text_second_leido));

            if (mModel.isEnviado()){
                holder.imgVisto.setImageResource(R.drawable.ic_done_all_green_500_18dp);
            }else {
                holder.imgVisto.setImageResource(R.drawable.ic_done_grey_600_18dp);
            }


        } else {
            holder.imgVisto.setVisibility(View.INVISIBLE);

            holder.txtAsunto.setTextColor(Color.BLACK);
            holder.txtMensaje.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return mensajesArray.size();
    }

    public void notifyChange(){
        mensajesArray.clear();
        this.notifyDataSetChanged();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtAsunto, txtMensaje, txtFecha, txtTime;
        ImageView imgVisto;

        public MensajesViewHolder(View itemView) {
            super(itemView);

            txtAsunto = (TextView) itemView.findViewById(R.id.asunto);
            txtMensaje = (TextView) itemView.findViewById(R.id.mensaje);
            txtFecha = (TextView) itemView.findViewById(R.id.fecha);
            txtTime = (TextView) itemView.findViewById(R.id.time);

            imgVisto = (ImageView) itemView.findViewById(R.id.image_favorito);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            MensajeModel mensajeModel = mensajesArray.get(getAdapterPosition());
            Intent i = new Intent(context, MessaginActivity.class);

            i.putExtra("content", mensajeModel.getMensaje());
            i.putExtra("idMensaje", mensajeModel.getIdMensaje());
            i.putExtra("idServidor", mensajeModel.getIdServidor());

            context.startActivity(i);
        }
    }


    private String getTimeAgo(String formato, String myTime){
        String response = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(formato, Locale.getDefault());

        try {
            Date date = dateFormat.parse(myTime);

            long dateMilli = date.getTime();
            long currentTime = System.currentTimeMillis();

            response = DateUtils.getRelativeTimeSpanString(dateMilli,
                    currentTime, DateUtils.MINUTE_IN_MILLIS).toString();

            Log.d("fecha", " " + DateUtils.getRelativeTimeSpanString(dateMilli, currentTime, DateUtils.MINUTE_IN_MILLIS));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return response;
    }
}
