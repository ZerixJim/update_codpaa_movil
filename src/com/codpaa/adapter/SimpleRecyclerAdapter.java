package com.codpaa.adapter;
/*
 * Created by Gustavo on 20/10/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.codpaa.R;
import com.codpaa.activity.MenuTienda;
import com.codpaa.fragment.UbicacionDialogFragment;
import com.codpaa.model.RutaDia;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.DiaViewHolder>{


    private List<RutaDia> rutaDias;
    private Boolean isHomeList = false;

    private static List<String> homeActivitiesList = new ArrayList<>();
    private static List<String> homeActivitiesSubList = new ArrayList<>();
    private Context context;
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
            final RutaDia diaModel = rutaDias.get(i);
            dia.title.setText(diaModel.getNombreTienda());
            dia.subTitle.setText(diaModel.getSucursal());
            dia.rol.setText(diaModel.getRol());
            dia.hora.setText(diaModel.getHora());
            dia.number.setText(String.format(Locale.getDefault(),"%d", i + 1));

            if (diaModel.getModo() == 1){
                dia.modo.setText("promotoria");
            }else if(diaModel.getModo() == 2){
                dia.modo.setText("impulsor");
            }

            if (!diaModel.getLongitud().isEmpty() && !diaModel.getLatitud().isEmpty()){


                dia.ubicacion.setVisibility(View.VISIBLE);
                dia.ubicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri.Builder builder = new Uri.Builder();
                        builder.scheme("https")
                                .authority("maps.googleapis.com")
                                .appendPath("maps")
                                .appendPath("api")
                                .appendPath("staticmap")
                                .appendQueryParameter("center", diaModel.getLatitud() + "," + diaModel.getLongitud())
                                .appendQueryParameter("zoom", "15")
                                .appendQueryParameter("scale", "false")
                                .appendQueryParameter("format", "png")
                                //.appendQueryParameter("key", "AIzaSyCSdyBL0a7eYhfUhZPKVGKcyI3A5A_xQwY")
                                .appendQueryParameter("markers", "color:orange|" +
                                        "label:T|"+ diaModel.getLatitud() + "," +
                                        diaModel.getLongitud())
                                .appendQueryParameter("size", "400x400");

                        String url = builder.build().toString();

                        /*String uri = "geo:" + diaModel.getLatitud() + ","
                                + diaModel.getLongitud() + "?q=" + diaModel.getLatitud()
                                + "," + diaModel.getLongitud();*/

                        //Log.d("url", url);

                        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        //intent.setType("image/png");
                        //context.startActivity(intent);


                        //Toast.makeText(context, "pulsaste", Toast.LENGTH_SHORT).show();

                        UbicacionDialogFragment dialog =
                                UbicacionDialogFragment.getInstance(url,diaModel.getLatitud(),
                                        diaModel.getLongitud());

                        FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
                        Fragment prev = ((Activity)context).getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null){
                            ft.remove(prev);
                        }

                        ft.addToBackStack(null);

                        dialog.show(ft, "dialog");

                    }
                });


            }else {
                dia.ubicacion.setVisibility(View.INVISIBLE);
            }





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
        TextView modo;
        ImageView ubicacion;

        public DiaViewHolder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.rutadia_tienda);
            subTitle = (TextView) itemView.findViewById(R.id.rutadia_sucursal);
            rol = (TextView) itemView.findViewById(R.id.rutadia_rol);
            hora = (TextView) itemView.findViewById(R.id.hora);
            number = (TextView) itemView.findViewById(R.id.number);
            modo = (TextView) itemView.findViewById(R.id.modo);
            ubicacion = (ImageView) itemView.findViewById(R.id.ubicacion);


            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            RutaDia rutaDia = rutaDias.get(getAdapterPosition());
            //Log.d("Onclick", "Element: " + rutaDia.getIdTienda());

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

                    try{

                        ((Activity)context).finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }).setNegativeButton("Cancelar", null);
            builder.create().show();
        }

    }


}
