package com.codpaa.adapter;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.model.MarcaModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class MarcasAdapter extends ArrayAdapter<MarcaModel>{

    Context _context;
    private ArrayList<MarcaModel> arrayMarcas;
    LayoutInflater layoutInflater;
    BitmapDrawable bitmapDrawable;

    private class ViewHolder{
        TextView nombre;
        TextView descrip;
        ImageView img;
    }

    public MarcasAdapter(Context context, int resource, ArrayList<MarcaModel> objects) {
        super(context, resource, objects);

        this._context = context;
        this.arrayMarcas = objects;

        layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bitmapDrawable = (BitmapDrawable) _context.getResources()
                .getDrawable(R.drawable.ic_launcher);

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.row_list_marca, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombre = (TextView) convertView.findViewById(R.id.NombreTxtMarca);
            viewHolder.descrip = (TextView) convertView.findViewById(R.id.DescriTxtMarca);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.logotipoMarca);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        MarcaModel spm = arrayMarcas.get(position);
        viewHolder.nombre.setText(spm.getNombre());
        viewHolder.descrip.setText("");




        Picasso picasso = Picasso.with(_context);

        //picasso.setIndicatorsEnabled(true);

        picasso.load(spm.getUrl())
                .resize(bitmapDrawable.getBitmap().getWidth(),bitmapDrawable.getBitmap().getHeight())
                .centerCrop()
                .placeholder(R.drawable.ic_crop_original_grey600_36dp)
                .error(R.drawable.ic_error_grey600_36dp)
                .into(viewHolder.img);


        return convertView;

    }
}
