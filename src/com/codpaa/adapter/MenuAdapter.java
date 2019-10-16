package com.codpaa.adapter;/*
 * Created by grim on 14/06/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.activity.CalendarioRuta;
import com.codpaa.activity.EnviarInformacion;
import com.codpaa.activity.ListaMensajesActivity;
import com.codpaa.model.MenuModel;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<MenuModel> menuModels;

    public MenuAdapter(List<MenuModel> menuModels, Context context) {
        this.menuModels = menuModels;
        this.context = context;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu_pricipal, parent, false);

        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        MenuModel menuModel = menuModels.get(position);

        holder.title.setText(menuModel.getMenu());


        if (menuModel.getChange() >= 1){
            if (holder.count.getVisibility() == View.INVISIBLE){
                holder.count.setVisibility(View.VISIBLE);

            }

            holder.count.setText(String.format("%s", menuModel.getCount()));
        } else {
            holder.count.setVisibility(View.INVISIBLE);

        }




        Uri uri = Uri.parse("android.resource://com.codpaa/drawable/"+ menuModel.getImage());

        //Picasso.with(context).load(uri).into(holder.imageView);

        holder.imageView.setImageURI(uri);

    }


    public List<MenuModel> getAllItems(){
        return menuModels;
    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView title, count;
        CardView card;

        public MenuViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.image);
            title =  itemView.findViewById(R.id.menu_title);
            card =  itemView.findViewById(R.id.card);
            count =  itemView.findViewById(R.id.count);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            MenuModel menuModel = menuModels.get(getAdapterPosition());
            switch (menuModel.getId()){
                case 1:
                    Intent ruta = new Intent(context, CalendarioRuta.class);
                    ruta.putExtra("idCelular", menuModel.getIdPromotor());
                    context.startActivity(ruta);
                    break;

                case 2:
                    Intent mensaje = new Intent(context, ListaMensajesActivity.class);

                    MenuModel item = menuModels.get(2);
                    item.setChange(0);

                    notifyDataSetChanged();

                    context.startActivity(mensaje);
                    break;

                case 3:
                    Intent enviar = new Intent(context, EnviarInformacion.class);
                    context.startActivity(enviar);
                    break;
            }
        }
    }
}
