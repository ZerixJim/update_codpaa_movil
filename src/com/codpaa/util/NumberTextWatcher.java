package com.codpaa.util;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.DecimalFormat;


public class NumberTextWatcher implements TextWatcher {



    private final EditText et;
    private String current = "";
    private final String INIT = "0.00";


    public NumberTextWatcher(final EditText editText) {

        this.et = editText;


        et.setText(INIT);
        et.setSelection(INIT.length());

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){

                    try {
                        final EditText editText1 = (EditText)view;
                        editText1.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("onfocus", "focus" + editText1.getSelectionStart());
                                editText1.setSelection(editText1.getText().length());
                            }
                        });
                    }catch (Exception e){

                        e.printStackTrace();
                    }


                }
            }
        });


    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        try{


            if (!s.toString().equals(current)) {
                et.removeTextChangedListener(this);




                String cleanString = s.toString().replaceAll("[$,.]", "");
                Log.d("clean", cleanString);

                double parsed = Double.parseDouble(cleanString);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                decimalFormat.setDecimalSeparatorAlwaysShown(true);


                //String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                String formatted = decimalFormat.format((parsed / 100));

                current = formatted;
                et.setText(formatted);
                et.setSelection(formatted.length());

                et.addTextChangedListener(this);

            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void afterTextChanged(Editable s) {


    }
}
