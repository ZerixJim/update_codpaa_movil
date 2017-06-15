package com.codpaa.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.db.BDopenHelper;


/*
 * Created by grim on 15/06/2017.
 */

public class DetalleProductoDialogFragment extends DialogFragment {



    public static DetalleProductoDialogFragment newInstance(){

        return new DetalleProductoDialogFragment();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dialog_detalle_producto, container, false);


        TextView detalle = (TextView) view.findViewById(R.id.description);
        //ImageView imageView = (ImageView) view.findViewById(R.id.image);

        Bundle arg = getArguments();

        if (arg != null){

            int idProducto = arg.getInt("idProducto", 0);


            if (idProducto > 0){

                SQLiteDatabase sql = new BDopenHelper(getContext()).getReadableDatabase();

                Cursor cursor = sql.rawQuery("select descripcion from producto where idProducto=" + idProducto  , null);

                if (cursor.getCount() > 0){

                    cursor.moveToFirst();

                    String sDetalle = cursor.getString(cursor.getColumnIndex("descripcion"));

                    //Picasso.with(getContext()).load(Utilities.PRODUCT_PATH+producto.getIdMarca()+"/"+producto.getIdProducto()+".gif")

                    detalle.setText(sDetalle);


                }

                cursor.close();

                sql.close();


            }

        }




        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
