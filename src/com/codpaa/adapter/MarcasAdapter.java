package com.codpaa.adapter;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codpaa.R;
import com.codpaa.model.MarcaModel;
import com.codpaa.util.Utilities;


import java.util.ArrayList;



public class MarcasAdapter extends ArrayAdapter<MarcaModel>{

    private Context _context;
    private ArrayList<MarcaModel> arrayMarcas;
    private LayoutInflater layoutInflater;
    private BitmapDrawable bitmapDrawable;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            bitmapDrawable = (BitmapDrawable) _context.getResources()
                    .getDrawable(R.drawable.ic_launcher, _context.getTheme());
        }else {
            bitmapDrawable = (BitmapDrawable) _context.getResources()
                    .getDrawable(R.drawable.ic_launcher);
        }

    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.row_list_marca, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombre = convertView.findViewById(R.id.NombreTxtMarca);
            viewHolder.descrip = convertView.findViewById(R.id.DescriTxtMarca);
            viewHolder.img =  convertView.findViewById(R.id.logotipoMarca);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        MarcaModel spm = arrayMarcas.get(position);
        viewHolder.nombre.setText(spm.getNombre());


        viewHolder.descrip.setText("");


        //Picasso picasso = Picasso.get();

        //picasso.setIndicatorsEnabled(true);


        if (spm.getId() != 0){

            viewHolder.img.setVisibility(View.VISIBLE);


            Glide.with(_context).load(Utilities.MARCA_PATH + "/" + spm.getId() + ".gif")
                    .placeholder(R.drawable.ic_crop_original_grey600_36dp)
                    .error(R.drawable.ic_error_grey600_36dp)
                    .into(viewHolder.img);


           /* picasso.load(Utilities.MARCA_PATH + "/" + spm.getId() + ".gif")
                    .resize(bitmapDrawable.getBitmap().getWidth(),0)
                    .placeholder(R.drawable.ic_crop_original_grey600_36dp)
                    .error(R.drawable.ic_error_grey600_36dp)
                    .into(viewHolder.img);*/
        }else {
            viewHolder.img.setVisibility(View.GONE);
        }


        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.row_list_marca, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nombre =  convertView.findViewById(R.id.NombreTxtMarca);
            viewHolder.descrip =  convertView.findViewById(R.id.DescriTxtMarca);
            viewHolder.img = convertView.findViewById(R.id.logotipoMarca);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        MarcaModel spm = arrayMarcas.get(position);
        viewHolder.nombre.setText(spm.getNombre());


        viewHolder.nombre.setPadding(0,15,0,0);


        viewHolder.descrip.setText("");



        viewHolder.img.setVisibility(View.GONE);



        return convertView;
    }


}
