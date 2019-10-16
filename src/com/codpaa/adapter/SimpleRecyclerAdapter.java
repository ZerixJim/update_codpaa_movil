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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.codpaa.R;
import com.codpaa.activity.MenuTienda;
import com.codpaa.fragment.UbicacionDialogFragment;
import com.codpaa.model.RutaDia;
import com.codpaa.util.Utilities;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.DiaViewHolder>
            implements Filterable{


    private List<RutaDia> rutaDias;
    private List<RutaDia> rutasFilter;
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
        this.rutasFilter = rutaDias;
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
            final RutaDia diaModel = rutasFilter.get(i);
            dia.title.setText(diaModel.getNombreTienda());
            dia.subTitle.setText(diaModel.getSucursal());
            dia.rol.setText(diaModel.getRol());
            dia.hora.setText(diaModel.getHora());
            dia.number.setText(String.format(Locale.getDefault(),"%d", i + 1));

            /*if (diaModel.getModo() == 1){
                dia.modo.setText("promotoria");
            }else if(diaModel.getModo() == 2){
                dia.modo.setText("impulsor");
            }*/

            Picasso.get().load(Utilities.FORMATOS_PATH + diaModel.getFormato() + ".png").into(dia.image);


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
                                .appendQueryParameter("zoom", "19")
                                .appendQueryParameter("scale", "false")
                                .appendQueryParameter("format", "png")
                                .appendQueryParameter("key", "AIzaSyCDb3AWOIzMkm-_tdCezft9_ygxrgZ3__0")
                                .appendQueryParameter("markers", "color:orange|" +
                                        "label:T|"+ diaModel.getLatitud() + "," +
                                        diaModel.getLongitud())
                                .appendQueryParameter("size", "500x800");

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
        return rutasFilter.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                String charString = charSequence.toString();

                if (charString.isEmpty()){

                    rutasFilter = rutaDias;

                }else {


                    List<RutaDia> filterList = new ArrayList<>();


                    for (RutaDia row: rutaDias){


                        //Log.i("filter","'"+ row.getNombreTienda().toLowerCase() + "' '" + charString.toLowerCase() +"'");



                        if (row.getNombreTienda().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSucursal().toLowerCase().contains(charString.toLowerCase())){



                            filterList.add(row);

                        }


                    }


                    rutasFilter = filterList;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = rutasFilter;

                return filterResults;



            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                rutasFilter = (ArrayList<RutaDia>) filterResults.values;


                notifyDataSetChanged();

            }
        };


    }


    public class DiaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItemLayout;
        TextView title;
        TextView subTitle;
        TextView rol;
        TextView hora;
        TextView number;
        TextView modo;
        ImageView ubicacion, image;

        public DiaViewHolder(View itemView) {
            super(itemView);

            cardItemLayout =  itemView.findViewById(R.id.cardlist_item);
            title =  itemView.findViewById(R.id.rutadia_tienda);
            subTitle =  itemView.findViewById(R.id.rutadia_sucursal);
            rol =  itemView.findViewById(R.id.rutadia_rol);
            hora =  itemView.findViewById(R.id.hora);
            number =  itemView.findViewById(R.id.number);
            modo =  itemView.findViewById(R.id.modo);
            ubicacion = itemView.findViewById(R.id.ubicacion);

            image = itemView.findViewById(R.id.image);


            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            RutaDia rutaDia = rutasFilter.get(getAdapterPosition());
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
