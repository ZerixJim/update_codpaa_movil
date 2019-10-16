package com.codpaa.adapter.generic;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.AvanceGestionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Created by grim on 22/05/2017.
 */

public class AvanceGestionRecyclerAdaptar extends RecyclerView.Adapter<AvanceGestionRecyclerAdaptar.ViewHolder> {

    private List<AvanceGestionModel> list;
    private Context context;
    private ItemCheckListener listener;


    public interface ItemCheckListener{

        void onItemCheck(int cantidadCkecked);

    }


    public AvanceGestionRecyclerAdaptar(Context context, List<AvanceGestionModel> list, ItemCheckListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_avance_gestion_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final AvanceGestionModel data = list.get(i);

        String estatus = data.getEstatus() == null ? "" : data.getEstatus();

        viewHolder.estatus.setText(estatus + " " + data.getFecha());

        viewHolder.checkBox.setChecked(data.isCheck());


        if(data.getEstatus() != null){

            viewHolder.estatusEnvio.setText(data.getEstatus());
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                data.setCheck(isChecked);

                listener.onItemCheck(getCountItemsSelecteds());

            }
        });


        viewHolder.content.setText(data.getContent());



        if(data.getFolio() > 0 && data.getFirma() != null){


            viewHolder.checkBox.setVisibility(View.INVISIBLE);

            viewHolder.estatusFirma.setText(String.format(Locale.getDefault(),"folio: %d", data.getFolio()));
            //viewHolder.estatusFirma.setTextColor(Color.rgb(145, 255, 137));

        }

        if (data.getFolio() == 0 && data.getFirma() != null){


            viewHolder.checkBox.setVisibility(View.INVISIBLE);

            viewHolder.estatusFirma.setText("Firmado");

            viewHolder.estatusEnvio.setText("no enviado");


        }


        if (data.getFolio() > 0 ){

            viewHolder.estatus.setText("Folio Generado " + data.getFecha());

        }



    }

    private int getCountItemsSelecteds(){

        int count = 0;

        for (AvanceGestionModel item: list){

            if (item.isCheck()){

                count++;

            }

        }
        return count;

    }

    public List<AvanceGestionModel> getSelectedItems(){

        List<AvanceGestionModel> array = new ArrayList<>();

        for (AvanceGestionModel item: list){

            if (item.isCheck()){

                array.add(item);

            }

        }

        return array;
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView estatus, content, estatusFirma, estatusEnvio;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            estatus = (TextView) itemView.findViewById(R.id.estatus);
            checkBox = (CheckBox) itemView.findViewById(R.id.check);
            content = (TextView) itemView.findViewById(R.id.content);
            estatusFirma = (TextView) itemView.findViewById(R.id.estatus_firma);
            estatusEnvio = (TextView) itemView.findViewById(R.id.estatus_envio);

            itemView.setOnCreateContextMenuListener(this);


        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Selecciona la accion");
            menu.add(0,v.getId(),0, "Eliminar");
            menu.add(0, v.getId(), 0, "Modificar");
        }
    }




}
