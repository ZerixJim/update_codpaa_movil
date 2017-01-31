package com.codpaa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codpaa.R;

/*
 * Created by grim on 30/01/17.
 */

public class DialogMaterialRequest extends DialogFragment {

    public static DialogMaterialRequest newInstance(){

        return new DialogMaterialRequest();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_material_request, container, false);



        return v;
    }
}
