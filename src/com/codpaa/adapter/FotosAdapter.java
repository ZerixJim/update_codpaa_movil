package com.codpaa.adapter;
/*
 * Created by grim on 14/01/16.
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

        holder.tipo.setText(fo.getTipo());

        Picasso.get()
                .load(new File(fo.getImg()))
                .resize(dp2px(220), 0)
                .into(holder.img);


    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    private int dp2px(int dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return (int) (dp * displaymetrics.density + 0.5f);
    }

    class ViewHolderFotos extends RecyclerView.ViewHolder{

        ImageView img;
        TextView marca;
        TextView fecha;
        TextView status;
        TextView tipo;

        public ViewHolderFotos(View itemView) {
            super(itemView);


            img =  itemView.findViewById(R.id.image);
            marca =  itemView.findViewById(R.id.txt_marca);
            fecha =  itemView.findViewById(R.id.txt_fecha);
            status =  itemView.findViewById(R.id.txt_status);
            tipo =  itemView.findViewById(R.id.txt_tipo);


        }
    }

}
