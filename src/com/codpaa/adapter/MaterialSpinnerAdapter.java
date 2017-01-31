package com.codpaa.adapter;

/*
 * Created by grim on 30/01/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.MaterialModel;

import java.util.List;

public class MaterialSpinnerAdapter extends ArrayAdapter<MaterialModel>{

    private List<MaterialModel> materiales;
    private Context context;
    private LayoutInflater layoutInflater;
    private MaterialModel materialModel;
    private int listSize;

    private class ViewHolder{
        TextView nombreMaterial, divider;
        ImageView image;
    }


    public MaterialSpinnerAdapter(Context context, int resource, List<MaterialModel> objects) {
        super(context, resource, objects);

        materiales = objects;
        this.context = context;

        listSize = objects.size();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder viewHolder;

        if (row == null){
            row = layoutInflater.inflate(R.layout.spinner_materiales, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombreMaterial = (TextView) row.findViewById(R.id.material);
            viewHolder.image = (ImageView) row.findViewById(R.id.image);
            viewHolder.divider = (TextView) row.findViewById(R.id.divider);

            row.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) row.getTag();
        }



        materialModel = materiales.get(position);


        viewHolder.nombreMaterial.setText(materialModel.getNombreMaterial());
        //viewHolder.image.setVisibility(View.GONE);



        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;

        if (row == null){
            row = layoutInflater.inflate(R.layout.spinner_materiales, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombreMaterial = (TextView) row.findViewById(R.id.material);
            viewHolder.image = (ImageView) row.findViewById(R.id.image);
            viewHolder.divider = (TextView) row.findViewById(R.id.divider);

            row.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) row.getTag();
        }



        materialModel = materiales.get(position);


        viewHolder.nombreMaterial.setText(materialModel.getNombreMaterial());



        if (position == listSize - 1){
            viewHolder.divider.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.divider.setVisibility(View.VISIBLE);
        }

        return row;
    }
}
