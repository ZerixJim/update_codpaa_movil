package com.codpaa.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codpaa.R;

public class MedicionAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;

    public MedicionAdapter(Activity context, String[] maintitle, String[] subtitle){
        super(context, R.layout.custom_view);

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_view, null,true);

        TextView titleText = rowView.findViewById(R.id.headerCategoria);
        TextView subtitleText = rowView.findViewById(R.id.cantidadMedicion);

        titleText.setText(maintitle[position]);
        subtitleText.setText(subtitle[position]);

        return rowView;
    }
}
