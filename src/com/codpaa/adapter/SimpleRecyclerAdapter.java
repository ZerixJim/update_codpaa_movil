package com.codpaa.adapter;
/*
 * Created by Gustavo on 20/10/2015.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.activity.MenuTienda;
import com.codpaa.model.RutaDia;


import java.util.ArrayList;
import java.util.List;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.DiaViewHolder>{


    List<RutaDia> rutaDias;
    Boolean isHomeList = false;

    public static List<String> homeActivitiesList = new ArrayList<>();
    public static List<String> homeActivitiesSubList = new ArrayList<>();
    Context context;
    private int idPromotor;



    public void setHomeActivitiesList(Context context) {
        String[] listArray = context.getResources().getStringArray(R.array.home_activities);
        String[] subTitleArray = context.getResources().getStringArray(R.array.home_activities_subtitle);
        for (int i = 0; i < listArray.length; ++i) {
            homeActivitiesList.add(listArray[i]);
            homeActivitiesSubList.add(subTitleArray[i]);
        }
    }



    public SimpleRecyclerAdapter(Context context, List<RutaDia> rutaDias, int idPromotor) {
        isHomeList = false;

        this.context = context;
        this.rutaDias = rutaDias;
        this.idPromotor = idPromotor;

    }

    @Override
    public DiaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);

        return new DiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiaViewHolder dia, int i) {
        if (isHomeList) {
            dia.title.setText(homeActivitiesList.get(i));
            dia.subTitle.setText(homeActivitiesSubList.get(i));
        } else {
            RutaDia diaModel = rutaDias.get(i);
            dia.title.setText(diaModel.getNombreTienda());
            dia.subTitle.setText(diaModel.getSucursal());
            dia.rol.setText(diaModel.getRol());
            dia.hora.setText(diaModel.getHora());
            dia.number.setText(String.format("%d", i + 1));

        }
    }

    @Override
    public int getItemCount() {
        if (isHomeList)
            return homeActivitiesList == null ? 0 : homeActivitiesList.size();
        else
            return rutaDias == null ? 0 : rutaDias.size();
    }


    class DiaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItemLayout;
        TextView title;
        TextView subTitle;
        TextView rol;
        TextView hora;
        TextView number;

        public DiaViewHolder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.rutadia_tienda);
            subTitle = (TextView) itemView.findViewById(R.id.rutadia_sucursal);
            rol = (TextView) itemView.findViewById(R.id.rutadia_rol);
            hora = (TextView) itemView.findViewById(R.id.hora);
            number = (TextView) itemView.findViewById(R.id.number);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            RutaDia rutaDia = rutaDias.get(getAdapterPosition());
            Log.d("Onclick", "Element: " + rutaDia.getIdTienda());

            dialodStartComfirm(rutaDia.getIdTienda(), rutaDia);

        }


        private void startActivityRute(int idTienda){

            Intent i = new Intent(context, MenuTienda.class);
            i.putExtra("idTienda", idTienda);
            i.putExtra("idPromotor", idPromotor);
            context.startActivity(i);
        }

        private void dialodStartComfirm(final int idTienda, final RutaDia rute){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("¿Estas Seguro(a) que quieres Entrar a "+ rute.getNombreTienda() +
            " " + rute.getSucursal() + "?");

            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    startActivityRute(idTienda);

                }
            }).setNegativeButton("Cancelar", null);
            builder.create().show();
        }

    }


}
