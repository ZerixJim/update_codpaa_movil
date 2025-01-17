package com.codpaa.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.util.Utilities;


import java.util.List;

/*
 * Created by grim on 22/08/2016.
 */
public class RecyclerFrentesAdapter extends RecyclerView.Adapter<RecyclerFrentesAdapter.FrentesViewHolder> {

    private Context context;
    private List<SpinnerProductoModel> listFrentes;
    String frentesCaptura[] = {"Frentes en Mueble", "Frentes linea de Cajas", "Inventario", "Inteligencia de Mercado"};

    public RecyclerFrentesAdapter(Context context, List<SpinnerProductoModel> list) {
        this.context = context;
        this.listFrentes = list;
    }

    public static class FrentesViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {

        EditText c1, c2, c3, c4, c5, c6;
        TextView nombreProducto, presentacion, codigoBarras;
        ImageView image;
        EditText f1,f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14;
        Spinner spinner;
        LinearLayout listCharola, listFilas;

        public FrentesViewHolder(View itemView) {
            super(itemView);

            nombreProducto =  itemView.findViewById(R.id.nombre_producto);
            presentacion =  itemView.findViewById(R.id.presentacion);
            codigoBarras =  itemView.findViewById(R.id.codigo_barras);

            c1 =  itemView.findViewById(R.id.c1);
            c2 =  itemView.findViewById(R.id.c2);
            c3 =  itemView.findViewById(R.id.c3);
            c4 =  itemView.findViewById(R.id.c4);
            c5 =  itemView.findViewById(R.id.c5);
            c6 =  itemView.findViewById(R.id.c6);


            f1 =  itemView.findViewById(R.id.unifila);
            f2 =  itemView.findViewById(R.id.caja2);
            f3 =  itemView.findViewById(R.id.caja3);
            f4 =  itemView.findViewById(R.id.caja4);
            f5 =  itemView.findViewById(R.id.caja5);
            f6 =  itemView.findViewById(R.id.caja6);
            f7 =  itemView.findViewById(R.id.caja7);
            f8 =  itemView.findViewById(R.id.caja8);
            f9 =  itemView.findViewById(R.id.caja9);
            f10 =  itemView.findViewById(R.id.caja10);
            f11 =  itemView.findViewById(R.id.caja11);
            f12 =  itemView.findViewById(R.id.caja12);
            f13 =  itemView.findViewById(R.id.caja13);
            f14 =  itemView.findViewById(R.id.caja14);


            image =  itemView.findViewById(R.id.image);

            spinner =  itemView.findViewById(R.id.spinner);


            listCharola = itemView.findViewById(R.id.list_charolas);
            listFilas =  itemView.findViewById(R.id.list_filas);


            spinner.setOnItemSelectedListener(this);



        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String selected = parent.getItemAtPosition(position).toString();

            switch (selected) {
                case "Frentes en Mueble":
                    if (listCharola.getVisibility() == View.GONE) {
                        listCharola.setVisibility(View.VISIBLE);
                    }

                    listFilas.setVisibility(View.GONE);
                    break;
                case "Frentes linea de Cajas":
                    if (listFilas.getVisibility() == View.GONE)
                        listFilas.setVisibility(View.VISIBLE);

                    if (listCharola.getVisibility() == View.VISIBLE)
                        listCharola.setVisibility(View.GONE);
                    break;
                case "Inventario":
                    listFilas.setVisibility(View.GONE);
                    listCharola.setVisibility(View.GONE);
                    break;
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    @Override
    public FrentesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_frente, parent, false);

        return new FrentesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FrentesViewHolder holder, int position) {


        final SpinnerProductoModel frentes = listFrentes.get(position);
        holder.nombreProducto.setText(frentes.getNombre());
        holder.presentacion.setText(frentes.getPresentacion());
        holder.codigoBarras.setText(frentes.getCodigoBarras());


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, frentesCaptura);


        holder.spinner.setAdapter(arrayAdapter);


        Glide.with(context).load(Utilities.PRODUCT_PATH+frentes.getIdMarca()+"/"+frentes.getIdProducto()+".gif")
                .into(holder.image);


        holder.c1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listFrentes.size();
    }
}
