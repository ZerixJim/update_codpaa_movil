package com.codpaa.adapter;


import android.content.Context;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codpaa.R;
import com.codpaa.model.ProductosModel;
import com.codpaa.util.Utilities;

import java.util.ArrayList;


public class ProductosAdapterFilter extends ArrayAdapter<ProductosModel> implements Filterable {

    private ArrayList<ProductosModel> mOriginalData;
    private ArrayList<ProductosModel> arrayList;
    private ProductosModel productosModel = null;
    private Context context;


    private class ViewHolder {
        TextView nombreProducto;
        TextView presentacion;
        ImageView imageView;
        TextView txtBarcode;
        CheckBox checkBox;

    }


    public ProductosAdapterFilter(Context context, int textViewResourceId, ArrayList<ProductosModel> objects) {

        super(context, textViewResourceId, objects);

        this.context = context;

        this.arrayList = objects;

        mOriginalData = new ArrayList<>(objects);


    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_view_productos, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombreProducto = convertView.findViewById(R.id.textProNombre);
            viewHolder.presentacion = convertView.findViewById(R.id.textProducPresentacion);
            viewHolder.checkBox = convertView.findViewById(R.id.checkProduct);
            viewHolder.imageView = convertView.findViewById(R.id.image);

            viewHolder.txtBarcode = convertView.findViewById(R.id.text_codigo_barras);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

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

            if (productosModel.getHasImage() == 1 ){

                Glide.with(context)
                        .load(Utilities.PRODUCT_PATH + productosModel.getIdMarca() + "/" + productosModel.getIdProducto() + ".gif")
                        .placeholder(R.drawable.ic_no_image)
                        .into(viewHolder.imageView);
            }else {

                Glide.with(context)
                        .load(R.drawable.ic_no_image)
                        .into(viewHolder.imageView);

            }


        }

        viewHolder.nombreProducto.setText(productosModel.getNombre());
        viewHolder.presentacion.setText(productosModel.getPresentacion());
        viewHolder.txtBarcode.setText(productosModel.getCodigoBarras());



        viewHolder.checkBox.setVisibility(View.INVISIBLE);
        //viewHolder.checkBox.setChecked(productosModel.isChecked());


        return convertView;
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

                        if (pm.getNombre().toLowerCase().contains(constraint) || pm.getCodigoBarras().trim().contains(constraint)) {

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
