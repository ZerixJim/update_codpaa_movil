package com.codpaa.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.generic.Producto;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by grim on 23/05/2017.
 */

public class ProcesoAceptacionRecyclerAdapter extends RecyclerView.Adapter<ProcesoAceptacionRecyclerAdapter.ViewHolder> {


    private List<Producto> list;
    private Context context;
    private ItemCheckListener listener;

    public ProcesoAceptacionRecyclerAdapter(Context context, List<Producto> list, ItemCheckListener listener) {
        this.list = list;
        this.context = context;

        this.listener= listener;
    }


    public interface ItemCheckListener{

        void onItemCheck(int cantidadCkecked);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_proceso_aceptacion_row, viewGroup, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Producto data = list.get(position);

        holder.nombre.setText(data.getNombre());
        holder.presentacion.setText(data.getPresentacion());

        holder.checkBox.setChecked(data.isChecked());

        holder.estatus.setText(data.getFecha());

        holder.cantidad.setText("Cantidad: " + data.getCantidad());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setChecked(isChecked);

                listener.onItemCheck(getCountChecked());

            }
        });


        if (data.getEstatusProceso() != null){

            holder.cantidad.setVisibility(View.VISIBLE);
            holder.radioGroup.setVisibility(View.GONE);

            holder.estatus.setText(data.getEstatusProceso());

        }else {

            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    //data.setEstatusProceso();

                    RadioButton radioButton = (RadioButton) holder.radioGroup.findViewById(checkedId);

                    //Log.d("text", radioButton.getText().toString());

                    data.setEstatusProceso(radioButton.getText().toString());


                }
            });

        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public List<Producto> getSelectProducts(){

        List<Producto> array = new ArrayList<>();

        for (Producto producto: list){

            if (producto.getEstatusProceso() != null){

                array.add(producto);


            }

        }

        return array;

    }


    public int getCountChecked(){
        int count= 0;

        for (Producto producto: list){

            if(producto.isChecked()){

                count++;

            }


        }


        return count;
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        TextView estatus, nombre, presentacion, cantidad;
        RadioGroup radioGroup;
        CheckBox checkBox;
        EditText editText;


        public ViewHolder(View itemView) {
            super(itemView);

            estatus = (TextView) itemView.findViewById(R.id.estatus_proceso);
            nombre = (TextView) itemView.findViewById(R.id.producto_nombre);
            presentacion = (TextView) itemView.findViewById(R.id.presentacion);

            radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

            editText = (EditText) itemView.findViewById(R.id.cantidad_entrega);
            cantidad = (TextView) itemView.findViewById(R.id.cantidad);


        }
    }



}
