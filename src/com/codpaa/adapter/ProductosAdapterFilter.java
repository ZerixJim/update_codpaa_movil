package com.codpaa.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.codpaa.R;
import com.codpaa.model.ProductosModel;
import com.codpaa.util.Utilities;

import java.util.ArrayList;


public class ProductosAdapterFilter extends ArrayAdapter<ProductosModel> implements Filterable {

    private ArrayList<ProductosModel> mOriginalData;
    private ArrayList<ProductosModel> arrayList;
    private LayoutInflater inflater;
    private ProductosModel productosModel = null;
    private Context context;


    private static class ViewHolder {
        TextView nombreProducto;
        TextView presentacion;
        ImageView imageView;
        CheckBox checkBox;
        ProductosModel productosModel;
    }


    public ProductosAdapterFilter(Context context, int textViewResourceId,
                                  ArrayList<ProductosModel> objects) {

        super(context, textViewResourceId, objects);

        this.context = context;

        this.arrayList = objects;

        mOriginalData = new ArrayList<>(objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Nullable
    @Override
    public ProductosModel getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        final ViewHolder viewHolder;

        if (row == null) {
            row = inflater.inflate(R.layout.custom_view_productos, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombreProducto = row.findViewById(R.id.textProNombre);
            viewHolder.presentacion = row.findViewById(R.id.textProducPresentacion);
            viewHolder.checkBox = row.findViewById(R.id.checkProduct);
            viewHolder.imageView = row.findViewById(R.id.image);


            row.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) row.getTag();
            viewHolder.productosModel = productosModel;
        }

        productosModel = arrayList.get(position);

        //Log.d("idMarca adapter", "" + productosModel.getIdMarca());


        if (productosModel.getIdProducto() == 0) {

            viewHolder.presentacion.setVisibility(View.GONE);

            viewHolder.checkBox.setVisibility(View.INVISIBLE);
            viewHolder.imageView.setVisibility(View.GONE);

        } else {

            viewHolder.presentacion.setVisibility(View.VISIBLE);


            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Utilities.PRODUCT_PATH + productosModel.getIdMarca() + "/" + productosModel.getIdProducto() + ".gif")
                    .into(viewHolder.imageView);
        }

        viewHolder.nombreProducto.setText(productosModel.getNombre());
        viewHolder.presentacion.setText(productosModel.getPresentacion());


        viewHolder.checkBox.setVisibility(View.INVISIBLE);
        //viewHolder.checkBox.setChecked(productosModel.isChecked());


        return row;
    }


    @NonNull
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<ProductosModel> filterArrayList = new ArrayList<>();


                if (constraint == null || constraint.length() == 0) {

                    results.count = mOriginalData.size();
                    results.values = mOriginalData;

                } else {

                    constraint = constraint.toString().toLowerCase();


                    for (ProductosModel pm : mOriginalData) {

                        if (pm.getNombre().toLowerCase().contains(constraint)) {

                            filterArrayList.add(pm);

                        }

                    }

                    results.values = filterArrayList;
                    results.count = filterArrayList.size();

                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                ArrayList<ProductosModel> tempList = (ArrayList<ProductosModel>) results.values;


                if (results.count == 0) {

                    final ProductosModel model = new ProductosModel();
                    model.setNombre("Selecciona produco");
                    model.setIdProducto(0);
                    tempList.add(model);

                }

                // notifyDataSetChanged();

                clear();
                addAll(tempList);




            }
        };

    }

    public ArrayList<ProductosModel> getProductos() {
        return arrayList;
    }


}
