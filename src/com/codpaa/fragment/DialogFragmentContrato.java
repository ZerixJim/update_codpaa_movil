package com.codpaa.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codpaa.R;
import com.github.gcacace.signaturepad.views.SignaturePad;

/*
 * Created by grim on 22/05/2017.
 */

public class DialogFragmentContrato extends DialogFragment {

    private SignaturePad firma;
    private SignatureListener listener;




    public static DialogFragmentContrato getIntance(){

        return new DialogFragmentContrato();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_contrato, container, false);

        firma = (SignaturePad) view.findViewById(R.id.signature);
        Button button = (Button) view.findViewById(R.id.button);
        Button buttonClose = (Button) view.findViewById(R.id.btn_close);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!firma.isEmpty()){

                    getDialog().dismiss();


                    listener.onCompleteSignature(firma.getSignatureSvg());
                }



            }
        });



        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });





        return view;
    }


    public void setOnSignatureListener(SignatureListener listener){

        this.listener = listener;

    }

    public interface SignatureListener{
        void onCompleteSignature(String firma);
    }


}
