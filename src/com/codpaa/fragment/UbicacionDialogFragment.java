package com.codpaa.fragment;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.codpaa.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/*
 * Created by grim on 05/10/2017.
 */

public class UbicacionDialogFragment extends DialogFragment {


    public static UbicacionDialogFragment getInstance(String url, String lat, String lon){

        UbicacionDialogFragment u = new UbicacionDialogFragment();

        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("lat", lat);
        args.putString("lon", lon);

        u.setArguments(args);

        return u;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ubicacion, container, false);

        final String url = getArguments().getString("url");
        final String lat = getArguments().getString("lat");
        final String lon = getArguments().getString("lon");


        ImageView imageView =  view.findViewById(R.id.image);
        final ProgressBar progressBar = view.findViewById(R.id.progress);
        Button button =  view.findViewById(R.id.btn_ubicacion);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "geo:" + lat + ","
                                + lon + "?q=" + lat
                                + "," + lon;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                getActivity().startActivity(intent);



            }
        });


        Picasso.get().load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });


        return view;
    }

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setPositiveButton("ubicacion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("push", "click");
                    }
                }).setTitle("Ubicacion")
                .create();
    }*/
}
