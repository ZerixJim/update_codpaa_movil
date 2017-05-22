package com.codpaa.adapter.generic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.AvanceGestionModel;

import java.util.List;

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

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                data.setCheck(isChecked);

                listener.onItemCheck(getCountItemsSelecteds());

            }
        });


        viewHolder.content.setText(data.getContent());

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



    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView estatus, content;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            estatus = (TextView) itemView.findViewById(R.id.estatus);
            checkBox = (CheckBox) itemView.findViewById(R.id.check);
            content = (TextView) itemView.findViewById(R.id.content);


        }
    }




}
